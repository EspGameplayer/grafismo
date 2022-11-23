package grafismo.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import grafismo.IntegrationTest;
import grafismo.domain.Competition;
import grafismo.domain.Person;
import grafismo.domain.Suspension;
import grafismo.repository.SuspensionRepository;
import grafismo.service.SuspensionService;
import grafismo.service.dto.SuspensionDTO;
import grafismo.service.mapper.SuspensionMapper;
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
 * Integration tests for the {@link SuspensionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SuspensionResourceIT {

    private static final Integer DEFAULT_MATCHES = 1;
    private static final Integer UPDATED_MATCHES = 2;

    private static final Instant DEFAULT_MOMENT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_MOMENT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_REASON = "AAAAAAAAAA";
    private static final String UPDATED_REASON = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/suspensions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SuspensionRepository suspensionRepository;

    @Mock
    private SuspensionRepository suspensionRepositoryMock;

    @Autowired
    private SuspensionMapper suspensionMapper;

    @Mock
    private SuspensionService suspensionServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSuspensionMockMvc;

    private Suspension suspension;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Suspension createEntity(EntityManager em) {
        Suspension suspension = new Suspension().matches(DEFAULT_MATCHES).moment(DEFAULT_MOMENT).reason(DEFAULT_REASON);
        // Add required entity
        Person person;
        if (TestUtil.findAll(em, Person.class).isEmpty()) {
            person = PersonResourceIT.createEntity(em);
            em.persist(person);
            em.flush();
        } else {
            person = TestUtil.findAll(em, Person.class).get(0);
        }
        suspension.setPerson(person);
        // Add required entity
        Competition competition;
        if (TestUtil.findAll(em, Competition.class).isEmpty()) {
            competition = CompetitionResourceIT.createEntity(em);
            em.persist(competition);
            em.flush();
        } else {
            competition = TestUtil.findAll(em, Competition.class).get(0);
        }
        suspension.setCompetition(competition);
        return suspension;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Suspension createUpdatedEntity(EntityManager em) {
        Suspension suspension = new Suspension().matches(UPDATED_MATCHES).moment(UPDATED_MOMENT).reason(UPDATED_REASON);
        // Add required entity
        Person person;
        if (TestUtil.findAll(em, Person.class).isEmpty()) {
            person = PersonResourceIT.createUpdatedEntity(em);
            em.persist(person);
            em.flush();
        } else {
            person = TestUtil.findAll(em, Person.class).get(0);
        }
        suspension.setPerson(person);
        // Add required entity
        Competition competition;
        if (TestUtil.findAll(em, Competition.class).isEmpty()) {
            competition = CompetitionResourceIT.createUpdatedEntity(em);
            em.persist(competition);
            em.flush();
        } else {
            competition = TestUtil.findAll(em, Competition.class).get(0);
        }
        suspension.setCompetition(competition);
        return suspension;
    }

    @BeforeEach
    public void initTest() {
        suspension = createEntity(em);
    }

    @Test
    @Transactional
    void createSuspension() throws Exception {
        int databaseSizeBeforeCreate = suspensionRepository.findAll().size();
        // Create the Suspension
        SuspensionDTO suspensionDTO = suspensionMapper.toDto(suspension);
        restSuspensionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(suspensionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Suspension in the database
        List<Suspension> suspensionList = suspensionRepository.findAll();
        assertThat(suspensionList).hasSize(databaseSizeBeforeCreate + 1);
        Suspension testSuspension = suspensionList.get(suspensionList.size() - 1);
        assertThat(testSuspension.getMatches()).isEqualTo(DEFAULT_MATCHES);
        assertThat(testSuspension.getMoment()).isEqualTo(DEFAULT_MOMENT);
        assertThat(testSuspension.getReason()).isEqualTo(DEFAULT_REASON);
    }

    @Test
    @Transactional
    void createSuspensionWithExistingId() throws Exception {
        // Create the Suspension with an existing ID
        suspension.setId(1L);
        SuspensionDTO suspensionDTO = suspensionMapper.toDto(suspension);

        int databaseSizeBeforeCreate = suspensionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSuspensionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(suspensionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Suspension in the database
        List<Suspension> suspensionList = suspensionRepository.findAll();
        assertThat(suspensionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSuspensions() throws Exception {
        // Initialize the database
        suspensionRepository.saveAndFlush(suspension);

        // Get all the suspensionList
        restSuspensionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(suspension.getId().intValue())))
            .andExpect(jsonPath("$.[*].matches").value(hasItem(DEFAULT_MATCHES)))
            .andExpect(jsonPath("$.[*].moment").value(hasItem(DEFAULT_MOMENT.toString())))
            .andExpect(jsonPath("$.[*].reason").value(hasItem(DEFAULT_REASON)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSuspensionsWithEagerRelationshipsIsEnabled() throws Exception {
        when(suspensionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSuspensionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(suspensionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSuspensionsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(suspensionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSuspensionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(suspensionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getSuspension() throws Exception {
        // Initialize the database
        suspensionRepository.saveAndFlush(suspension);

        // Get the suspension
        restSuspensionMockMvc
            .perform(get(ENTITY_API_URL_ID, suspension.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(suspension.getId().intValue()))
            .andExpect(jsonPath("$.matches").value(DEFAULT_MATCHES))
            .andExpect(jsonPath("$.moment").value(DEFAULT_MOMENT.toString()))
            .andExpect(jsonPath("$.reason").value(DEFAULT_REASON));
    }

    @Test
    @Transactional
    void getNonExistingSuspension() throws Exception {
        // Get the suspension
        restSuspensionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSuspension() throws Exception {
        // Initialize the database
        suspensionRepository.saveAndFlush(suspension);

        int databaseSizeBeforeUpdate = suspensionRepository.findAll().size();

        // Update the suspension
        Suspension updatedSuspension = suspensionRepository.findById(suspension.getId()).get();
        // Disconnect from session so that the updates on updatedSuspension are not directly saved in db
        em.detach(updatedSuspension);
        updatedSuspension.matches(UPDATED_MATCHES).moment(UPDATED_MOMENT).reason(UPDATED_REASON);
        SuspensionDTO suspensionDTO = suspensionMapper.toDto(updatedSuspension);

        restSuspensionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, suspensionDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(suspensionDTO))
            )
            .andExpect(status().isOk());

        // Validate the Suspension in the database
        List<Suspension> suspensionList = suspensionRepository.findAll();
        assertThat(suspensionList).hasSize(databaseSizeBeforeUpdate);
        Suspension testSuspension = suspensionList.get(suspensionList.size() - 1);
        assertThat(testSuspension.getMatches()).isEqualTo(UPDATED_MATCHES);
        assertThat(testSuspension.getMoment()).isEqualTo(UPDATED_MOMENT);
        assertThat(testSuspension.getReason()).isEqualTo(UPDATED_REASON);
    }

    @Test
    @Transactional
    void putNonExistingSuspension() throws Exception {
        int databaseSizeBeforeUpdate = suspensionRepository.findAll().size();
        suspension.setId(count.incrementAndGet());

        // Create the Suspension
        SuspensionDTO suspensionDTO = suspensionMapper.toDto(suspension);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSuspensionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, suspensionDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(suspensionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Suspension in the database
        List<Suspension> suspensionList = suspensionRepository.findAll();
        assertThat(suspensionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSuspension() throws Exception {
        int databaseSizeBeforeUpdate = suspensionRepository.findAll().size();
        suspension.setId(count.incrementAndGet());

        // Create the Suspension
        SuspensionDTO suspensionDTO = suspensionMapper.toDto(suspension);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSuspensionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(suspensionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Suspension in the database
        List<Suspension> suspensionList = suspensionRepository.findAll();
        assertThat(suspensionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSuspension() throws Exception {
        int databaseSizeBeforeUpdate = suspensionRepository.findAll().size();
        suspension.setId(count.incrementAndGet());

        // Create the Suspension
        SuspensionDTO suspensionDTO = suspensionMapper.toDto(suspension);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSuspensionMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(suspensionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Suspension in the database
        List<Suspension> suspensionList = suspensionRepository.findAll();
        assertThat(suspensionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSuspensionWithPatch() throws Exception {
        // Initialize the database
        suspensionRepository.saveAndFlush(suspension);

        int databaseSizeBeforeUpdate = suspensionRepository.findAll().size();

        // Update the suspension using partial update
        Suspension partialUpdatedSuspension = new Suspension();
        partialUpdatedSuspension.setId(suspension.getId());

        partialUpdatedSuspension.moment(UPDATED_MOMENT);

        restSuspensionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSuspension.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSuspension))
            )
            .andExpect(status().isOk());

        // Validate the Suspension in the database
        List<Suspension> suspensionList = suspensionRepository.findAll();
        assertThat(suspensionList).hasSize(databaseSizeBeforeUpdate);
        Suspension testSuspension = suspensionList.get(suspensionList.size() - 1);
        assertThat(testSuspension.getMatches()).isEqualTo(DEFAULT_MATCHES);
        assertThat(testSuspension.getMoment()).isEqualTo(UPDATED_MOMENT);
        assertThat(testSuspension.getReason()).isEqualTo(DEFAULT_REASON);
    }

    @Test
    @Transactional
    void fullUpdateSuspensionWithPatch() throws Exception {
        // Initialize the database
        suspensionRepository.saveAndFlush(suspension);

        int databaseSizeBeforeUpdate = suspensionRepository.findAll().size();

        // Update the suspension using partial update
        Suspension partialUpdatedSuspension = new Suspension();
        partialUpdatedSuspension.setId(suspension.getId());

        partialUpdatedSuspension.matches(UPDATED_MATCHES).moment(UPDATED_MOMENT).reason(UPDATED_REASON);

        restSuspensionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSuspension.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSuspension))
            )
            .andExpect(status().isOk());

        // Validate the Suspension in the database
        List<Suspension> suspensionList = suspensionRepository.findAll();
        assertThat(suspensionList).hasSize(databaseSizeBeforeUpdate);
        Suspension testSuspension = suspensionList.get(suspensionList.size() - 1);
        assertThat(testSuspension.getMatches()).isEqualTo(UPDATED_MATCHES);
        assertThat(testSuspension.getMoment()).isEqualTo(UPDATED_MOMENT);
        assertThat(testSuspension.getReason()).isEqualTo(UPDATED_REASON);
    }

    @Test
    @Transactional
    void patchNonExistingSuspension() throws Exception {
        int databaseSizeBeforeUpdate = suspensionRepository.findAll().size();
        suspension.setId(count.incrementAndGet());

        // Create the Suspension
        SuspensionDTO suspensionDTO = suspensionMapper.toDto(suspension);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSuspensionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, suspensionDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(suspensionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Suspension in the database
        List<Suspension> suspensionList = suspensionRepository.findAll();
        assertThat(suspensionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSuspension() throws Exception {
        int databaseSizeBeforeUpdate = suspensionRepository.findAll().size();
        suspension.setId(count.incrementAndGet());

        // Create the Suspension
        SuspensionDTO suspensionDTO = suspensionMapper.toDto(suspension);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSuspensionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(suspensionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Suspension in the database
        List<Suspension> suspensionList = suspensionRepository.findAll();
        assertThat(suspensionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSuspension() throws Exception {
        int databaseSizeBeforeUpdate = suspensionRepository.findAll().size();
        suspension.setId(count.incrementAndGet());

        // Create the Suspension
        SuspensionDTO suspensionDTO = suspensionMapper.toDto(suspension);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSuspensionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(suspensionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Suspension in the database
        List<Suspension> suspensionList = suspensionRepository.findAll();
        assertThat(suspensionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSuspension() throws Exception {
        // Initialize the database
        suspensionRepository.saveAndFlush(suspension);

        int databaseSizeBeforeDelete = suspensionRepository.findAll().size();

        // Delete the suspension
        restSuspensionMockMvc
            .perform(delete(ENTITY_API_URL_ID, suspension.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Suspension> suspensionList = suspensionRepository.findAll();
        assertThat(suspensionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
