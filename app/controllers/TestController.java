package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import models.Algorithm;
import models.Feature;
import models.FeatureSet;
import models.ResultSet;
import play.Logger;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.libs.ws.WSClient;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.tests.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ThreadLocalRandom;

public class TestController extends Controller {

    @Inject
    FormFactory formFactory;

    @Inject
    WSClient ws;

    @Security.Authenticated(Secured.class)
    public Result create() {
        List<FeatureSet> featureSets = FeatureSet.find.all();
        List<Algorithm> algorithms = Algorithm.find.all();

        return ok(run.render(formFactory.form(TestData.class), featureSets, algorithms));
    }

    @Security.Authenticated(Secured.class)
    public CompletionStage<Result> run() {

        Form<TestData> testForm = formFactory.form(TestData.class).bindFromRequest();

        if (testForm.hasErrors()) {
            List<FeatureSet> featureSets = FeatureSet.find.all();
            List<Algorithm> algorithms = Algorithm.find.all();

            return CompletableFuture.supplyAsync(() -> badRequest(run.render(testForm, featureSets, algorithms)));
        }

        FeatureSet featureSet = FeatureSet.find.byId(testForm.get().getFeatureSetId());

        if(featureSet == null) {
            testForm.reject("The featureSet does not exist");

            List<FeatureSet> featureSets = FeatureSet.find.all();
            List<Algorithm> algorithms = Algorithm.find.all();

            return CompletableFuture.supplyAsync(() -> badRequest(run.render(testForm, featureSets, algorithms)));
        }

        Algorithm algorithm = Algorithm.find.byId(testForm.get().getAlgorithmId());

        if(algorithm == null) {
            testForm.reject("The algorithm does not exist");

            List<FeatureSet> featureSets = FeatureSet.find.all();
            List<Algorithm> algorithms = Algorithm.find.all();

            return CompletableFuture.supplyAsync(() -> badRequest(run.render(testForm, featureSets, algorithms)));
        }

        JsonNode labels = Json.toJson(featureSet.getLabels());
        ArrayNode features = Json.newArray();
        for(Feature feature : featureSet.getFeatures()) {
            ObjectNode featureNode = Json.newObject();
            featureNode.set("result", Json.toJson(feature.getResult()));
            featureNode.set("data", Json.toJson(feature.getData()));
            features.add(featureNode);
        }

        ObjectNode wrapper = Json.newObject();
        wrapper.set("labels", labels);
        wrapper.set("features", features);

        return ws.url(algorithm.getEndpoint()).post(wrapper).thenApply(wsResponse -> {

            JsonNode response = wsResponse.asJson();

            if(!response.isArray()) {
                return badRequest("Invalid answer format");
            }

            ArrayNode answers = (ArrayNode) response;

            ResultSet resultSet = new ResultSet();
            resultSet.setFeatureSet(featureSet);
            resultSet.setAlgorithm(algorithm);

            List<models.Result> results = new ArrayList<>();

            for(JsonNode current: answers) {
                if(!current.isObject() || !current.has("actual") || !current.has("expected") ||
                        !current.get("actual").isDouble() || !current.get("expected").isDouble()) {
                    return badRequest("Invalid answer format");
                }

                models.Result result = new models.Result();
                result.setActual(current.get("actual").asDouble());
                result.setExpected(current.get("expected").asDouble());
                results.add(result);
            }

            resultSet.setResults(results);
            resultSet.save();

            return redirect(routes.TestController.create());
        });
    }

    public Result testAnswer() {
        JsonNode json = request().body().asJson();

        Logger.info(json.asText());

        ArrayNode result = Json.newArray();

        ObjectNode wrapper = (ObjectNode) json;
        ArrayNode features = (ArrayNode) wrapper.get("features");
        for(JsonNode node: features) {
            ObjectNode current = Json.newObject();
            current.set("expected", node.get("result"));
            current.set("actual", Json.toJson(ThreadLocalRandom.current().nextDouble(1.0, 5.0)));
            result.add(current);
        }

        Logger.info(result.asText());

        return ok(result);
    }

    public static class TestData {

        protected Integer featureSetId;
        protected Integer algorithmId;

        public Integer getFeatureSetId() {
            return featureSetId;
        }

        public void setFeatureSetId(Integer featureSetId) {
            this.featureSetId = featureSetId;
        }

        public Integer getAlgorithmId() {
            return algorithmId;
        }

        public void setAlgorithmId(Integer algorithmId) {
            this.algorithmId = algorithmId;
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

}
