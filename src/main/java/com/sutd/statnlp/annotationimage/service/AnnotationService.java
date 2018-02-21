package com.sutd.statnlp.annotationimage.service;

import com.sutd.statnlp.annotationimage.model.Annotation;

import java.util.List;

/**
 * Service Interface for managing Annotation.
 */
public interface AnnotationService {

    /**
     * Save a annotation.
     *
     * @param annotation the entity to save
     * @return the persisted entity
     */
    Annotation save(Annotation annotation);

    /**
     * Get all the annotations.
     *
     * @return the list of entities
     */
    List<Annotation> findAll();

    /**
     * Get the "id" annotation.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Annotation findOne(String id);

    /**
     * Delete the "id" annotation.
     *
     * @param id the id of the entity
     */
    void delete(String id);
}
