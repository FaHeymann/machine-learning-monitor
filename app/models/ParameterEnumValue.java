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
public class ParameterEnumValue extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    private String value;

    @ManyToOne
    @NotNull
    @JsonBackReference
    private Parameter parameter;

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }

    public Parameter getParameter() {
        return parameter;
    }

    public void setParameter(final Parameter parameter) {
        this.parameter = parameter;
    }

    @annotations.AllowPublic
    public static Model.Finder<Integer, ParameterEnumValue> find = new Model.Finder<>(ParameterEnumValue.class);
}
