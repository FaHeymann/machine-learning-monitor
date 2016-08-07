package models;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
public class ResultSet extends TimestampedModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @NotNull
    @JsonBackReference
    private FeatureSet featureSet;

    @ManyToOne
    @NotNull
    @JsonBackReference
    private Algorithm algorithm;

    @OneToMany(cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Result> results;

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

    public Algorithm getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(Algorithm algorithm) {
        this.algorithm = algorithm;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    public double getMaxPositiveDeviation() {
        return this.getResults().stream()
            .mapToDouble(r -> r.getActual() - r.getExpected())
            .max().getAsDouble();
    }

    public double getMaxNegativeDeviation() {
        return this.getResults().stream()
            .mapToDouble(r -> r.getExpected() - r.getActual())
            .max().getAsDouble();
    }

    public double getMaxDeviation() {
        return Math.max(this.getMaxNegativeDeviation(), this.getMaxPositiveDeviation());
    }

    public double get25Quantile() {
        return this.getQuantile(0.25);
    }

    public double get50Quantile() {
        return this.getQuantile(0.50);
    }

    public double get75Quantile() {
        return this.getQuantile(0.75);
    }

    public double get90Quantile() {
        return this.getQuantile(0.90);
    }

    public double get99Quantile() {
        return this.getQuantile(0.99);
    }

    private double getQuantile(double p) {
        return this.getResults().stream()
            .mapToDouble(r -> Math.abs(r.getActual() - r.getExpected()))
            .sorted()
            .limit(Math.round(Math.ceil(p * this.getResults().size())))
            .reduce((a, b) -> b).getAsDouble();
    }



    public static Model.Finder<Integer, ResultSet> find = new Model.Finder<>(ResultSet.class);
}
