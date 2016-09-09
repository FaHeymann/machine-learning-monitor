package models;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.UpdatedTimestamp;
import com.fasterxml.jackson.annotation.JsonFormat;
import play.data.format.Formats;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import java.util.Date;

@MappedSuperclass
public class TimestampedModel extends Model {
    @Formats.DateTime(pattern = "yyyy-MM-dd HH:mm:ss")
    @CreatedTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(updatable = false)
    private Date createdAt;

    @Formats.DateTime(pattern = "yyyy-MM-dd HH:mm:ss")
    @UpdatedTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Version
    private Date updatedAt;

    public final Date getCreatedAt() {
        return createdAt;
    }

    public final void setCreatedAt(final Date date) {
        createdAt = date;
    }

    public final Date getUpdatedAt() {
        return updatedAt;
    }

    public final void setUpdatedAt(final Date date) {
        updatedAt = date;
    }
}
