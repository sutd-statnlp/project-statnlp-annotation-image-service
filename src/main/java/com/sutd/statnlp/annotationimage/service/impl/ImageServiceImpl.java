package com.sutd.statnlp.annotationimage.service.impl;

import com.sutd.statnlp.annotationimage.model.Image;
import com.sutd.statnlp.annotationimage.repository.ImageRepository;
import com.sutd.statnlp.annotationimage.service.ImageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service Implementation for managing Image.
 */
@Service
public class ImageServiceImpl implements ImageService {

    private final Logger log = LoggerFactory.getLogger(ImageServiceImpl.class);

    private final ImageRepository imageRepository;

    public ImageServiceImpl(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    /**
     * Save a image.
     *
     * @param image the entity to save
     * @return the persisted entity
     */
    @Override
    public Image save(Image image) {
        log.debug("Request to save Image : {}", image);
        return imageRepository.save(image);
    }

    /**
     * Get all the images.
     *
     * @return the list of entities
     */
    @Override
    public List<Image> findAll() {
        log.debug("Request to get all Images");
        return imageRepository.findAll();
    }

    /**
     * Get one image by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    public Image findOne(String id) {
        log.debug("Request to get Image : {}", id);
        return imageRepository.findOne(id);
    }

    /**
     * Delete the image by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(String id) {
        log.debug("Request to delete Image : {}", id);
        imageRepository.delete(id);
    }
}
