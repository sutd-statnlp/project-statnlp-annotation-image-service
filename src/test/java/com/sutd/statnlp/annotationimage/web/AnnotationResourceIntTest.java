package com.sutd.statnlp.annotationimage.web;


import com.sutd.statnlp.annotationimage.AnnotationImageApplication;
import com.sutd.statnlp.annotationimage.model.Annotation;
import com.sutd.statnlp.annotationimage.repository.AnnotationRepository;
import com.sutd.statnlp.annotationimage.service.AnnotationService;
import com.sutd.statnlp.annotationimage.web.error.ExceptionTranslator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static com.sutd.statnlp.annotationimage.web.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the AnnotationResource REST controller.
 *
 * @see AnnotationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AnnotationImageApplication.class)
public class AnnotationResourceIntTest {

    private static final String DEFAULT_IMAGE_ID = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE_ID = "BBBBBBBBBB";

    @Autowired
    private AnnotationRepository annotationRepository;

    @Autowired
    private AnnotationService annotationService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restAnnotationMockMvc;

    private Annotation annotation;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AnnotationResource annotationResource = new AnnotationResource(annotationService);
        this.restAnnotationMockMvc = MockMvcBuilders.standaloneSetup(annotationResource)
                .setCustomArgumentResolvers(pageableArgumentResolver)
                .setControllerAdvice(exceptionTranslator)
                .setConversionService(createFormattingConversionService())
                .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Annotation createEntity() {
        Annotation annotation = new Annotation()
                .imageId(DEFAULT_IMAGE_ID);
        return annotation;
    }

    @Before
    public void initTest() {
        annotationRepository.deleteAll();
        annotation = createEntity();
    }

    @Test
    public void createAnnotation() throws Exception {
        int databaseSizeBeforeCreate = annotationRepository.findAll().size();

        // Create the Annotation
        restAnnotationMockMvc.perform(post("/api/annotations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(annotation)))
                .andExpect(status().isCreated());

        // Validate the Annotation in the database
        List<Annotation> annotationList = annotationRepository.findAll();
        assertThat(annotationList).hasSize(databaseSizeBeforeCreate + 1);
        Annotation testAnnotation = annotationList.get(annotationList.size() - 1);
        assertThat(testAnnotation.getImageId()).isEqualTo(DEFAULT_IMAGE_ID);
    }

    @Test
    public void createAnnotationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = annotationRepository.findAll().size();

        // Create the Annotation with an existing ID
        annotation.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restAnnotationMockMvc.perform(post("/api/annotations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(annotation)))
                .andExpect(status().isBadRequest());

        // Validate the Annotation in the database
        List<Annotation> annotationList = annotationRepository.findAll();
        assertThat(annotationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void checkImageIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = annotationRepository.findAll().size();
        // set the field null
        annotation.setImageId(null);

        // Create the Annotation, which fails.

        restAnnotationMockMvc.perform(post("/api/annotations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(annotation)))
                .andExpect(status().isBadRequest());

        List<Annotation> annotationList = annotationRepository.findAll();
        assertThat(annotationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllAnnotations() throws Exception {
        // Initialize the database
        annotationRepository.save(annotation);

        // Get all the annotationList
        restAnnotationMockMvc.perform(get("/api/annotations?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(annotation.getId())))
                .andExpect(jsonPath("$.[*].imageId").value(hasItem(DEFAULT_IMAGE_ID.toString())));
    }

    @Test
    public void getAnnotation() throws Exception {
        // Initialize the database
        annotationRepository.save(annotation);

        // Get the annotation
        restAnnotationMockMvc.perform(get("/api/annotations/{id}", annotation.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id").value(annotation.getId()))
                .andExpect(jsonPath("$.imageId").value(DEFAULT_IMAGE_ID.toString()));
    }

    @Test
    public void getNonExistingAnnotation() throws Exception {
        // Get the annotation
        restAnnotationMockMvc.perform(get("/api/annotations/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateAnnotation() throws Exception {
        // Initialize the database
        annotationService.save(annotation);

        int databaseSizeBeforeUpdate = annotationRepository.findAll().size();

        // Update the annotation
        Annotation updatedAnnotation = annotationRepository.findOne(annotation.getId());
        updatedAnnotation
                .imageId(UPDATED_IMAGE_ID);

        restAnnotationMockMvc.perform(put("/api/annotations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedAnnotation)))
                .andExpect(status().isOk());

        // Validate the Annotation in the database
        List<Annotation> annotationList = annotationRepository.findAll();
        assertThat(annotationList).hasSize(databaseSizeBeforeUpdate);
        Annotation testAnnotation = annotationList.get(annotationList.size() - 1);
        assertThat(testAnnotation.getImageId()).isEqualTo(UPDATED_IMAGE_ID);
    }

    @Test
    public void updateNonExistingAnnotation() throws Exception {
        int databaseSizeBeforeUpdate = annotationRepository.findAll().size();

        // Create the Annotation

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAnnotationMockMvc.perform(put("/api/annotations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(annotation)))
                .andExpect(status().isCreated());

        // Validate the Annotation in the database
        List<Annotation> annotationList = annotationRepository.findAll();
        assertThat(annotationList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    public void deleteAnnotation() throws Exception {
        // Initialize the database
        annotationService.save(annotation);

        int databaseSizeBeforeDelete = annotationRepository.findAll().size();

        // Get the annotation
        restAnnotationMockMvc.perform(delete("/api/annotations/{id}", annotation.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Annotation> annotationList = annotationRepository.findAll();
        assertThat(annotationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Annotation.class);
        Annotation annotation1 = new Annotation();
        annotation1.setId("id1");
        Annotation annotation2 = new Annotation();
        annotation2.setId(annotation1.getId());
        assertThat(annotation1).isEqualTo(annotation2);
        annotation2.setId("id2");
        assertThat(annotation1).isNotEqualTo(annotation2);
        annotation1.setId(null);
        assertThat(annotation1).isNotEqualTo(annotation2);
    }
}
