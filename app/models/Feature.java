package models;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

@Entity
public class Feature extends TimestampedModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    private int columnAmount;

    @Transient
    private List<String> data;

    @NotNull
    private String serializedData;

    @NotNull
    private double result;

    @ManyToOne
    @NotNull
    @JsonBackReference
    private FeatureSet featureSet;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getColumnAmount() {
        return columnAmount;
    }

    public void setColumnAmount(int columnAmount) {
        this.columnAmount = columnAmount;
    }

    public List<String> getData() {
        return Arrays.asList(serializedData.split(";;;"));
    }

    public void setData(List<String> data) {
        this.data = data;
        this.serializedData = String.join(";;;", data);
    }

    public String getSerializedData() {
        return serializedData;
    }

    public void setSerializedData(String serializedData) {
        this.serializedData = serializedData;
        this.data = Arrays.asList(serializedData.split(";;;"));
    }

    public double getResult() {
        return result;
    }

    public void setResult(double result) {
        this.result = result;
    }

    public FeatureSet getFeatureSet() {
        return featureSet;
    }

    public void setFeatureSet(FeatureSet featureSet) {
        this.featureSet = featureSet;
    }

    public static Model.Finder<Integer, Feature> find = new Model.Finder<>(Feature.class);
}
