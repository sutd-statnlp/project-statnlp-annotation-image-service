package com.sutd.statnlp.annotationimage.web;

import com.codahale.metrics.annotation.Timed;
import com.sutd.statnlp.annotationimage.model.Image;
import com.sutd.statnlp.annotationimage.service.ImageService;
import com.sutd.statnlp.annotationimage.web.error.BadRequestAlertException;
import com.sutd.statnlp.annotationimage.web.util.HeaderUtil;
import com.sutd.statnlp.annotationimage.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Image.
 */
@RestController
@RequestMapping("/api")
public class ImageResource {

    private final Logger log = LoggerFactory.getLogger(ImageResource.class);

    private static final String ENTITY_NAME = "image";

    private final ImageService imageService;

    public ImageResource(ImageService imageService) {
        this.imageService = imageService;
    }

    /**
     * POST  /images : Create a new image.
     *
     * @param image the image to create
     * @return the ResponseEntity with status 201 (Created) and with body the new image, or with status 400 (Bad Request) if the image has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/images")
    @Timed
    public ResponseEntity<Image> createImage(@RequestBody Image image) throws URISyntaxException {
        log.debug("REST request to save Image : {}", image);
        if (image.getId() != null) {
            throw new BadRequestAlertException("A new image cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Image result = imageService.save(image);
        return ResponseEntity.created(new URI("/api/images/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId()))
                .body(result);
    }

    /**
     * PUT  /images : Updates an existing image.
     *
     * @param image the image to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated image,
     * or with status 400 (Bad Request) if the image is not valid,
     * or with status 500 (Internal Server Error) if the image couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/images")
    @Timed
    public ResponseEntity<Image> updateImage(@RequestBody Image image) throws URISyntaxException {
        log.debug("REST request to update Image : {}", image);
        if (image.getId() == null) {
            return createImage(image);
        }
        Image result = imageService.save(image);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, image.getId()))
                .body(result);
    }

    /**
     * GET  /images : get all the images.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of images in body
     */
    @GetMapping("/images")
    @Timed
    public List<Image> getAllImages() {
        log.debug("REST request to get all Images");
        return imageService.findAll();
    }

    /**
     * GET  /images/:id : get the "id" image.
     *
     * @param id the id of the image to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the image, or with status 404 (Not Found)
     */
    @GetMapping("/images/{id}")
    @Timed
    public ResponseEntity<Image> getImage(@PathVariable String id) {
        log.debug("REST request to get Image : {}", id);
        Image image = imageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(image));
    }

    /**
     * DELETE  /images/:id : delete the "id" image.
     *
     * @param id the id of the image to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/images/{id}")
    @Timed
    public ResponseEntity<Void> deleteImage(@PathVariable String id) {
        log.debug("REST request to delete Image : {}", id);
        imageService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }
}
