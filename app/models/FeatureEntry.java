package models;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class FeatureEntry extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @NotNull
    @JsonBackReference
    private Feature feature;

    @ManyToOne
    @NotNull
    @JsonBackReference
    private FeatureLabel featureLabel;

    @NotNull
    private String value;

    public FeatureEntry(String value, Feature feature, FeatureLabel featureLabel) {
        this(value);
        this.setFeature(feature);
        this.setFeatureLabel(featureLabel);
    }

    public FeatureEntry(String value) {
        this.setValue(value);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Feature getFeature() {
        return feature;
    }

    public void setFeature(Feature feature) {
        this.feature = feature;
    }

    public FeatureLabel getFeatureLabel() {
        return featureLabel;
    }

    public void setFeatureLabel(FeatureLabel featureLabel) {
        this.featureLabel = featureLabel;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static Model.Finder<Integer, FeatureEntry> find = new Model.Finder<>(FeatureEntry.class);
}
