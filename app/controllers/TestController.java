package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import models.Algorithm;
import models.Feature;
import models.FeatureSet;
import models.ParameterTestValue;
import models.ResultSet;
import models.User;
import play.Logger;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.libs.ws.WSClient;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.tests.run;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class TestController extends Controller {

    @Inject
    private FormFactory formFactory;

    @Inject
    private WSClient ws;

    @Security.Authenticated(Secured.class)
    public Result create() {
        User user = User.find.where().eq("email", request().username()).findUnique();
        List<FeatureSet> featureSets = FeatureSet.find.where().eq("user_id", user.getId()).findList();
        List<Algorithm> algorithms = Algorithm.find.where().eq("user_id", user.getId()).findList();

        return ok(run.render(Json.toJson(featureSets).toString(), Json.toJson(algorithms).toString()));
    }

    @Security.Authenticated(Secured.class)
    @BodyParser.Of(BodyParser.Json.class)
    public CompletionStage<Result> run() {
        Form<TestData> testForm = formFactory.form(TestData.class).bindFromRequest();
        if (testForm.hasErrors()) {
            return CompletableFuture.supplyAsync(() -> badRequest(testForm.errorsAsJson()));
        }

        FeatureSet featureSet = FeatureSet.find.byId(testForm.get().getFeatureSetId());
        if (featureSet == null) {
            ObjectNode response = Json.newObject();
            response.set("error", Json.toJson("The featureSet does not exist"));
            return CompletableFuture.supplyAsync(() -> badRequest(response));
        }

        Algorithm algorithm = Algorithm.find.byId(testForm.get().getAlgorithmId());
        if (algorithm == null) {
            ObjectNode response = Json.newObject();
            response.set("error", Json.toJson("The algorithm does not exist"));
            return CompletableFuture.supplyAsync(() -> badRequest(response));
        }

        ObjectNode wrapper = this.assembleRequestBody(testForm, featureSet);

        ResultSet resultSet = new ResultSet();
        resultSet.setFeatureSet(featureSet);
        resultSet.setAlgorithm(algorithm);

        if (testForm.get().getParameters() != null) {
            resultSet.setParameterTestValues(
                testForm.get().getParameters().stream()
                    .map(
                        p -> new ParameterTestValue(
                            algorithm.getParameters().stream()
                                .filter(d -> d.getId() == p.getId())
                                .findFirst()
                                .orElseThrow(AssertionError::new),
                            p.getValue()
                        )
                    )
                    .collect(Collectors.toList())
            );
        }

        if (testForm.get().getExcludeLabels() != null) {
            resultSet.setIgnoredLabels(
                testForm.get().getExcludeLabels().stream()
                    .map(
                        el -> featureSet.getLabels().stream()
                            .filter(l -> el.equals(l.getValue()))
                            .findFirst()
                            .orElse(null)
                    )
                    .collect(Collectors.toList())
            );
        }


        return ws.url(algorithm.getEndpoint()).post(wrapper).thenApply(wsResponse -> {
            JsonNode response;
            try {
                response = wsResponse.asJson();
            } catch (Exception e) {
                return badRequest("Invalid response");
            }

            if (!response.isArray()) {
                return badRequest("Invalid answer format");
            }

            ArrayNode answers = (ArrayNode) response;

            List<models.Result> results = new ArrayList<>();

            for (JsonNode current: answers) {
                if (!current.isObject()
                    || !current.has("actual")
                    || !current.has("expected")
                    || !current.get("actual").isDouble()
                    || !current.get("expected").isDouble()) {
                        return badRequest("Invalid answer format");
                }

                models.Result result = new models.Result();
                result.setActual(current.get("actual").asDouble());
                result.setExpected(current.get("expected").asDouble());
                results.add(result);
            }

            resultSet.setResults(results);
            resultSet.save();

            return ok(Json.newObject().set("id", Json.toJson(resultSet.getId())));
        });
    }

    private ObjectNode assembleRequestBody(final Form<TestData> testForm, final FeatureSet featureSet) {
        JsonNode labels = Json.toJson(featureSet.getLabelStrings());
        ArrayNode features = Json.newArray();
        for (Feature feature : featureSet.getFeatures()) {
            ObjectNode featureNode = Json.newObject();
            featureNode.set("result", Json.toJson(feature.getResult()));
            featureNode.set("data", Json.toJson(feature.getEntryStrings()));
            features.add(featureNode);
        }

        ObjectNode wrapper = Json.newObject();
        wrapper.set("allLabels", labels);
        wrapper.set("excludeLabels",
            testForm.get().getExcludeLabels() == null
                ? Json.newArray()
                : Json.toJson(testForm.get().getExcludeLabels())

        );
        wrapper.set("features", features);
        wrapper.set("parameters",
            testForm.get().getParameters() == null
                ? Json.newObject()
                : Json.toJson(
                testForm.get().getParameters().stream()
                    .collect(Collectors.toMap(
                        Parameter::getName,
                        x -> x.getType().equals("INT") || x.getType().equals("DOUBLE")
                            ? Double.parseDouble(x.getValue())
                            : x.getValue())
                    )
            )
        );

        return wrapper;
    }

    public Result testAnswer() {
        final double minValue = 1.0;
        final double maxValue = 5.0;

        JsonNode json = request().body().asJson();

        Logger.info(json.asText());

//        System.out.println(json.toString());

        ArrayNode result = Json.newArray();

        ObjectNode wrapper = (ObjectNode) json;
        ArrayNode features = (ArrayNode) wrapper.get("features");
        for (JsonNode node: features) {
            ObjectNode current = Json.newObject();
            current.set("expected", node.get("result"));
            current.set("actual", Json.toJson(ThreadLocalRandom.current().nextDouble(minValue, maxValue)));
            result.add(current);
        }

        Logger.info(result.asText());

        return ok(result);
    }

    public static class TestData {

        private Integer featureSetId;
        private Integer algorithmId;
        private List<String> excludeLabels;
        private List<Parameter> parameters;

        public Integer getFeatureSetId() {
            return featureSetId;
        }

        public void setFeatureSetId(final Integer featureSetId) {
            this.featureSetId = featureSetId;
        }

        public Integer getAlgorithmId() {
            return algorithmId;
        }

        public void setAlgorithmId(final Integer algorithmId) {
            this.algorithmId = algorithmId;
        }

        public List<String> getExcludeLabels() {
            return excludeLabels;
        }

        public void setExcludeLabels(final List<String> excludeLabels) {
            this.excludeLabels = excludeLabels;
        }

        public List<Parameter> getParameters() {
            return parameters;
        }

        public void setParameters(final List<Parameter> parameters) {
            this.parameters = parameters;
        }

        public String validate() {

            if (featureSetId == null) {
                return "You must select a feature set";
            }
            if (algorithmId == null) {
                return "You must select an algorithm";
            }

            return null;
        }
    }

    public static class Parameter {
        private int id;
        private String type;
        private String name;
        private String value;

        public int getId() {
            return id;
        }

        public void setId(final int id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(final String type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(final String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(final String value) {
            this.value = value;
        }
    }

}
