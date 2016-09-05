package models;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
public class FeatureLabel extends TimestampedModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @NotNull
    @JsonBackReference
    private FeatureSet featureSet;

    @OneToMany(cascade = CascadeType.ALL)
    @JsonIgnore
    private List<FeatureEntry> entries;

    @NotNull
    private String value;

    public FeatureLabel(final String value, final FeatureSet featureSet) {
        this(value);
        this.setFeatureSet(featureSet);
    }

    public FeatureLabel(final String value) {
        this.setValue(value);
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public FeatureSet getFeatureSet() {
        return featureSet;
    }

    public void setFeatureSet(final FeatureSet featureSet) {
        this.featureSet = featureSet;
    }

    public List<FeatureEntry> getEntries() {
        return entries;
    }

    public void setEntries(final List<FeatureEntry> entries) {
        this.entries = entries;
    }

    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }

    @annotations.AllowPublic
    public static Model.Finder<Integer, FeatureLabel> find = new Model.Finder<>(FeatureLabel.class);
}
