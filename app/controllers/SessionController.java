package controllers;

import com.google.inject.Inject;
import models.User;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.session.login;

public class SessionController extends Controller {

    @Inject
    private FormFactory formFactory;

    public Result login() {
        return ok(login.render(formFactory.form(Login.class)));
    }

    public Result authenticate() {
        Form<Login> loginForm = formFactory.form(Login.class).bindFromRequest();
        if (loginForm.hasErrors()) {
            return badRequest(login.render(loginForm));
        } else {
            session().clear();
            session("email", loginForm.get().email);
            return redirect(routes.FeatureController.list());
        }
    }

    public Result logout() {
        session().clear();
        return redirect(routes.SessionController.login());
    }

    public static class Login {

        private String email;
        private String password;

        public String getEmail() {
            return email;
        }

        public void setEmail(final String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(final String password) {
            this.password = password;
        }

        public String validate() {
            if (User.authenticate(email, password) == null) {
                return "Invalid credentials";
            }
            return null;
        }
    }

}
