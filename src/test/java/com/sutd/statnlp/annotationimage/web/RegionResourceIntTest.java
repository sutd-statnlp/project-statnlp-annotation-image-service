package com.sutd.statnlp.annotationimage.web;

import com.sutd.statnlp.annotationimage.AnnotationImageApplication;
import com.sutd.statnlp.annotationimage.model.Region;
import com.sutd.statnlp.annotationimage.repository.RegionRepository;
import com.sutd.statnlp.annotationimage.service.RegionService;
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
 * Test class for the RegionResource REST controller.
 *
 * @see RegionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AnnotationImageApplication.class)
public class RegionResourceIntTest {

    private static final Integer DEFAULT_HEIGHT = 1;
    private static final Integer UPDATED_HEIGHT = 2;

    private static final String DEFAULT_IMAGE_ID = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE_ID = "BBBBBBBBBB";

    private static final Integer DEFAULT_WIDTH = 1;
    private static final Integer UPDATED_WIDTH = 2;

    private static final String DEFAULT_PHRASE = "AAAAAAAAAA";
    private static final String UPDATED_PHRASE = "BBBBBBBBBB";

    private static final Integer DEFAULT_X = 1;
    private static final Integer UPDATED_X = 2;

    private static final Integer DEFAULT_Y = 1;
    private static final Integer UPDATED_Y = 2;

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private RegionService regionService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restRegionMockMvc;

    private Region region;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RegionResource regionResource = new RegionResource(regionService);
        this.restRegionMockMvc = MockMvcBuilders.standaloneSetup(regionResource)
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
    public static Region createEntity() {
        Region region = new Region()
                .height(DEFAULT_HEIGHT)
                .imageId(DEFAULT_IMAGE_ID)
                .width(DEFAULT_WIDTH)
                .phrase(DEFAULT_PHRASE)
                .x(DEFAULT_X)
                .y(DEFAULT_Y);
        return region;
    }

    @Before
    public void initTest() {
        regionRepository.deleteAll();
        region = createEntity();
    }

    @Test
    public void createRegion() throws Exception {
        int databaseSizeBeforeCreate = regionRepository.findAll().size();

        // Create the Region
        restRegionMockMvc.perform(post("/api/regions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(region)))
                .andExpect(status().isCreated());

        // Validate the Region in the database
        List<Region> regionList = regionRepository.findAll();
        assertThat(regionList).hasSize(databaseSizeBeforeCreate + 1);
        Region testRegion = regionList.get(regionList.size() - 1);
        assertThat(testRegion.getHeight()).isEqualTo(DEFAULT_HEIGHT);
        assertThat(testRegion.getImageId()).isEqualTo(DEFAULT_IMAGE_ID);
        assertThat(testRegion.getWidth()).isEqualTo(DEFAULT_WIDTH);
        assertThat(testRegion.getPhrase()).isEqualTo(DEFAULT_PHRASE);
        assertThat(testRegion.getX()).isEqualTo(DEFAULT_X);
        assertThat(testRegion.getY()).isEqualTo(DEFAULT_Y);
    }

    @Test
    public void createRegionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = regionRepository.findAll().size();

        // Create the Region with an existing ID
        region.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restRegionMockMvc.perform(post("/api/regions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(region)))
                .andExpect(status().isBadRequest());

        // Validate the Region in the database
        List<Region> regionList = regionRepository.findAll();
        assertThat(regionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void checkImageIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = regionRepository.findAll().size();
        // set the field null
        region.setImageId(null);

        // Create the Region, which fails.

        restRegionMockMvc.perform(post("/api/regions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(region)))
                .andExpect(status().isBadRequest());

        List<Region> regionList = regionRepository.findAll();
        assertThat(regionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllRegions() throws Exception {
        // Initialize the database
        regionRepository.save(region);

        // Get all the regionList
        restRegionMockMvc.perform(get("/api/regions?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(region.getId())))
                .andExpect(jsonPath("$.[*].height").value(hasItem(DEFAULT_HEIGHT)))
                .andExpect(jsonPath("$.[*].imageId").value(hasItem(DEFAULT_IMAGE_ID.toString())))
                .andExpect(jsonPath("$.[*].width").value(hasItem(DEFAULT_WIDTH)))
                .andExpect(jsonPath("$.[*].phrase").value(hasItem(DEFAULT_PHRASE.toString())))
                .andExpect(jsonPath("$.[*].x").value(hasItem(DEFAULT_X)))
                .andExpect(jsonPath("$.[*].y").value(hasItem(DEFAULT_Y)));
    }

    @Test
    public void getRegion() throws Exception {
        // Initialize the database
        regionRepository.save(region);

        // Get the region
        restRegionMockMvc.perform(get("/api/regions/{id}", region.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id").value(region.getId()))
                .andExpect(jsonPath("$.height").value(DEFAULT_HEIGHT))
                .andExpect(jsonPath("$.imageId").value(DEFAULT_IMAGE_ID.toString()))
                .andExpect(jsonPath("$.width").value(DEFAULT_WIDTH))
                .andExpect(jsonPath("$.phrase").value(DEFAULT_PHRASE.toString()))
                .andExpect(jsonPath("$.x").value(DEFAULT_X))
                .andExpect(jsonPath("$.y").value(DEFAULT_Y));
    }

    @Test
    public void getNonExistingRegion() throws Exception {
        // Get the region
        restRegionMockMvc.perform(get("/api/regions/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateRegion() throws Exception {
        // Initialize the database
        regionService.save(region);

        int databaseSizeBeforeUpdate = regionRepository.findAll().size();

        // Update the region
        Region updatedRegion = regionRepository.findOne(region.getId());
        updatedRegion
                .height(UPDATED_HEIGHT)
                .imageId(UPDATED_IMAGE_ID)
                .width(UPDATED_WIDTH)
                .phrase(UPDATED_PHRASE)
                .x(UPDATED_X)
                .y(UPDATED_Y);

        restRegionMockMvc.perform(put("/api/regions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedRegion)))
                .andExpect(status().isOk());

        // Validate the Region in the database
        List<Region> regionList = regionRepository.findAll();
        assertThat(regionList).hasSize(databaseSizeBeforeUpdate);
        Region testRegion = regionList.get(regionList.size() - 1);
        assertThat(testRegion.getHeight()).isEqualTo(UPDATED_HEIGHT);
        assertThat(testRegion.getImageId()).isEqualTo(UPDATED_IMAGE_ID);
        assertThat(testRegion.getWidth()).isEqualTo(UPDATED_WIDTH);
        assertThat(testRegion.getPhrase()).isEqualTo(UPDATED_PHRASE);
        assertThat(testRegion.getX()).isEqualTo(UPDATED_X);
        assertThat(testRegion.getY()).isEqualTo(UPDATED_Y);
    }

    @Test
    public void updateNonExistingRegion() throws Exception {
        int databaseSizeBeforeUpdate = regionRepository.findAll().size();

        // Create the Region

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restRegionMockMvc.perform(put("/api/regions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(region)))
                .andExpect(status().isCreated());

        // Validate the Region in the database
        List<Region> regionList = regionRepository.findAll();
        assertThat(regionList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    public void deleteRegion() throws Exception {
        // Initialize the database
        regionService.save(region);

        int databaseSizeBeforeDelete = regionRepository.findAll().size();

        // Get the region
        restRegionMockMvc.perform(delete("/api/regions/{id}", region.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Region> regionList = regionRepository.findAll();
        assertThat(regionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Region.class);
        Region region1 = new Region();
        region1.setId("id1");
        Region region2 = new Region();
        region2.setId(region1.getId());
        assertThat(region1).isEqualTo(region2);
        region2.setId("id2");
        assertThat(region1).isNotEqualTo(region2);
        region1.setId(null);
        assertThat(region1).isNotEqualTo(region2);
    }
}
