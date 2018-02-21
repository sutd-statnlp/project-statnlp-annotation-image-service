package com.sutd.statnlp.annotationimage.web;

import com.codahale.metrics.annotation.Timed;
import com.sutd.statnlp.annotationimage.model.Annotation;
import com.sutd.statnlp.annotationimage.service.AnnotationService;
import com.sutd.statnlp.annotationimage.web.error.BadRequestAlertException;
import com.sutd.statnlp.annotationimage.web.util.HeaderUtil;
import com.sutd.statnlp.annotationimage.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Annotation.
 */
@RestController
@RequestMapping("/api")
public class AnnotationResource {

    private final Logger log = LoggerFactory.getLogger(AnnotationResource.class);

    private static final String ENTITY_NAME = "annotation";

    private final AnnotationService annotationService;

    public AnnotationResource(AnnotationService annotationService) {
        this.annotationService = annotationService;
    }

    /**
     * POST  /annotations : Create a new annotation.
     *
     * @param annotation the annotation to create
     * @return the ResponseEntity with status 201 (Created) and with body the new annotation, or with status 400 (Bad Request) if the annotation has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/annotations")
    @Timed
    public ResponseEntity<Annotation> createAnnotation(@Valid @RequestBody Annotation annotation) throws URISyntaxException {
        log.debug("REST request to save Annotation : {}", annotation);
        if (annotation.getId() != null) {
            throw new BadRequestAlertException("A new annotation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Annotation result = annotationService.save(annotation);
        return ResponseEntity.created(new URI("/api/annotations/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId()))
                .body(result);
    }

    /**
     * PUT  /annotations : Updates an existing annotation.
     *
     * @param annotation the annotation to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated annotation,
     * or with status 400 (Bad Request) if the annotation is not valid,
     * or with status 500 (Internal Server Error) if the annotation couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/annotations")
    @Timed
    public ResponseEntity<Annotation> updateAnnotation(@Valid @RequestBody Annotation annotation) throws URISyntaxException {
        log.debug("REST request to update Annotation : {}", annotation);
        if (annotation.getId() == null) {
            return createAnnotation(annotation);
        }
        Annotation result = annotationService.save(annotation);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, annotation.getId()))
                .body(result);
    }

    /**
     * GET  /annotations : get all the annotations.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of annotations in body
     */
    @GetMapping("/annotations")
    @Timed
    public List<Annotation> getAllAnnotations() {
        log.debug("REST request to get all Annotations");
        return annotationService.findAll();
    }

    /**
     * GET  /annotations/:id : get the "id" annotation.
     *
     * @param id the id of the annotation to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the annotation, or with status 404 (Not Found)
     */
    @GetMapping("/annotations/{id}")
    @Timed
    public ResponseEntity<Annotation> getAnnotation(@PathVariable String id) {
        log.debug("REST request to get Annotation : {}", id);
        Annotation annotation = annotationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(annotation));
    }

    /**
     * DELETE  /annotations/:id : delete the "id" annotation.
     *
     * @param id the id of the annotation to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/annotations/{id}")
    @Timed
    public ResponseEntity<Void> deleteAnnotation(@PathVariable String id) {
        log.debug("REST request to delete Annotation : {}", id);
        annotationService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }
}
