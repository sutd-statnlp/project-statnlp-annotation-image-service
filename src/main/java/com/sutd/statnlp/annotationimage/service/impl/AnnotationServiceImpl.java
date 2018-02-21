package com.sutd.statnlp.annotationimage.service.impl;

import com.sutd.statnlp.annotationimage.model.Annotation;
import com.sutd.statnlp.annotationimage.repository.AnnotationRepository;
import com.sutd.statnlp.annotationimage.service.AnnotationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service Implementation for managing Annotation.
 */
@Service
public class AnnotationServiceImpl implements AnnotationService {

    private final Logger log = LoggerFactory.getLogger(AnnotationServiceImpl.class);

    private final AnnotationRepository annotationRepository;

    public AnnotationServiceImpl(AnnotationRepository annotationRepository) {
        this.annotationRepository = annotationRepository;
    }

    /**
     * Save a annotation.
     *
     * @param annotation the entity to save
     * @return the persisted entity
     */
    @Override
    public Annotation save(Annotation annotation) {
        log.debug("Request to save Annotation : {}", annotation);
        return annotationRepository.save(annotation);
    }

    /**
     * Get all the annotations.
     *
     * @return the list of entities
     */
    @Override
    public List<Annotation> findAll() {
        log.debug("Request to get all Annotations");
        return annotationRepository.findAll();
    }

    /**
     * Get one annotation by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    public Annotation findOne(String id) {
        log.debug("Request to get Annotation : {}", id);
        return annotationRepository.findOne(id);
    }

    /**
     * Delete the annotation by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(String id) {
        log.debug("Request to delete Annotation : {}", id);
        annotationRepository.delete(id);
    }
}
