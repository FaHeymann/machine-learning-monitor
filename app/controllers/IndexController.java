package controllers;

import play.mvc.Controller;
import play.mvc.Result;

public class IndexController extends Controller {
    public Result index() {
        return redirect(routes.FeatureController.list());
    }
}
