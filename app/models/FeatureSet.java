package models;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class FeatureSet extends TimestampedModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    private String name;

    @NotNull
    private String description;

    @OneToMany(cascade = CascadeType.ALL)
    @JsonIgnore
    private List<FeatureLabel> labels;

    @OneToMany(cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Feature> features;

    @OneToMany
    @JsonManagedReference
    private List<ResultSet> resultSets;

    @ManyToOne
    private User user;

    public FeatureSet() {
        this.labels = new ArrayList<>();
        this.features = new ArrayList<>();
        this.resultSets = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<FeatureLabel> getLabels() {
        return this.labels;
    }

    public List<String> getLabelStrings() {
        return this.labels.stream()
            .map(FeatureLabel::getValue)
            .collect(Collectors.toList());
    }

    public void setLabels(List<FeatureLabel> labels) {
        this.labels = labels;
    }

    public void addLabel(FeatureLabel label) {
        this.labels.add(label);
    }

    public List<Feature> getFeatures() {
        return features;
    }

    public void setFeatures(List<Feature> features) {
        this.features = features;
    }

    public void addFeature(Feature feature) {
        this.features.add(feature);
    }

    public List<ResultSet> getResultSets() {
        return resultSets;
    }

    public void setResultSets(List<ResultSet> resultSets) {
        this.resultSets = resultSets;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public static Model.Finder<Integer, FeatureSet> find = new Model.Finder<>(FeatureSet.class);
}
