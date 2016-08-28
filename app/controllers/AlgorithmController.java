package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import models.Algorithm;
import models.Parameter;
import models.ParameterEnumValue;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.algorithms.*;

import java.util.ArrayList;
import java.util.List;

public class AlgorithmController extends Controller {

    @Inject
    private FormFactory formFactory;

    @Security.Authenticated(Secured.class)
    public Result list() {
        List<Algorithm> algorithms = Algorithm.find.all();

        return ok(list.render(algorithms));
    }

    @Security.Authenticated(Secured.class)
    public Result create() {
//        return ok(create.render(formFactory.form(AlgorithmData.class)));
        return ok(create.render());
    }

    @Security.Authenticated(Secured.class)
    @BodyParser.Of(BodyParser.Json.class)
    public Result save() {

        Form<AlgorithmData> algorithmForm = formFactory.form(AlgorithmData.class).bindFromRequest();

        if (algorithmForm.hasErrors()) {
            return badRequest(algorithmForm.errorsAsJson());
        }

        Algorithm algorithm = new Algorithm();
        algorithm.setName(algorithmForm.get().getName());
        algorithm.setDescription(algorithmForm.get().getDescription());
        algorithm.setEndpoint(algorithmForm.get().getEndpoint());

        List<Parameter> parameters = new ArrayList<>();
        for (ParameterData parameterData : algorithmForm.get().getParameters()) {
            Parameter parameter = new Parameter();
            parameter.setName(parameterData.getName());
            parameter.setType(parameterData.getType());


            List<ParameterEnumValue> parameterEnumValues = new ArrayList<>();
            for (String enumValue : parameterData.getEnumValues()) {
                if(enumValue.equals("")) {
                    continue;
                }

                ParameterEnumValue parameterEnumValue = new ParameterEnumValue();
                parameterEnumValue.setValue(enumValue);
                parameterEnumValues.add(parameterEnumValue);
            }
            parameter.setEnumValues(parameterEnumValues);

            parameters.add(parameter);
        }
        algorithm.setParameters(parameters);

        algorithm.save();

        return ok();
    }

    @Security.Authenticated(Secured.class)
    public Result detail(int algorithmId) {
        Algorithm algorithm = Algorithm.find.byId(algorithmId);

        return ok(detail.render(algorithm));
    }

    public static class AlgorithmData {

        protected String name;
        protected String description;
        protected String endpoint;
        protected List<ParameterData> parameters;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getEndpoint() {
            return endpoint;
        }

        public void setEndpoint(String endpoint) {
            this.endpoint = endpoint;
        }

        public List<ParameterData> getParameters() {
            return parameters;
        }

        public void setParameters(List<ParameterData> parameters) {
            this.parameters = parameters;
        }

        public String validate() {

            if (name == null || name.equals("")) {
                return "Name must not be empty";
            }
            if (description == null || description.equals("")) {
                return "Description must not be empty";
            }
            if (endpoint == null || endpoint.equals("")) {
                return "Endpoint must not be empty";
            }

            for (ParameterData parameterData : this.getParameters()) {
                if (parameterData.validate() != null) {
                    return parameterData.validate();
                }
            }

            return null;
        }
    }

    public static class ParameterData {

        protected String name;
        protected Parameter.Type type;
        protected List<String> enumValues;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Parameter.Type getType() {
            return type;
        }

        public void setType(String type) {
            this.type = Parameter.Type.fromString(type);
        }

        public List<String> getEnumValues() {
            return enumValues;
        }

        public void setEnumValues(List<String> enumValues) {
            this.enumValues = enumValues;
        }

        public String validate() {
            if (name == null || name.equals("")) {
                return "Parameter name must not be empty";
            }
            if (type == null) {
                return "Invalid parameter type";
            }

            return null;
        }
    }
}
