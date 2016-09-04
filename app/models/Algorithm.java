package models;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Algorithm extends TimestampedModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    private String name;

    @NotNull
    private String description;

    @NotNull
    private String endpoint;

    @OneToMany
    @JsonIgnore
    private List<ResultSet> resultSets;

    @OneToMany(cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Parameter> parameters;

    public Algorithm() {
        this.parameters = new ArrayList<>();
        this.resultSets = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

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

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(final String endpoint) {
        this.endpoint = endpoint;
    }

    public List<ResultSet> getResultSets() {
        return resultSets;
    }

    public void setResultSets(final List<ResultSet> resultSets) {
        this.resultSets = resultSets;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(final List<Parameter> parameters) {
        this.parameters = parameters;
    }

    @annotations.AllowPublic
    public static Model.Finder<Integer, Algorithm> find = new Model.Finder<>(Algorithm.class);
}
