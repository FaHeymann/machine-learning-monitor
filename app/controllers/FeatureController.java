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
    public final Result list() {
        List<FeatureSet> featureSets = FeatureSet.find.all();

        return ok(list.render(featureSets));
    }

    @Security.Authenticated(Secured.class)
    public final Result create() {
        return ok(create.render(formFactory.form(FeatureSetData.class)));
    }

    @Security.Authenticated(Secured.class)
    public final Result save() {

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
    public final Result detail(final int featureSetId) {
        FeatureSet featureSet = FeatureSet.find.byId(featureSetId);

        return ok(detail.render(featureSet));
    }

    public static class FeatureSetData {

        private String name;
        private String description;

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

        public final String validate() {

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
