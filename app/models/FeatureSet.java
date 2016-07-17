package models;

import com.avaje.ebean.Model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

@Entity
public class FeatureSet extends Model {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;

    @NotNull
    private String name;

    @NotNull
    private String description;

    @NotNull
    private int columnAmount;

    @Transient
    private List<String> labels;

    @NotNull
    private String serializedLabels;

    @OneToMany(cascade=CascadeType.ALL)
    private List<Feature> features;

    @OneToMany
    private List<ResultSet> resultSets;

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

    public int getColumnAmount() {
        return columnAmount;
    }

    public void setColumnAmount(int columnAmount) {
        this.columnAmount = columnAmount;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
        this.serializedLabels = String.join(";;;", labels);
    }

    public String getSerializedLabels() {
        return serializedLabels;
    }

    public void setSerializedLabels(String serializedLabels) {
        this.serializedLabels = serializedLabels;
        this.labels = Arrays.asList(serializedLabels.split(";;;"));
    }

    public List<Feature> getFeatures() {
        return features;
    }

    public void setFeatures(List<Feature> features) {
        this.features = features;
    }

    public List<ResultSet> getResultSets() {
        return resultSets;
    }

    public void setResultSets(List<ResultSet> resultSets) {
        this.resultSets = resultSets;
    }

    public static Model.Finder<Integer, FeatureSet> find = new Model.Finder<>(FeatureSet.class);
}
