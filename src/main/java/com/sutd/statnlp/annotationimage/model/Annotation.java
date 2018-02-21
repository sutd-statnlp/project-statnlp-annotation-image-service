package com.sutd.statnlp.annotationimage.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Annotation.
 */
@Document(collection = "annotation")
public class Annotation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("image_id")
    private String imageId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageId() {
        return imageId;
    }

    public Annotation imageId(String imageId) {
        this.imageId = imageId;
        return this;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Annotation annotation = (Annotation) o;
        if (annotation.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), annotation.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Annotation{" +
                "id=" + getId() +
                ", imageId='" + getImageId() + "'" +
                "}";
    }
}
