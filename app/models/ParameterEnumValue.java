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
public class ParameterEnumValue extends TimestampedModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    private String value;

    @ManyToOne
    @NotNull
    @JsonBackReference
    private Parameter parameter;

    @OneToMany(cascade = CascadeType.ALL)
    @JsonIgnore
    private List<ParameterTestValue> parameterTestValues;

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

    public List<ParameterTestValue> getParameterTestValues() {
        return parameterTestValues;
    }

    public void setParameterTestValues(final List<ParameterTestValue> parameterTestValues) {
        this.parameterTestValues = parameterTestValues;
    }

    @annotations.AllowPublic
    public static Model.Finder<Integer, ParameterEnumValue> find = new Model.Finder<>(ParameterEnumValue.class);
}
