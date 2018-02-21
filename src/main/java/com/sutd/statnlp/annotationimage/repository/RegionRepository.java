package com.sutd.statnlp.annotationimage.repository;

import com.sutd.statnlp.annotationimage.model.Region;
import org.springframework.stereotype.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Region entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RegionRepository extends MongoRepository<Region, String> {

}