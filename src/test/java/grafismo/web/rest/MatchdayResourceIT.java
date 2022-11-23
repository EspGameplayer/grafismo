package grafismo.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import grafismo.IntegrationTest;
import grafismo.domain.Matchday;
import grafismo.repository.MatchdayRepository;
import grafismo.service.MatchdayService;
import grafismo.service.dto.MatchdayDTO;
import grafismo.service.mapper.MatchdayMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link MatchdayResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class MatchdayResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_GRAPHICS_NAME = "AAAAAAAAAA";
    private static final String UPDATED_GRAPHICS_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_NUMBER = 0;
    private static final Integer UPDATED_NUMBER = 1;

    private static final String ENTITY_API_URL = "/api/matchdays";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MatchdayRepository matchdayRepository;

    @Mock
    private MatchdayRepository matchdayRepositoryMock;

    @Autowired
    private MatchdayMapper matchdayMapper;

    @Mock
    private MatchdayService matchdayServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMatchdayMockMvc;

    private Matchday matchday;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Matchday createEntity(EntityManager em) {
        Matchday matchday = new Matchday().name(DEFAULT_NAME).graphicsName(DEFAULT_GRAPHICS_NAME).number(DEFAULT_NUMBER);
        return matchday;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Matchday createUpdatedEntity(EntityManager em) {
        Matchday matchday = new Matchday().name(UPDATED_NAME).graphicsName(UPDATED_GRAPHICS_NAME).number(UPDATED_NUMBER);
        return matchday;
    }

    @BeforeEach
    public void initTest() {
        matchday = createEntity(em);
    }

    @Test
    @Transactional
    void createMatchday() throws Exception {
        int databaseSizeBeforeCreate = matchdayRepository.findAll().size();
        // Create the Matchday
        MatchdayDTO matchdayDTO = matchdayMapper.toDto(matchday);
        restMatchdayMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(matchdayDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Matchday in the database
        List<Matchday> matchdayList = matchdayRepository.findAll();
        assertThat(matchdayList).hasSize(databaseSizeBeforeCreate + 1);
        Matchday testMatchday = matchdayList.get(matchdayList.size() - 1);
        assertThat(testMatchday.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMatchday.getGraphicsName()).isEqualTo(DEFAULT_GRAPHICS_NAME);
        assertThat(testMatchday.getNumber()).isEqualTo(DEFAULT_NUMBER);
    }

    @Test
    @Transactional
    void createMatchdayWithExistingId() throws Exception {
        // Create the Matchday with an existing ID
        matchday.setId(1L);
        MatchdayDTO matchdayDTO = matchdayMapper.toDto(matchday);

        int databaseSizeBeforeCreate = matchdayRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMatchdayMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(matchdayDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Matchday in the database
        List<Matchday> matchdayList = matchdayRepository.findAll();
        assertThat(matchdayList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = matchdayRepository.findAll().size();
        // set the field null
        matchday.setName(null);

        // Create the Matchday, which fails.
        MatchdayDTO matchdayDTO = matchdayMapper.toDto(matchday);

        restMatchdayMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(matchdayDTO))
            )
            .andExpect(status().isBadRequest());

        List<Matchday> matchdayList = matchdayRepository.findAll();
        assertThat(matchdayList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMatchdays() throws Exception {
        // Initialize the database
        matchdayRepository.saveAndFlush(matchday);

        // Get all the matchdayList
        restMatchdayMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(matchday.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].graphicsName").value(hasItem(DEFAULT_GRAPHICS_NAME)))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMatchdaysWithEagerRelationshipsIsEnabled() throws Exception {
        when(matchdayServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMatchdayMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(matchdayServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMatchdaysWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(matchdayServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMatchdayMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(matchdayServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getMatchday() throws Exception {
        // Initialize the database
        matchdayRepository.saveAndFlush(matchday);

        // Get the matchday
        restMatchdayMockMvc
            .perform(get(ENTITY_API_URL_ID, matchday.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(matchday.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.graphicsName").value(DEFAULT_GRAPHICS_NAME))
            .andExpect(jsonPath("$.number").value(DEFAULT_NUMBER));
    }

    @Test
    @Transactional
    void getNonExistingMatchday() throws Exception {
        // Get the matchday
        restMatchdayMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMatchday() throws Exception {
        // Initialize the database
        matchdayRepository.saveAndFlush(matchday);

        int databaseSizeBeforeUpdate = matchdayRepository.findAll().size();

        // Update the matchday
        Matchday updatedMatchday = matchdayRepository.findById(matchday.getId()).get();
        // Disconnect from session so that the updates on updatedMatchday are not directly saved in db
        em.detach(updatedMatchday);
        updatedMatchday.name(UPDATED_NAME).graphicsName(UPDATED_GRAPHICS_NAME).number(UPDATED_NUMBER);
        MatchdayDTO matchdayDTO = matchdayMapper.toDto(updatedMatchday);

        restMatchdayMockMvc
            .perform(
                put(ENTITY_API_URL_ID, matchdayDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(matchdayDTO))
            )
            .andExpect(status().isOk());

        // Validate the Matchday in the database
        List<Matchday> matchdayList = matchdayRepository.findAll();
        assertThat(matchdayList).hasSize(databaseSizeBeforeUpdate);
        Matchday testMatchday = matchdayList.get(matchdayList.size() - 1);
        assertThat(testMatchday.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMatchday.getGraphicsName()).isEqualTo(UPDATED_GRAPHICS_NAME);
        assertThat(testMatchday.getNumber()).isEqualTo(UPDATED_NUMBER);
    }

    @Test
    @Transactional
    void putNonExistingMatchday() throws Exception {
        int databaseSizeBeforeUpdate = matchdayRepository.findAll().size();
        matchday.setId(count.incrementAndGet());

        // Create the Matchday
        MatchdayDTO matchdayDTO = matchdayMapper.toDto(matchday);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMatchdayMockMvc
            .perform(
                put(ENTITY_API_URL_ID, matchdayDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(matchdayDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Matchday in the database
        List<Matchday> matchdayList = matchdayRepository.findAll();
        assertThat(matchdayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMatchday() throws Exception {
        int databaseSizeBeforeUpdate = matchdayRepository.findAll().size();
        matchday.setId(count.incrementAndGet());

        // Create the Matchday
        MatchdayDTO matchdayDTO = matchdayMapper.toDto(matchday);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMatchdayMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(matchdayDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Matchday in the database
        List<Matchday> matchdayList = matchdayRepository.findAll();
        assertThat(matchdayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMatchday() throws Exception {
        int databaseSizeBeforeUpdate = matchdayRepository.findAll().size();
        matchday.setId(count.incrementAndGet());

        // Create the Matchday
        MatchdayDTO matchdayDTO = matchdayMapper.toDto(matchday);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMatchdayMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(matchdayDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Matchday in the database
        List<Matchday> matchdayList = matchdayRepository.findAll();
        assertThat(matchdayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMatchdayWithPatch() throws Exception {
        // Initialize the database
        matchdayRepository.saveAndFlush(matchday);

        int databaseSizeBeforeUpdate = matchdayRepository.findAll().size();

        // Update the matchday using partial update
        Matchday partialUpdatedMatchday = new Matchday();
        partialUpdatedMatchday.setId(matchday.getId());

        partialUpdatedMatchday.name(UPDATED_NAME).graphicsName(UPDATED_GRAPHICS_NAME);

        restMatchdayMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMatchday.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMatchday))
            )
            .andExpect(status().isOk());

        // Validate the Matchday in the database
        List<Matchday> matchdayList = matchdayRepository.findAll();
        assertThat(matchdayList).hasSize(databaseSizeBeforeUpdate);
        Matchday testMatchday = matchdayList.get(matchdayList.size() - 1);
        assertThat(testMatchday.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMatchday.getGraphicsName()).isEqualTo(UPDATED_GRAPHICS_NAME);
        assertThat(testMatchday.getNumber()).isEqualTo(DEFAULT_NUMBER);
    }

    @Test
    @Transactional
    void fullUpdateMatchdayWithPatch() throws Exception {
        // Initialize the database
        matchdayRepository.saveAndFlush(matchday);

        int databaseSizeBeforeUpdate = matchdayRepository.findAll().size();

        // Update the matchday using partial update
        Matchday partialUpdatedMatchday = new Matchday();
        partialUpdatedMatchday.setId(matchday.getId());

        partialUpdatedMatchday.name(UPDATED_NAME).graphicsName(UPDATED_GRAPHICS_NAME).number(UPDATED_NUMBER);

        restMatchdayMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMatchday.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMatchday))
            )
            .andExpect(status().isOk());

        // Validate the Matchday in the database
        List<Matchday> matchdayList = matchdayRepository.findAll();
        assertThat(matchdayList).hasSize(databaseSizeBeforeUpdate);
        Matchday testMatchday = matchdayList.get(matchdayList.size() - 1);
        assertThat(testMatchday.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMatchday.getGraphicsName()).isEqualTo(UPDATED_GRAPHICS_NAME);
        assertThat(testMatchday.getNumber()).isEqualTo(UPDATED_NUMBER);
    }

    @Test
    @Transactional
    void patchNonExistingMatchday() throws Exception {
        int databaseSizeBeforeUpdate = matchdayRepository.findAll().size();
        matchday.setId(count.incrementAndGet());

        // Create the Matchday
        MatchdayDTO matchdayDTO = matchdayMapper.toDto(matchday);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMatchdayMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, matchdayDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(matchdayDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Matchday in the database
        List<Matchday> matchdayList = matchdayRepository.findAll();
        assertThat(matchdayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMatchday() throws Exception {
        int databaseSizeBeforeUpdate = matchdayRepository.findAll().size();
        matchday.setId(count.incrementAndGet());

        // Create the Matchday
        MatchdayDTO matchdayDTO = matchdayMapper.toDto(matchday);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMatchdayMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(matchdayDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Matchday in the database
        List<Matchday> matchdayList = matchdayRepository.findAll();
        assertThat(matchdayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMatchday() throws Exception {
        int databaseSizeBeforeUpdate = matchdayRepository.findAll().size();
        matchday.setId(count.incrementAndGet());

        // Create the Matchday
        MatchdayDTO matchdayDTO = matchdayMapper.toDto(matchday);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMatchdayMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(matchdayDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Matchday in the database
        List<Matchday> matchdayList = matchdayRepository.findAll();
        assertThat(matchdayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMatchday() throws Exception {
        // Initialize the database
        matchdayRepository.saveAndFlush(matchday);

        int databaseSizeBeforeDelete = matchdayRepository.findAll().size();

        // Delete the matchday
        restMatchdayMockMvc
            .perform(delete(ENTITY_API_URL_ID, matchday.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Matchday> matchdayList = matchdayRepository.findAll();
        assertThat(matchdayList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
