package controllers;

import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.Security;

class Secured extends Security.Authenticator {

    @Override
    public String getUsername(final Context ctx) {
        return ctx.session().get("email");
    }

    @Override
    public Result onUnauthorized(final Context ctx) {
        return redirect(routes.SessionController.login());
    }
}
