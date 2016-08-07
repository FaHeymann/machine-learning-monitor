package models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.avaje.ebean.Model;
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

    public User(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.setPassword(password);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        try {
            this.password = PasswordHelper.createPassword(password);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public List<FeatureSet> getFeatureSets() {
        return featureSets;
    }

    public void setFeatureSets(List<FeatureSet> featureSets) {
        this.featureSets = featureSets;
    }

    public static User authenticate(String email, String password) {
        User user = find.where().eq("email", email).findUnique();
        return user != null && PasswordHelper.checkPassword(password, user.password) ? user : null;
    }

    public static Finder<Integer, User> find = new Finder<>(User.class);
}