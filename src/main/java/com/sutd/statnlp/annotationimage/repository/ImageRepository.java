package com.sutd.statnlp.annotationimage.repository;

import com.sutd.statnlp.annotationimage.model.Image;
import org.springframework.stereotype.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Image entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ImageRepository extends MongoRepository<Image, String> {

}