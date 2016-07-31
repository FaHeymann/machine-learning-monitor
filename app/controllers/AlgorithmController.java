package controllers;

import com.google.inject.Inject;
import models.Algorithm;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.algorithms.*;

import java.util.List;

public class AlgorithmController extends Controller {

    @Inject
    FormFactory formFactory;

    @Security.Authenticated(Secured.class)
    public Result list() {
        List<Algorithm> algorithms = Algorithm.find.all();

        return ok(list.render(algorithms));
    }

    @Security.Authenticated(Secured.class)
    public Result create() {
        return ok(create.render(formFactory.form(AlgorithmData.class)));
    }

    @Security.Authenticated(Secured.class)
    public Result save() {

        Form<AlgorithmData> algorithmForm = formFactory.form(AlgorithmData.class).bindFromRequest();
        if (algorithmForm.hasErrors()) {
            return badRequest(create.render(algorithmForm));
        }

        Algorithm algorithm = new Algorithm();
        algorithm.setName(algorithmForm.get().getName());
        algorithm.setDescription(algorithmForm.get().getDescription());
        algorithm.setEndpoint(algorithmForm.get().getEndpoint());

        algorithm.save();

        return redirect(routes.AlgorithmController.list());

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

        public String validate() {

            if(name == null || name.equals("")) {
                return "Name must not be empty";
            }
            if(description == null || description.equals("")) {
                return "Description must not be empty";
            }
            return null;
        }
    }
}
