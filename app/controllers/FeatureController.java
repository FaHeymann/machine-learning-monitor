package controllers;

import com.google.inject.Inject;

import models.FeatureSet;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;
import services.DataFileParser;
import views.html.features.create;
import views.html.features.detail;
import views.html.features.list;

import java.io.File;
import java.util.List;

public class FeatureController extends Controller {

    @Inject
    private FormFactory formFactory;

    @Inject
    private DataFileParser parser;

    @Security.Authenticated(Secured.class)
    public Result list() {
        List<FeatureSet> featureSets = FeatureSet.find.all();

        return ok(list.render(featureSets));
    }

    @Security.Authenticated(Secured.class)
    public Result create() {
        return ok(create.render(formFactory.form(FeatureSetData.class)));
    }

    @Security.Authenticated(Secured.class)
    public Result save() {

        Form<FeatureSetData> featureSetForm = formFactory.form(FeatureSetData.class).bindFromRequest();
        if (featureSetForm.hasErrors()) {
            return badRequest(create.render(featureSetForm));
        }

        Http.MultipartFormData<File> body = request().body().asMultipartFormData();
        Http.MultipartFormData.FilePart<File> dataFile = body.getFile("dataFile");

        if (dataFile == null) {
            featureSetForm.reject("No data file specified");
            return badRequest(create.render(featureSetForm));
        }

        this.parser.parse(dataFile);

        if (this.parser.hasError()) {
            featureSetForm.reject(this.parser.getError());
            return badRequest(create.render(featureSetForm));
        }

        FeatureSet featureSet = this.parser.getFeatureSet();
        featureSet.setName(featureSetForm.get().getName());
        featureSet.setDescription(featureSetForm.get().getDescription());

        featureSet.save();

        return redirect(routes.FeatureController.list());

    }

    @Security.Authenticated(Secured.class)
    public Result detail(final int featureSetId) {
        FeatureSet featureSet = FeatureSet.find.byId(featureSetId);

        return ok(detail.render(featureSet));
    }

    public static class FeatureSetData {

        private String name;
        private String description;

        public String getName() {
            return name;
        }

        public void setName(final String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(final String description) {
            this.description = description;
        }

        public String validate() {

            if (name == null || name.equals("")) {
                return "Name must not be empty";
            }
            if (description == null || description.equals("")) {
                return "Description must not be empty";
            }
            return null;
        }
    }
}
