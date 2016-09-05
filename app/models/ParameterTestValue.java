package models;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity
public class ParameterTestValue extends TimestampedModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @NotNull
    @JsonBackReference
    private ResultSet resultSet;

    @ManyToOne
    @NotNull
    private Parameter parameter;

    @Column
    private String stringValue;

    @Column
    private int intValue;

    @Column
    private double doubleValue;

    @ManyToOne
    @JsonBackReference
    private ParameterEnumValue enumValue;

    public ParameterTestValue(final Parameter parameter, final String value) {
        this.setParameter(parameter);
        this.setValueFromString(value);
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public ResultSet getResultSet() {
        return resultSet;
    }

    public void setResultSet(final ResultSet resultSet) {
        this.resultSet = resultSet;
    }

    public Parameter getParameter() {
        return parameter;
    }

    public void setParameter(final Parameter parameter) {
        this.parameter = parameter;
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(final String stringValue) {
        this.stringValue = stringValue;
    }

    public int getIntValue() {
        return intValue;
    }

    public void setIntValue(final int intValue) {
        this.intValue = intValue;
    }

    public double getDoubleValue() {
        return doubleValue;
    }

    public void setDoubleValue(final double doubleValue) {
        this.doubleValue = doubleValue;
    }

    public ParameterEnumValue getEnumValue() {
        return enumValue;
    }

    public void setEnumValue(final ParameterEnumValue enumValue) {
        this.enumValue = enumValue;
    }

    public void setValueFromString(final String value) throws AssertionError {
        if (this.getParameter() == null || this.getParameter().getType() == null) {
            throw new AssertionError("The value cannot be interpolated without setting a proper Parameter");
        }

        switch (this.getParameter().getType()) {
            case STRING:
                this.setStringValue(value);
                break;
            case INT:
                this.setIntValue(Integer.parseInt(value));
                break;
            case DOUBLE:
                this.setDoubleValue(Double.parseDouble(value));
                break;
            case ENUM:
                this.setEnumValue(
                    this.getParameter().getEnumValues().stream()
                        .filter(e -> e.getValue().equals(value))
                        .findFirst().orElseThrow(AssertionError::new)
                );
                break;
            default:
                break;
        }
    }

    public String getValueAsString() throws AssertionError {
        if (this.getParameter() == null || this.getParameter().getType() == null) {
            throw new AssertionError("The value cannot be interpolated without setting a proper Parameter");
        }

        switch (this.getParameter().getType()) {
            case STRING:
                return this.getStringValue();
            case INT:
                return "" + this.getIntValue();
            case DOUBLE:
                return "" + this.getDoubleValue();
            case ENUM:
                return this.getEnumValue().getValue();
            default:
                return "";
        }
    }

    @annotations.AllowPublic
    public static Model.Finder<Integer, ParameterTestValue> find = new Model.Finder<>(ParameterTestValue.class);
}
