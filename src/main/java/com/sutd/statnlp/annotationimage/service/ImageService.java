package com.sutd.statnlp.annotationimage.service;

import com.sutd.statnlp.annotationimage.model.Image;

import java.util.List;

/**
 * Service Interface for managing Image.
 */
public interface ImageService {

    /**
     * Save a image.
     *
     * @param image the entity to save
     * @return the persisted entity
     */
    Image save(Image image);

    /**
     * Get all the images.
     *
     * @return the list of entities
     */
    List<Image> findAll();

    /**
     * Get the "id" image.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Image findOne(String id);

    /**
     * Delete the "id" image.
     *
     * @param id the id of the entity
     */
    void delete(String id);
}
