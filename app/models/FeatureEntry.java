package models;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity
public class FeatureEntry extends TimestampedModel {

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

    public FeatureEntry(final String value, final Feature feature, final FeatureLabel featureLabel) {
        this(value);
        this.setFeature(feature);
        this.setFeatureLabel(featureLabel);
    }

    public FeatureEntry(final String value) {
        this.setValue(value);
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public Feature getFeature() {
        return feature;
    }

    public void setFeature(final Feature feature) {
        this.feature = feature;
    }

    public FeatureLabel getFeatureLabel() {
        return featureLabel;
    }

    public void setFeatureLabel(final FeatureLabel featureLabel) {
        this.featureLabel = featureLabel;
    }

    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }

    @annotations.AllowPublic
    public static Model.Finder<Integer, FeatureEntry> find = new Model.Finder<>(FeatureEntry.class);
}
