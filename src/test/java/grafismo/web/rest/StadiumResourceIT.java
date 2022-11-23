package grafismo.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import grafismo.IntegrationTest;
import grafismo.domain.Stadium;
import grafismo.repository.StadiumRepository;
import grafismo.service.dto.StadiumDTO;
import grafismo.service.mapper.StadiumMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link StadiumResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class StadiumResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_GRAPHICS_NAME = "AAAAAAAAAA";
    private static final String UPDATED_GRAPHICS_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final Integer DEFAULT_CAPACITY = 0;
    private static final Integer UPDATED_CAPACITY = 1;

    private static final Integer DEFAULT_FIELD_LENGTH = 0;
    private static final Integer UPDATED_FIELD_LENGTH = 1;

    private static final Integer DEFAULT_FIELD_WIDTH = 0;
    private static final Integer UPDATED_FIELD_WIDTH = 1;

    private static final String ENTITY_API_URL = "/api/stadiums";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private StadiumRepository stadiumRepository;

    @Autowired
    private StadiumMapper stadiumMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStadiumMockMvc;

    private Stadium stadium;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Stadium createEntity(EntityManager em) {
        Stadium stadium = new Stadium()
            .name(DEFAULT_NAME)
            .graphicsName(DEFAULT_GRAPHICS_NAME)
            .location(DEFAULT_LOCATION)
            .capacity(DEFAULT_CAPACITY)
            .fieldLength(DEFAULT_FIELD_LENGTH)
            .fieldWidth(DEFAULT_FIELD_WIDTH);
        return stadium;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Stadium createUpdatedEntity(EntityManager em) {
        Stadium stadium = new Stadium()
            .name(UPDATED_NAME)
            .graphicsName(UPDATED_GRAPHICS_NAME)
            .location(UPDATED_LOCATION)
            .capacity(UPDATED_CAPACITY)
            .fieldLength(UPDATED_FIELD_LENGTH)
            .fieldWidth(UPDATED_FIELD_WIDTH);
        return stadium;
    }

    @BeforeEach
    public void initTest() {
        stadium = createEntity(em);
    }

    @Test
    @Transactional
    void createStadium() throws Exception {
        int databaseSizeBeforeCreate = stadiumRepository.findAll().size();
        // Create the Stadium
        StadiumDTO stadiumDTO = stadiumMapper.toDto(stadium);
        restStadiumMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(stadiumDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Stadium in the database
        List<Stadium> stadiumList = stadiumRepository.findAll();
        assertThat(stadiumList).hasSize(databaseSizeBeforeCreate + 1);
        Stadium testStadium = stadiumList.get(stadiumList.size() - 1);
        assertThat(testStadium.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testStadium.getGraphicsName()).isEqualTo(DEFAULT_GRAPHICS_NAME);
        assertThat(testStadium.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testStadium.getCapacity()).isEqualTo(DEFAULT_CAPACITY);
        assertThat(testStadium.getFieldLength()).isEqualTo(DEFAULT_FIELD_LENGTH);
        assertThat(testStadium.getFieldWidth()).isEqualTo(DEFAULT_FIELD_WIDTH);
    }

    @Test
    @Transactional
    void createStadiumWithExistingId() throws Exception {
        // Create the Stadium with an existing ID
        stadium.setId(1L);
        StadiumDTO stadiumDTO = stadiumMapper.toDto(stadium);

        int databaseSizeBeforeCreate = stadiumRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStadiumMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(stadiumDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Stadium in the database
        List<Stadium> stadiumList = stadiumRepository.findAll();
        assertThat(stadiumList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkGraphicsNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = stadiumRepository.findAll().size();
        // set the field null
        stadium.setGraphicsName(null);

        // Create the Stadium, which fails.
        StadiumDTO stadiumDTO = stadiumMapper.toDto(stadium);

        restStadiumMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(stadiumDTO))
            )
            .andExpect(status().isBadRequest());

        List<Stadium> stadiumList = stadiumRepository.findAll();
        assertThat(stadiumList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllStadiums() throws Exception {
        // Initialize the database
        stadiumRepository.saveAndFlush(stadium);

        // Get all the stadiumList
        restStadiumMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stadium.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].graphicsName").value(hasItem(DEFAULT_GRAPHICS_NAME)))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].capacity").value(hasItem(DEFAULT_CAPACITY)))
            .andExpect(jsonPath("$.[*].fieldLength").value(hasItem(DEFAULT_FIELD_LENGTH)))
            .andExpect(jsonPath("$.[*].fieldWidth").value(hasItem(DEFAULT_FIELD_WIDTH)));
    }

    @Test
    @Transactional
    void getStadium() throws Exception {
        // Initialize the database
        stadiumRepository.saveAndFlush(stadium);

        // Get the stadium
        restStadiumMockMvc
            .perform(get(ENTITY_API_URL_ID, stadium.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(stadium.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.graphicsName").value(DEFAULT_GRAPHICS_NAME))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION))
            .andExpect(jsonPath("$.capacity").value(DEFAULT_CAPACITY))
            .andExpect(jsonPath("$.fieldLength").value(DEFAULT_FIELD_LENGTH))
            .andExpect(jsonPath("$.fieldWidth").value(DEFAULT_FIELD_WIDTH));
    }

    @Test
    @Transactional
    void getNonExistingStadium() throws Exception {
        // Get the stadium
        restStadiumMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewStadium() throws Exception {
        // Initialize the database
        stadiumRepository.saveAndFlush(stadium);

        int databaseSizeBeforeUpdate = stadiumRepository.findAll().size();

        // Update the stadium
        Stadium updatedStadium = stadiumRepository.findById(stadium.getId()).get();
        // Disconnect from session so that the updates on updatedStadium are not directly saved in db
        em.detach(updatedStadium);
        updatedStadium
            .name(UPDATED_NAME)
            .graphicsName(UPDATED_GRAPHICS_NAME)
            .location(UPDATED_LOCATION)
            .capacity(UPDATED_CAPACITY)
            .fieldLength(UPDATED_FIELD_LENGTH)
            .fieldWidth(UPDATED_FIELD_WIDTH);
        StadiumDTO stadiumDTO = stadiumMapper.toDto(updatedStadium);

        restStadiumMockMvc
            .perform(
                put(ENTITY_API_URL_ID, stadiumDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(stadiumDTO))
            )
            .andExpect(status().isOk());

        // Validate the Stadium in the database
        List<Stadium> stadiumList = stadiumRepository.findAll();
        assertThat(stadiumList).hasSize(databaseSizeBeforeUpdate);
        Stadium testStadium = stadiumList.get(stadiumList.size() - 1);
        assertThat(testStadium.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testStadium.getGraphicsName()).isEqualTo(UPDATED_GRAPHICS_NAME);
        assertThat(testStadium.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testStadium.getCapacity()).isEqualTo(UPDATED_CAPACITY);
        assertThat(testStadium.getFieldLength()).isEqualTo(UPDATED_FIELD_LENGTH);
        assertThat(testStadium.getFieldWidth()).isEqualTo(UPDATED_FIELD_WIDTH);
    }

    @Test
    @Transactional
    void putNonExistingStadium() throws Exception {
        int databaseSizeBeforeUpdate = stadiumRepository.findAll().size();
        stadium.setId(count.incrementAndGet());

        // Create the Stadium
        StadiumDTO stadiumDTO = stadiumMapper.toDto(stadium);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStadiumMockMvc
            .perform(
                put(ENTITY_API_URL_ID, stadiumDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(stadiumDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Stadium in the database
        List<Stadium> stadiumList = stadiumRepository.findAll();
        assertThat(stadiumList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStadium() throws Exception {
        int databaseSizeBeforeUpdate = stadiumRepository.findAll().size();
        stadium.setId(count.incrementAndGet());

        // Create the Stadium
        StadiumDTO stadiumDTO = stadiumMapper.toDto(stadium);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStadiumMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(stadiumDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Stadium in the database
        List<Stadium> stadiumList = stadiumRepository.findAll();
        assertThat(stadiumList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStadium() throws Exception {
        int databaseSizeBeforeUpdate = stadiumRepository.findAll().size();
        stadium.setId(count.incrementAndGet());

        // Create the Stadium
        StadiumDTO stadiumDTO = stadiumMapper.toDto(stadium);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStadiumMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(stadiumDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Stadium in the database
        List<Stadium> stadiumList = stadiumRepository.findAll();
        assertThat(stadiumList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStadiumWithPatch() throws Exception {
        // Initialize the database
        stadiumRepository.saveAndFlush(stadium);

        int databaseSizeBeforeUpdate = stadiumRepository.findAll().size();

        // Update the stadium using partial update
        Stadium partialUpdatedStadium = new Stadium();
        partialUpdatedStadium.setId(stadium.getId());

        partialUpdatedStadium.name(UPDATED_NAME).location(UPDATED_LOCATION).capacity(UPDATED_CAPACITY).fieldLength(UPDATED_FIELD_LENGTH);

        restStadiumMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStadium.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStadium))
            )
            .andExpect(status().isOk());

        // Validate the Stadium in the database
        List<Stadium> stadiumList = stadiumRepository.findAll();
        assertThat(stadiumList).hasSize(databaseSizeBeforeUpdate);
        Stadium testStadium = stadiumList.get(stadiumList.size() - 1);
        assertThat(testStadium.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testStadium.getGraphicsName()).isEqualTo(DEFAULT_GRAPHICS_NAME);
        assertThat(testStadium.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testStadium.getCapacity()).isEqualTo(UPDATED_CAPACITY);
        assertThat(testStadium.getFieldLength()).isEqualTo(UPDATED_FIELD_LENGTH);
        assertThat(testStadium.getFieldWidth()).isEqualTo(DEFAULT_FIELD_WIDTH);
    }

    @Test
    @Transactional
    void fullUpdateStadiumWithPatch() throws Exception {
        // Initialize the database
        stadiumRepository.saveAndFlush(stadium);

        int databaseSizeBeforeUpdate = stadiumRepository.findAll().size();

        // Update the stadium using partial update
        Stadium partialUpdatedStadium = new Stadium();
        partialUpdatedStadium.setId(stadium.getId());

        partialUpdatedStadium
            .name(UPDATED_NAME)
            .graphicsName(UPDATED_GRAPHICS_NAME)
            .location(UPDATED_LOCATION)
            .capacity(UPDATED_CAPACITY)
            .fieldLength(UPDATED_FIELD_LENGTH)
            .fieldWidth(UPDATED_FIELD_WIDTH);

        restStadiumMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStadium.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStadium))
            )
            .andExpect(status().isOk());

        // Validate the Stadium in the database
        List<Stadium> stadiumList = stadiumRepository.findAll();
        assertThat(stadiumList).hasSize(databaseSizeBeforeUpdate);
        Stadium testStadium = stadiumList.get(stadiumList.size() - 1);
        assertThat(testStadium.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testStadium.getGraphicsName()).isEqualTo(UPDATED_GRAPHICS_NAME);
        assertThat(testStadium.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testStadium.getCapacity()).isEqualTo(UPDATED_CAPACITY);
        assertThat(testStadium.getFieldLength()).isEqualTo(UPDATED_FIELD_LENGTH);
        assertThat(testStadium.getFieldWidth()).isEqualTo(UPDATED_FIELD_WIDTH);
    }

    @Test
    @Transactional
    void patchNonExistingStadium() throws Exception {
        int databaseSizeBeforeUpdate = stadiumRepository.findAll().size();
        stadium.setId(count.incrementAndGet());

        // Create the Stadium
        StadiumDTO stadiumDTO = stadiumMapper.toDto(stadium);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStadiumMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, stadiumDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(stadiumDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Stadium in the database
        List<Stadium> stadiumList = stadiumRepository.findAll();
        assertThat(stadiumList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStadium() throws Exception {
        int databaseSizeBeforeUpdate = stadiumRepository.findAll().size();
        stadium.setId(count.incrementAndGet());

        // Create the Stadium
        StadiumDTO stadiumDTO = stadiumMapper.toDto(stadium);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStadiumMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(stadiumDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Stadium in the database
        List<Stadium> stadiumList = stadiumRepository.findAll();
        assertThat(stadiumList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStadium() throws Exception {
        int databaseSizeBeforeUpdate = stadiumRepository.findAll().size();
        stadium.setId(count.incrementAndGet());

        // Create the Stadium
        StadiumDTO stadiumDTO = stadiumMapper.toDto(stadium);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStadiumMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(stadiumDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Stadium in the database
        List<Stadium> stadiumList = stadiumRepository.findAll();
        assertThat(stadiumList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStadium() throws Exception {
        // Initialize the database
        stadiumRepository.saveAndFlush(stadium);

        int databaseSizeBeforeDelete = stadiumRepository.findAll().size();

        // Delete the stadium
        restStadiumMockMvc
            .perform(delete(ENTITY_API_URL_ID, stadium.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Stadium> stadiumList = stadiumRepository.findAll();
        assertThat(stadiumList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
