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
public class Result extends TimestampedModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    private double expected;

    @NotNull
    private double actual;

    @ManyToOne
    @NotNull
    @JsonBackReference
    private ResultSet resultSet;

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public double getExpected() {
        return expected;
    }

    public void setExpected(final double expected) {
        this.expected = expected;
    }

    public double getActual() {
        return actual;
    }

    public void setActual(final double actual) {
        this.actual = actual;
    }

    public ResultSet getResultSet() {
        return resultSet;
    }

    public void setResultSet(final ResultSet resultSet) {
        this.resultSet = resultSet;
    }

    @annotations.AllowPublic
    public static Model.Finder<Integer, Result> find = new Model.Finder<>(Result.class);
}

