package com.sutd.statnlp.annotationimage.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Region.
 */
@Document(collection = "region")
public class Region implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("height")
    private Integer height;

    @NotNull
    @Field("image_id")
    private String imageId;

    @Field("width")
    private Integer width;

    @Field("phrase")
    private String phrase;

    @Field("x")
    private Integer x;

    @Field("y")
    private Integer y;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getHeight() {
        return height;
    }

    public Region height(Integer height) {
        this.height = height;
        return this;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public String getImageId() {
        return imageId;
    }

    public Region imageId(String imageId) {
        this.imageId = imageId;
        return this;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public Integer getWidth() {
        return width;
    }

    public Region width(Integer width) {
        this.width = width;
        return this;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public String getPhrase() {
        return phrase;
    }

    public Region phrase(String phrase) {
        this.phrase = phrase;
        return this;
    }

    public void setPhrase(String phrase) {
        this.phrase = phrase;
    }

    public Integer getX() {
        return x;
    }

    public Region x(Integer x) {
        this.x = x;
        return this;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public Region y(Integer y) {
        this.y = y;
        return this;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Region region = (Region) o;
        if (region.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), region.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Region{" +
                "id=" + getId() +
                ", height=" + getHeight() +
                ", imageId='" + getImageId() + "'" +
                ", width=" + getWidth() +
                ", phrase='" + getPhrase() + "'" +
                ", x=" + getX() +
                ", y=" + getY() +
                "}";
    }
}
