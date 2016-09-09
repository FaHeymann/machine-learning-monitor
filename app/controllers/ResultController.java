package controllers;

import com.avaje.ebean.Expr;
import models.Algorithm;
import models.FeatureSet;
import models.ResultSet;
import models.User;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.results.detail;
import views.html.results.results;

import java.util.List;

public class ResultController extends Controller {

    @Security.Authenticated(Secured.class)
    public Result index() {
        User user = User.find.where().eq("email", request().username()).findUnique();
        List<FeatureSet> featureSets = FeatureSet.find.where().eq("user_id", user.getId()).findList();
        List<Algorithm> algorithms = Algorithm.find.where().eq("user_id", user.getId()).findList();

        return ok(results.render(Json.toJson(featureSets).toString(), Json.toJson(algorithms).toString()));
    }

    @Security.Authenticated(Secured.class)
    public Result search(final int featureSetId, final int algorithmId) {
        FeatureSet featureSet = FeatureSet.find.byId(featureSetId);
        Algorithm algorithm = Algorithm.find.byId(algorithmId);
        User user = User.find.where().eq("email", request().username()).findUnique();

        if (featureSet == null
            || algorithm == null
            || !featureSet.getUser().getId().equals(user.getId())
            || !algorithm.getUser().getId().equals(user.getId())) {
            return notFound();
        }

        List<ResultSet> resultSets = ResultSet.find.where().and(
            Expr.eq("feature_set_id", featureSetId),
            Expr.eq("algorithm_id", algorithmId)
        ).findList();
        return ok(Json.toJson(resultSets));
    }

    @Security.Authenticated(Secured.class)
    public Result detail(final int resultSetId) {
        ResultSet resultSet = ResultSet.find.byId(resultSetId);
        return ok(detail.render(resultSet));
    }
}
