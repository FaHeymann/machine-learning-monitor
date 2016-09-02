package controllers;

import com.google.inject.Inject;
import models.Algorithm;
import models.Parameter;
import models.ParameterEnumValue;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.algorithms.create;
import views.html.algorithms.detail;
import views.html.algorithms.list;

import java.util.List;
import java.util.stream.Collectors;

public class AlgorithmController extends Controller {

    @Inject
    private FormFactory formFactory;

    @Security.Authenticated(Secured.class)
    public final Result list() {
        List<Algorithm> algorithms = Algorithm.find.all();

        return ok(list.render(algorithms));
    }

    @Security.Authenticated(Secured.class)
    public final Result create() {
        return ok(create.render());
    }

    @Security.Authenticated(Secured.class)
    @BodyParser.Of(BodyParser.Json.class)
    public final Result save() {

        Form<AlgorithmData> algorithmForm = formFactory.form(AlgorithmData.class).bindFromRequest();

        if (algorithmForm.hasErrors()) {
            return badRequest(algorithmForm.errorsAsJson());
        }

        Algorithm algorithm = new Algorithm();
        algorithm.setName(algorithmForm.get().getName());
        algorithm.setDescription(algorithmForm.get().getDescription());
        algorithm.setEndpoint(algorithmForm.get().getEndpoint());

        if (algorithmForm.get().getParameters() != null) {
            algorithm.setParameters(
                algorithmForm.get().getParameters().stream()
                .map(p -> {
                    Parameter parameter = new Parameter();
                    parameter.setName(p.getName());
                    parameter.setType(p.getType());
                    parameter.setEnumValues(p.getEnumValues().stream()
                        .filter(e -> !e.equals("") && p.getType().equals(Parameter.Type.ENUM))
                        .map(e -> {
                            ParameterEnumValue parameterEnumValue = new ParameterEnumValue();
                            parameterEnumValue.setValue(e);
                            return parameterEnumValue;
                        }).collect(Collectors.toList())
                    );
                    return parameter;
                }).collect(Collectors.toList())
            );
        }

        algorithm.save();

        return ok();
    }

    @Security.Authenticated(Secured.class)
    public final Result detail(final int algorithmId) {
        Algorithm algorithm = Algorithm.find.byId(algorithmId);

        return ok(detail.render(algorithm));
    }

    public static class AlgorithmData {

        private String name;
        private String description;
        private String endpoint;
        private List<ParameterData> parameters;

        public final String getName() {
            return name;
        }

        public final void setName(final String name) {
            this.name = name;
        }

        public final String getDescription() {
            return description;
        }

        public final void setDescription(final String description) {
            this.description = description;
        }

        public final String getEndpoint() {
            return endpoint;
        }

        public final void setEndpoint(final String endpoint) {
            this.endpoint = endpoint;
        }

        public final List<ParameterData> getParameters() {
            return parameters;
        }

        public final void setParameters(final List<ParameterData> parameters) {
            this.parameters = parameters;
        }

        public final String validate() {

            if (name == null || name.equals("")) {
                return "Name must not be empty";
            }
            if (description == null || description.equals("")) {
                return "Description must not be empty";
            }
            if (endpoint == null || endpoint.equals("")) {
                return "Endpoint must not be empty";
            }

            if (this.getParameters() != null) {
                for (ParameterData parameterData : this.getParameters()) {
                    if (parameterData.validate() != null) {
                        return parameterData.validate();
                    }
                }
            }

            return null;
        }
    }

    public static class ParameterData {

        private String name;
        private Parameter.Type type;
        private List<String> enumValues;

        public final String getName() {
            return name;
        }

        public final void setName(final String name) {
            this.name = name;
        }

        public final Parameter.Type getType() {
            return type;
        }

        public final void setType(final String type) {
            this.type = Parameter.Type.fromString(type);
        }

        public final List<String> getEnumValues() {
            return enumValues;
        }

        public final void setEnumValues(final List<String> enumValues) {
            this.enumValues = enumValues;
        }

        public final String validate() {
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
