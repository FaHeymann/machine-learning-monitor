package models;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
public class FeatureLabel extends Model {

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

    public FeatureLabel(String value, FeatureSet featureSet) {
        this(value);
        this.setFeatureSet(featureSet);
    }

    public FeatureLabel(String value) {
        this.setValue(value);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public FeatureSet getFeatureSet() {
        return featureSet;
    }

    public void setFeatureSet(FeatureSet featureSet) {
        this.featureSet = featureSet;
    }

    public List<FeatureEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<FeatureEntry> entries) {
        this.entries = entries;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static Model.Finder<Integer, FeatureLabel> find = new Model.Finder<>(FeatureLabel.class);
}
