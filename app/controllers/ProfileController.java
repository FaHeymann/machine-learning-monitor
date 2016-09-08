package controllers;

import com.avaje.ebean.Ebean;
import com.google.inject.Inject;
import helpers.PasswordHelper;
import models.User;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

import views.html.profile.index;

public class ProfileController extends Controller {

    @Inject
    private FormFactory formFactory;

    @Security.Authenticated(Secured.class)
    public Result index() {
        User user = User.find.where().eq("email", request().username()).findUnique();

        Form<EmailData> emailForm = formFactory.form(EmailData.class);
        emailForm.data().put("email", user.getEmail());

        Form<PasswordData> passwordForm = formFactory.form(PasswordData.class);

        return ok(index.render(emailForm, passwordForm, ""));
    }

    @Security.Authenticated(Secured.class)
    public Result changeEmail() {
        User user = User.find.where().eq("email", request().username()).findUnique();

        Form<EmailData> emailForm = formFactory.form(EmailData.class).bindFromRequest();

        if (emailForm.hasErrors()) {
            emailForm.data().put("email", user.getEmail());
            return badRequest(index.render(emailForm, formFactory.form(PasswordData.class), ""));
        } else {
            user.setEmail(emailForm.get().getEmail());
            Ebean.save(user);

            session().clear();
            session("email", user.getEmail());

            return ok(index.render(emailForm, formFactory.form(PasswordData.class), "Successfully changed Email"));
        }
    }

    @Security.Authenticated(Secured.class)
    public Result changePassword() {
        User user = User.find.where().eq("email", request().username()).findUnique();

        Form<PasswordData> passwordForm = formFactory.form(PasswordData.class).bindFromRequest();

        Form<EmailData> emailForm = formFactory.form(EmailData.class);
        emailForm.data().put("email", user.getEmail());

        if (passwordForm.hasErrors()) {
            return badRequest(index.render(emailForm, passwordForm, ""));
        } else {
            user.setPassword(passwordForm.get().getNewPassword1());
            Ebean.save(user);

            return ok(index.render(emailForm, formFactory.form(PasswordData.class), "Successfully changed Password"));
        }
    }

    public static class EmailData {
        private String email;

        public String getEmail() {
            return email;
        }

        public void setEmail(final String email) {
            this.email = email;
        }

        public String validate() {
            if ("".equals(email)) {
                return "email.empty";
            }
            if (null != User.find.where().eq("email", email).findUnique()) {
                return "email.used";
            }
            return null;
        }
    }

    public static class PasswordData {
        private String oldPassword;
        private String newPassword1;
        private String newPassword2;

        public String getOldPassword() {
            return oldPassword;
        }

        public void setOldPassword(final String oldPassword) {
            this.oldPassword = oldPassword;
        }

        public String getNewPassword1() {
            return newPassword1;
        }

        public void setNewPassword1(final String newPassword1) {
            this.newPassword1 = newPassword1;
        }

        public String getNewPassword2() {
            return newPassword2;
        }

        public void setNewPassword2(final String newPassword2) {
            this.newPassword2 = newPassword2;
        }

        public String validate() {

            User user = User.find.where().eq("email", request().username()).findUnique();

            if (!PasswordHelper.checkPassword(oldPassword, user.getPassword())) {
                return "The old password is not valid";
            }
            if ("".equals(newPassword1)) {
                return "The new password must not be empty";
            }
            if (!newPassword1.equals(newPassword2)) {
                return "The new passwords do not match";
            }
            if (oldPassword.equals(newPassword1)) {
                return "The new password must be different from the old one";
            }
            return null;
        }
    }
}
