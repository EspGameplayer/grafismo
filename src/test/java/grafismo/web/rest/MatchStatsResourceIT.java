package grafismo.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import grafismo.IntegrationTest;
import grafismo.domain.Match;
import grafismo.domain.MatchStats;
import grafismo.repository.MatchStatsRepository;
import grafismo.service.dto.MatchStatsDTO;
import grafismo.service.mapper.MatchStatsMapper;
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
 * Integration tests for the {@link MatchStatsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MatchStatsResourceIT {

    private static final Integer DEFAULT_HOME_POSSESSION_TIME = 0;
    private static final Integer UPDATED_HOME_POSSESSION_TIME = 1;

    private static final Integer DEFAULT_AWAY_POSSESSION_TIME = 0;
    private static final Integer UPDATED_AWAY_POSSESSION_TIME = 1;

    private static final Integer DEFAULT_IN_CONTEST_POSSESSION_TIME = 0;
    private static final Integer UPDATED_IN_CONTEST_POSSESSION_TIME = 1;

    private static final String ENTITY_API_URL = "/api/match-stats";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MatchStatsRepository matchStatsRepository;

    @Autowired
    private MatchStatsMapper matchStatsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMatchStatsMockMvc;

    private MatchStats matchStats;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MatchStats createEntity(EntityManager em) {
        MatchStats matchStats = new MatchStats()
            .homePossessionTime(DEFAULT_HOME_POSSESSION_TIME)
            .awayPossessionTime(DEFAULT_AWAY_POSSESSION_TIME)
            .inContestPossessionTime(DEFAULT_IN_CONTEST_POSSESSION_TIME);
        // Add required entity
        Match match;
        if (TestUtil.findAll(em, Match.class).isEmpty()) {
            match = MatchResourceIT.createEntity(em);
            em.persist(match);
            em.flush();
        } else {
            match = TestUtil.findAll(em, Match.class).get(0);
        }
        matchStats.setMatch(match);
        return matchStats;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MatchStats createUpdatedEntity(EntityManager em) {
        MatchStats matchStats = new MatchStats()
            .homePossessionTime(UPDATED_HOME_POSSESSION_TIME)
            .awayPossessionTime(UPDATED_AWAY_POSSESSION_TIME)
            .inContestPossessionTime(UPDATED_IN_CONTEST_POSSESSION_TIME);
        // Add required entity
        Match match;
        if (TestUtil.findAll(em, Match.class).isEmpty()) {
            match = MatchResourceIT.createUpdatedEntity(em);
            em.persist(match);
            em.flush();
        } else {
            match = TestUtil.findAll(em, Match.class).get(0);
        }
        matchStats.setMatch(match);
        return matchStats;
    }

    @BeforeEach
    public void initTest() {
        matchStats = createEntity(em);
    }

    @Test
    @Transactional
    void createMatchStats() throws Exception {
        int databaseSizeBeforeCreate = matchStatsRepository.findAll().size();
        // Create the MatchStats
        MatchStatsDTO matchStatsDTO = matchStatsMapper.toDto(matchStats);
        restMatchStatsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(matchStatsDTO))
            )
            .andExpect(status().isCreated());

        // Validate the MatchStats in the database
        List<MatchStats> matchStatsList = matchStatsRepository.findAll();
        assertThat(matchStatsList).hasSize(databaseSizeBeforeCreate + 1);
        MatchStats testMatchStats = matchStatsList.get(matchStatsList.size() - 1);
        assertThat(testMatchStats.getHomePossessionTime()).isEqualTo(DEFAULT_HOME_POSSESSION_TIME);
        assertThat(testMatchStats.getAwayPossessionTime()).isEqualTo(DEFAULT_AWAY_POSSESSION_TIME);
        assertThat(testMatchStats.getInContestPossessionTime()).isEqualTo(DEFAULT_IN_CONTEST_POSSESSION_TIME);
    }

    @Test
    @Transactional
    void createMatchStatsWithExistingId() throws Exception {
        // Create the MatchStats with an existing ID
        matchStats.setId(1L);
        MatchStatsDTO matchStatsDTO = matchStatsMapper.toDto(matchStats);

        int databaseSizeBeforeCreate = matchStatsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMatchStatsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(matchStatsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MatchStats in the database
        List<MatchStats> matchStatsList = matchStatsRepository.findAll();
        assertThat(matchStatsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMatchStats() throws Exception {
        // Initialize the database
        matchStatsRepository.saveAndFlush(matchStats);

        // Get all the matchStatsList
        restMatchStatsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(matchStats.getId().intValue())))
            .andExpect(jsonPath("$.[*].homePossessionTime").value(hasItem(DEFAULT_HOME_POSSESSION_TIME)))
            .andExpect(jsonPath("$.[*].awayPossessionTime").value(hasItem(DEFAULT_AWAY_POSSESSION_TIME)))
            .andExpect(jsonPath("$.[*].inContestPossessionTime").value(hasItem(DEFAULT_IN_CONTEST_POSSESSION_TIME)));
    }

    @Test
    @Transactional
    void getMatchStats() throws Exception {
        // Initialize the database
        matchStatsRepository.saveAndFlush(matchStats);

        // Get the matchStats
        restMatchStatsMockMvc
            .perform(get(ENTITY_API_URL_ID, matchStats.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(matchStats.getId().intValue()))
            .andExpect(jsonPath("$.homePossessionTime").value(DEFAULT_HOME_POSSESSION_TIME))
            .andExpect(jsonPath("$.awayPossessionTime").value(DEFAULT_AWAY_POSSESSION_TIME))
            .andExpect(jsonPath("$.inContestPossessionTime").value(DEFAULT_IN_CONTEST_POSSESSION_TIME));
    }

    @Test
    @Transactional
    void getNonExistingMatchStats() throws Exception {
        // Get the matchStats
        restMatchStatsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMatchStats() throws Exception {
        // Initialize the database
        matchStatsRepository.saveAndFlush(matchStats);

        int databaseSizeBeforeUpdate = matchStatsRepository.findAll().size();

        // Update the matchStats
        MatchStats updatedMatchStats = matchStatsRepository.findById(matchStats.getId()).get();
        // Disconnect from session so that the updates on updatedMatchStats are not directly saved in db
        em.detach(updatedMatchStats);
        updatedMatchStats
            .homePossessionTime(UPDATED_HOME_POSSESSION_TIME)
            .awayPossessionTime(UPDATED_AWAY_POSSESSION_TIME)
            .inContestPossessionTime(UPDATED_IN_CONTEST_POSSESSION_TIME);
        MatchStatsDTO matchStatsDTO = matchStatsMapper.toDto(updatedMatchStats);

        restMatchStatsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, matchStatsDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(matchStatsDTO))
            )
            .andExpect(status().isOk());

        // Validate the MatchStats in the database
        List<MatchStats> matchStatsList = matchStatsRepository.findAll();
        assertThat(matchStatsList).hasSize(databaseSizeBeforeUpdate);
        MatchStats testMatchStats = matchStatsList.get(matchStatsList.size() - 1);
        assertThat(testMatchStats.getHomePossessionTime()).isEqualTo(UPDATED_HOME_POSSESSION_TIME);
        assertThat(testMatchStats.getAwayPossessionTime()).isEqualTo(UPDATED_AWAY_POSSESSION_TIME);
        assertThat(testMatchStats.getInContestPossessionTime()).isEqualTo(UPDATED_IN_CONTEST_POSSESSION_TIME);
    }

    @Test
    @Transactional
    void putNonExistingMatchStats() throws Exception {
        int databaseSizeBeforeUpdate = matchStatsRepository.findAll().size();
        matchStats.setId(count.incrementAndGet());

        // Create the MatchStats
        MatchStatsDTO matchStatsDTO = matchStatsMapper.toDto(matchStats);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMatchStatsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, matchStatsDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(matchStatsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MatchStats in the database
        List<MatchStats> matchStatsList = matchStatsRepository.findAll();
        assertThat(matchStatsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMatchStats() throws Exception {
        int databaseSizeBeforeUpdate = matchStatsRepository.findAll().size();
        matchStats.setId(count.incrementAndGet());

        // Create the MatchStats
        MatchStatsDTO matchStatsDTO = matchStatsMapper.toDto(matchStats);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMatchStatsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(matchStatsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MatchStats in the database
        List<MatchStats> matchStatsList = matchStatsRepository.findAll();
        assertThat(matchStatsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMatchStats() throws Exception {
        int databaseSizeBeforeUpdate = matchStatsRepository.findAll().size();
        matchStats.setId(count.incrementAndGet());

        // Create the MatchStats
        MatchStatsDTO matchStatsDTO = matchStatsMapper.toDto(matchStats);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMatchStatsMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(matchStatsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MatchStats in the database
        List<MatchStats> matchStatsList = matchStatsRepository.findAll();
        assertThat(matchStatsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMatchStatsWithPatch() throws Exception {
        // Initialize the database
        matchStatsRepository.saveAndFlush(matchStats);

        int databaseSizeBeforeUpdate = matchStatsRepository.findAll().size();

        // Update the matchStats using partial update
        MatchStats partialUpdatedMatchStats = new MatchStats();
        partialUpdatedMatchStats.setId(matchStats.getId());

        partialUpdatedMatchStats.homePossessionTime(UPDATED_HOME_POSSESSION_TIME).awayPossessionTime(UPDATED_AWAY_POSSESSION_TIME);

        restMatchStatsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMatchStats.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMatchStats))
            )
            .andExpect(status().isOk());

        // Validate the MatchStats in the database
        List<MatchStats> matchStatsList = matchStatsRepository.findAll();
        assertThat(matchStatsList).hasSize(databaseSizeBeforeUpdate);
        MatchStats testMatchStats = matchStatsList.get(matchStatsList.size() - 1);
        assertThat(testMatchStats.getHomePossessionTime()).isEqualTo(UPDATED_HOME_POSSESSION_TIME);
        assertThat(testMatchStats.getAwayPossessionTime()).isEqualTo(UPDATED_AWAY_POSSESSION_TIME);
        assertThat(testMatchStats.getInContestPossessionTime()).isEqualTo(DEFAULT_IN_CONTEST_POSSESSION_TIME);
    }

    @Test
    @Transactional
    void fullUpdateMatchStatsWithPatch() throws Exception {
        // Initialize the database
        matchStatsRepository.saveAndFlush(matchStats);

        int databaseSizeBeforeUpdate = matchStatsRepository.findAll().size();

        // Update the matchStats using partial update
        MatchStats partialUpdatedMatchStats = new MatchStats();
        partialUpdatedMatchStats.setId(matchStats.getId());

        partialUpdatedMatchStats
            .homePossessionTime(UPDATED_HOME_POSSESSION_TIME)
            .awayPossessionTime(UPDATED_AWAY_POSSESSION_TIME)
            .inContestPossessionTime(UPDATED_IN_CONTEST_POSSESSION_TIME);

        restMatchStatsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMatchStats.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMatchStats))
            )
            .andExpect(status().isOk());

        // Validate the MatchStats in the database
        List<MatchStats> matchStatsList = matchStatsRepository.findAll();
        assertThat(matchStatsList).hasSize(databaseSizeBeforeUpdate);
        MatchStats testMatchStats = matchStatsList.get(matchStatsList.size() - 1);
        assertThat(testMatchStats.getHomePossessionTime()).isEqualTo(UPDATED_HOME_POSSESSION_TIME);
        assertThat(testMatchStats.getAwayPossessionTime()).isEqualTo(UPDATED_AWAY_POSSESSION_TIME);
        assertThat(testMatchStats.getInContestPossessionTime()).isEqualTo(UPDATED_IN_CONTEST_POSSESSION_TIME);
    }

    @Test
    @Transactional
    void patchNonExistingMatchStats() throws Exception {
        int databaseSizeBeforeUpdate = matchStatsRepository.findAll().size();
        matchStats.setId(count.incrementAndGet());

        // Create the MatchStats
        MatchStatsDTO matchStatsDTO = matchStatsMapper.toDto(matchStats);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMatchStatsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, matchStatsDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(matchStatsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MatchStats in the database
        List<MatchStats> matchStatsList = matchStatsRepository.findAll();
        assertThat(matchStatsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMatchStats() throws Exception {
        int databaseSizeBeforeUpdate = matchStatsRepository.findAll().size();
        matchStats.setId(count.incrementAndGet());

        // Create the MatchStats
        MatchStatsDTO matchStatsDTO = matchStatsMapper.toDto(matchStats);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMatchStatsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(matchStatsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MatchStats in the database
        List<MatchStats> matchStatsList = matchStatsRepository.findAll();
        assertThat(matchStatsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMatchStats() throws Exception {
        int databaseSizeBeforeUpdate = matchStatsRepository.findAll().size();
        matchStats.setId(count.incrementAndGet());

        // Create the MatchStats
        MatchStatsDTO matchStatsDTO = matchStatsMapper.toDto(matchStats);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMatchStatsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(matchStatsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MatchStats in the database
        List<MatchStats> matchStatsList = matchStatsRepository.findAll();
        assertThat(matchStatsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMatchStats() throws Exception {
        // Initialize the database
        matchStatsRepository.saveAndFlush(matchStats);

        int databaseSizeBeforeDelete = matchStatsRepository.findAll().size();

        // Delete the matchStats
        restMatchStatsMockMvc
            .perform(delete(ENTITY_API_URL_ID, matchStats.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MatchStats> matchStatsList = matchStatsRepository.findAll();
        assertThat(matchStatsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
