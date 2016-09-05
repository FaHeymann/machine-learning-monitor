package models;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class TestMatrix extends TimestampedModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToMany(cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<ResultSet> resultSets;

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public List<ResultSet> getResultSets() {
        return resultSets;
    }

    public void setResultSets(final List<ResultSet> resultSets) {
        this.resultSets = resultSets;
    }

    @annotations.AllowPublic
    public static Model.Finder<Integer, TestMatrix> find = new Model.Finder<>(TestMatrix.class);
}
