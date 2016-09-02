package models;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

@Entity
public class Parameter extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    private String name;

    @NotNull
    private Type type;

    @OneToMany(cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<ParameterEnumValue> enumValues;

    @ManyToOne
    @NotNull
    @JsonBackReference
    private Algorithm algorithm;

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(final Type type) {
        this.type = type;
    }

    public List<ParameterEnumValue> getEnumValues() {
        return enumValues;
    }

    public void setEnumValues(final List<ParameterEnumValue> enumValues) {
        this.enumValues = enumValues;
    }

    public Algorithm getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(final Algorithm algorithm) {
        this.algorithm = algorithm;
    }

    public enum Type {
        STRING("string"),
        INT("int"),
        ENUM("enum"),
        DOUBLE("double");

        private String type;

        Type(final String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }

        public void setType(final String type) {
            this.type = type;
        }

        public static Type fromString(final String text) {
            return text == null
                ? null
                : Arrays.stream(Type.values())
                    .filter(t -> t.getType().equalsIgnoreCase(text))
                    .findFirst()
                    .orElseGet(null);
        }
    }

    @annotations.AllowPublic
    public static Model.Finder<Integer, Parameter> find = new Model.Finder<>(Parameter.class);
}
