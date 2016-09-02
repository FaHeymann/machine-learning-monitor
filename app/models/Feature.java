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
import java.util.stream.Collectors;

@Entity
public class Feature extends TimestampedModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToMany(cascade = CascadeType.ALL)
    @JsonIgnore
    private List<FeatureEntry> entries;

    @NotNull
    private double result;

    @ManyToOne
    @NotNull
    @JsonBackReference
    private FeatureSet featureSet;

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public List<FeatureEntry> getEntries() {
        return entries;
    }

    public List<String> getEntryStrings() {
        return entries.stream()
            .map(FeatureEntry::getValue)
            .collect(Collectors.toList());
    }

    public void setEntries(final List<FeatureEntry> entries) {
        this.entries = entries;
    }

    public double getResult() {
        return result;
    }

    public void setResult(final double result) {
        this.result = result;
    }

    public FeatureSet getFeatureSet() {
        return featureSet;
    }

    public void setFeatureSet(final FeatureSet featureSet) {
        this.featureSet = featureSet;
    }

    @annotations.AllowPublic
    public static Model.Find<Integer, Feature> find = new Model.Finder<>(Feature.class);
}
