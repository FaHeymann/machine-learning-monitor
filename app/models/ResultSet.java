package models;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

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

    @ManyToOne
    @JsonBackReference
    private TestMatrix testMatrix;

    @OneToMany(cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<ParameterTestValue> parameterTestValues;

    @ManyToMany
    @JoinTable(name = "test_ignored_labels")
    @JsonIgnore
    private List<FeatureLabel> ignoredLabels;

    @OneToMany(cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Result> results;

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

    public Algorithm getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(final Algorithm algorithm) {
        this.algorithm = algorithm;
    }

    public TestMatrix getTestMatrix() {
        return testMatrix;
    }

    public void setTestMatrix(final TestMatrix testMatrix) {
        this.testMatrix = testMatrix;
    }

    public List<ParameterTestValue> getParameterTestValues() {
        return parameterTestValues;
    }

    public void setParameterTestValues(final List<ParameterTestValue> parameterTestValues) {
        this.parameterTestValues = parameterTestValues;
    }

    public List<FeatureLabel> getIgnoredLabels() {
        return ignoredLabels;
    }

    public List<String> getIgnoredLabelStrings() {
        return this.getIgnoredLabels().stream()
            .map(FeatureLabel::getValue)
            .collect(Collectors.toList());
    }

    public void setIgnoredLabels(final List<FeatureLabel> ignoredLabels) {
        this.ignoredLabels = ignoredLabels;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(final List<Result> results) {
        this.results = results;
    }

    public double getMaxPositiveDeviation() {
        return this.getResults().stream()
            .mapToDouble(r -> r.getActual() - r.getExpected())
            .max().orElse(0);
    }

    public double getMaxNegativeDeviation() {
        return this.getResults().stream()
            .mapToDouble(r -> r.getExpected() - r.getActual())
            .max().orElse(0);
    }

    public double getMaxDeviation() {
        return Math.max(this.getMaxNegativeDeviation(), this.getMaxPositiveDeviation());
    }

    public double get25Quantile() {
        final double twentyFivePercent = 0.25;
        return this.getQuantile(twentyFivePercent);
    }

    public double get50Quantile() {
        final double fiftyPercent = 0.50;
        return this.getQuantile(fiftyPercent);
    }

    public double get75Quantile() {
        final double seventyFivePercent = 0.75;
        return this.getQuantile(seventyFivePercent);
    }

    public double get90Quantile() {
        final double ninetyPercent = 0.25;
        return this.getQuantile(ninetyPercent);
    }

    public double get99Quantile() {
        final double ninetyNinePercent = 0.25;
        return this.getQuantile(ninetyNinePercent);
    }

    private double getQuantile(final double p) {
        return this.getResults().stream()
            .mapToDouble(r -> Math.abs(r.getActual() - r.getExpected()))
            .sorted()
            .limit(Math.round(Math.ceil(p * this.getResults().size())))
            .reduce((a, b) -> b).orElse(0);
    }

    @annotations.AllowPublic
    public static Model.Finder<Integer, ResultSet> find = new Model.Finder<>(ResultSet.class);
}
