package com.sutd.statnlp.annotationimage.repository;

import com.sutd.statnlp.annotationimage.model.Annotation;
import org.springframework.stereotype.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Annotation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AnnotationRepository extends MongoRepository<Annotation, String> {

}
