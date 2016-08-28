package models;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
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

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public List<ParameterEnumValue> getEnumValues() {
        return enumValues;
    }

    public void setEnumValues(List<ParameterEnumValue> enumValues) {
        this.enumValues = enumValues;
    }

    public Algorithm getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(Algorithm algorithm) {
        this.algorithm = algorithm;
    }

    public enum Type {
        STRING("string"),
        INT("int"),
        ENUM("enum"),
        DOUBLE("double");

        private String type;

        Type(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public static Type fromString(String text) {
            if (text != null) {
                for (Type type : Type.values()) {
                    if (text.equalsIgnoreCase(type.type)) {
                        return type;
                    }
                }
            }
            return null;
        }
    }

    public static Model.Finder<Integer, Parameter> find = new Model.Finder<>(Parameter.class);
}
