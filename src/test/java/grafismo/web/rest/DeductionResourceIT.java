package grafismo.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import grafismo.IntegrationTest;
import grafismo.domain.Competition;
import grafismo.domain.Deduction;
import grafismo.domain.Team;
import grafismo.repository.DeductionRepository;
import grafismo.service.DeductionService;
import grafismo.service.dto.DeductionDTO;
import grafismo.service.mapper.DeductionMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link DeductionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class DeductionResourceIT {

    private static final Integer DEFAULT_POINTS = 1;
    private static final Integer UPDATED_POINTS = 2;

    private static final Instant DEFAULT_MOMENT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_MOMENT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_REASON = "AAAAAAAAAA";
    private static final String UPDATED_REASON = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/deductions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DeductionRepository deductionRepository;

    @Mock
    private DeductionRepository deductionRepositoryMock;

    @Autowired
    private DeductionMapper deductionMapper;

    @Mock
    private DeductionService deductionServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDeductionMockMvc;

    private Deduction deduction;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Deduction createEntity(EntityManager em) {
        Deduction deduction = new Deduction().points(DEFAULT_POINTS).moment(DEFAULT_MOMENT).reason(DEFAULT_REASON);
        // Add required entity
        Team team;
        if (TestUtil.findAll(em, Team.class).isEmpty()) {
            team = TeamResourceIT.createEntity(em);
            em.persist(team);
            em.flush();
        } else {
            team = TestUtil.findAll(em, Team.class).get(0);
        }
        deduction.setTeam(team);
        // Add required entity
        Competition competition;
        if (TestUtil.findAll(em, Competition.class).isEmpty()) {
            competition = CompetitionResourceIT.createEntity(em);
            em.persist(competition);
            em.flush();
        } else {
            competition = TestUtil.findAll(em, Competition.class).get(0);
        }
        deduction.setCompetition(competition);
        return deduction;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Deduction createUpdatedEntity(EntityManager em) {
        Deduction deduction = new Deduction().points(UPDATED_POINTS).moment(UPDATED_MOMENT).reason(UPDATED_REASON);
        // Add required entity
        Team team;
        if (TestUtil.findAll(em, Team.class).isEmpty()) {
            team = TeamResourceIT.createUpdatedEntity(em);
            em.persist(team);
            em.flush();
        } else {
            team = TestUtil.findAll(em, Team.class).get(0);
        }
        deduction.setTeam(team);
        // Add required entity
        Competition competition;
        if (TestUtil.findAll(em, Competition.class).isEmpty()) {
            competition = CompetitionResourceIT.createUpdatedEntity(em);
            em.persist(competition);
            em.flush();
        } else {
            competition = TestUtil.findAll(em, Competition.class).get(0);
        }
        deduction.setCompetition(competition);
        return deduction;
    }

    @BeforeEach
    public void initTest() {
        deduction = createEntity(em);
    }

    @Test
    @Transactional
    void createDeduction() throws Exception {
        int databaseSizeBeforeCreate = deductionRepository.findAll().size();
        // Create the Deduction
        DeductionDTO deductionDTO = deductionMapper.toDto(deduction);
        restDeductionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deductionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Deduction in the database
        List<Deduction> deductionList = deductionRepository.findAll();
        assertThat(deductionList).hasSize(databaseSizeBeforeCreate + 1);
        Deduction testDeduction = deductionList.get(deductionList.size() - 1);
        assertThat(testDeduction.getPoints()).isEqualTo(DEFAULT_POINTS);
        assertThat(testDeduction.getMoment()).isEqualTo(DEFAULT_MOMENT);
        assertThat(testDeduction.getReason()).isEqualTo(DEFAULT_REASON);
    }

    @Test
    @Transactional
    void createDeductionWithExistingId() throws Exception {
        // Create the Deduction with an existing ID
        deduction.setId(1L);
        DeductionDTO deductionDTO = deductionMapper.toDto(deduction);

        int databaseSizeBeforeCreate = deductionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDeductionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deductionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Deduction in the database
        List<Deduction> deductionList = deductionRepository.findAll();
        assertThat(deductionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDeductions() throws Exception {
        // Initialize the database
        deductionRepository.saveAndFlush(deduction);

        // Get all the deductionList
        restDeductionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(deduction.getId().intValue())))
            .andExpect(jsonPath("$.[*].points").value(hasItem(DEFAULT_POINTS)))
            .andExpect(jsonPath("$.[*].moment").value(hasItem(DEFAULT_MOMENT.toString())))
            .andExpect(jsonPath("$.[*].reason").value(hasItem(DEFAULT_REASON)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDeductionsWithEagerRelationshipsIsEnabled() throws Exception {
        when(deductionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDeductionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(deductionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDeductionsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(deductionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDeductionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(deductionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getDeduction() throws Exception {
        // Initialize the database
        deductionRepository.saveAndFlush(deduction);

        // Get the deduction
        restDeductionMockMvc
            .perform(get(ENTITY_API_URL_ID, deduction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(deduction.getId().intValue()))
            .andExpect(jsonPath("$.points").value(DEFAULT_POINTS))
            .andExpect(jsonPath("$.moment").value(DEFAULT_MOMENT.toString()))
            .andExpect(jsonPath("$.reason").value(DEFAULT_REASON));
    }

    @Test
    @Transactional
    void getNonExistingDeduction() throws Exception {
        // Get the deduction
        restDeductionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDeduction() throws Exception {
        // Initialize the database
        deductionRepository.saveAndFlush(deduction);

        int databaseSizeBeforeUpdate = deductionRepository.findAll().size();

        // Update the deduction
        Deduction updatedDeduction = deductionRepository.findById(deduction.getId()).get();
        // Disconnect from session so that the updates on updatedDeduction are not directly saved in db
        em.detach(updatedDeduction);
        updatedDeduction.points(UPDATED_POINTS).moment(UPDATED_MOMENT).reason(UPDATED_REASON);
        DeductionDTO deductionDTO = deductionMapper.toDto(updatedDeduction);

        restDeductionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, deductionDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deductionDTO))
            )
            .andExpect(status().isOk());

        // Validate the Deduction in the database
        List<Deduction> deductionList = deductionRepository.findAll();
        assertThat(deductionList).hasSize(databaseSizeBeforeUpdate);
        Deduction testDeduction = deductionList.get(deductionList.size() - 1);
        assertThat(testDeduction.getPoints()).isEqualTo(UPDATED_POINTS);
        assertThat(testDeduction.getMoment()).isEqualTo(UPDATED_MOMENT);
        assertThat(testDeduction.getReason()).isEqualTo(UPDATED_REASON);
    }

    @Test
    @Transactional
    void putNonExistingDeduction() throws Exception {
        int databaseSizeBeforeUpdate = deductionRepository.findAll().size();
        deduction.setId(count.incrementAndGet());

        // Create the Deduction
        DeductionDTO deductionDTO = deductionMapper.toDto(deduction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeductionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, deductionDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deductionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Deduction in the database
        List<Deduction> deductionList = deductionRepository.findAll();
        assertThat(deductionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDeduction() throws Exception {
        int databaseSizeBeforeUpdate = deductionRepository.findAll().size();
        deduction.setId(count.incrementAndGet());

        // Create the Deduction
        DeductionDTO deductionDTO = deductionMapper.toDto(deduction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeductionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deductionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Deduction in the database
        List<Deduction> deductionList = deductionRepository.findAll();
        assertThat(deductionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDeduction() throws Exception {
        int databaseSizeBeforeUpdate = deductionRepository.findAll().size();
        deduction.setId(count.incrementAndGet());

        // Create the Deduction
        DeductionDTO deductionDTO = deductionMapper.toDto(deduction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeductionMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deductionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Deduction in the database
        List<Deduction> deductionList = deductionRepository.findAll();
        assertThat(deductionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDeductionWithPatch() throws Exception {
        // Initialize the database
        deductionRepository.saveAndFlush(deduction);

        int databaseSizeBeforeUpdate = deductionRepository.findAll().size();

        // Update the deduction using partial update
        Deduction partialUpdatedDeduction = new Deduction();
        partialUpdatedDeduction.setId(deduction.getId());

        partialUpdatedDeduction.moment(UPDATED_MOMENT).reason(UPDATED_REASON);

        restDeductionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDeduction.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDeduction))
            )
            .andExpect(status().isOk());

        // Validate the Deduction in the database
        List<Deduction> deductionList = deductionRepository.findAll();
        assertThat(deductionList).hasSize(databaseSizeBeforeUpdate);
        Deduction testDeduction = deductionList.get(deductionList.size() - 1);
        assertThat(testDeduction.getPoints()).isEqualTo(DEFAULT_POINTS);
        assertThat(testDeduction.getMoment()).isEqualTo(UPDATED_MOMENT);
        assertThat(testDeduction.getReason()).isEqualTo(UPDATED_REASON);
    }

    @Test
    @Transactional
    void fullUpdateDeductionWithPatch() throws Exception {
        // Initialize the database
        deductionRepository.saveAndFlush(deduction);

        int databaseSizeBeforeUpdate = deductionRepository.findAll().size();

        // Update the deduction using partial update
        Deduction partialUpdatedDeduction = new Deduction();
        partialUpdatedDeduction.setId(deduction.getId());

        partialUpdatedDeduction.points(UPDATED_POINTS).moment(UPDATED_MOMENT).reason(UPDATED_REASON);

        restDeductionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDeduction.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDeduction))
            )
            .andExpect(status().isOk());

        // Validate the Deduction in the database
        List<Deduction> deductionList = deductionRepository.findAll();
        assertThat(deductionList).hasSize(databaseSizeBeforeUpdate);
        Deduction testDeduction = deductionList.get(deductionList.size() - 1);
        assertThat(testDeduction.getPoints()).isEqualTo(UPDATED_POINTS);
        assertThat(testDeduction.getMoment()).isEqualTo(UPDATED_MOMENT);
        assertThat(testDeduction.getReason()).isEqualTo(UPDATED_REASON);
    }

    @Test
    @Transactional
    void patchNonExistingDeduction() throws Exception {
        int databaseSizeBeforeUpdate = deductionRepository.findAll().size();
        deduction.setId(count.incrementAndGet());

        // Create the Deduction
        DeductionDTO deductionDTO = deductionMapper.toDto(deduction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeductionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, deductionDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(deductionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Deduction in the database
        List<Deduction> deductionList = deductionRepository.findAll();
        assertThat(deductionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDeduction() throws Exception {
        int databaseSizeBeforeUpdate = deductionRepository.findAll().size();
        deduction.setId(count.incrementAndGet());

        // Create the Deduction
        DeductionDTO deductionDTO = deductionMapper.toDto(deduction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeductionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(deductionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Deduction in the database
        List<Deduction> deductionList = deductionRepository.findAll();
        assertThat(deductionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDeduction() throws Exception {
        int databaseSizeBeforeUpdate = deductionRepository.findAll().size();
        deduction.setId(count.incrementAndGet());

        // Create the Deduction
        DeductionDTO deductionDTO = deductionMapper.toDto(deduction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeductionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(deductionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Deduction in the database
        List<Deduction> deductionList = deductionRepository.findAll();
        assertThat(deductionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDeduction() throws Exception {
        // Initialize the database
        deductionRepository.saveAndFlush(deduction);

        int databaseSizeBeforeDelete = deductionRepository.findAll().size();

        // Delete the deduction
        restDeductionMockMvc
            .perform(delete(ENTITY_API_URL_ID, deduction.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Deduction> deductionList = deductionRepository.findAll();
        assertThat(deductionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
