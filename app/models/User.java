package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import helpers.PasswordHelper;

import java.security.NoSuchAlgorithmException;
import java.util.List;

@Entity
public class User extends TimestampedModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    @NotNull
    private String email;

    @NotNull
    private String name;

    @NotNull
    private String password;

    @OneToMany
    private List<FeatureSet> featureSets;

    public User(final String email, final String name, final String password) {
        this.email = email;
        this.name = name;
        this.setPassword(password);
    }

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(final String password) {
        try {
            this.password = PasswordHelper.createPassword(password);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public List<FeatureSet> getFeatureSets() {
        return featureSets;
    }

    public void setFeatureSets(final List<FeatureSet> featureSets) {
        this.featureSets = featureSets;
    }

    public static User authenticate(final String email, final String password) {
        User user = find.where().eq("email", email).findUnique();
        return user != null && PasswordHelper.checkPassword(password, user.password)
            ? user
            : null;
    }

    @annotations.AllowPublic
    public static Finder<Integer, User> find = new Finder<>(User.class);
}
