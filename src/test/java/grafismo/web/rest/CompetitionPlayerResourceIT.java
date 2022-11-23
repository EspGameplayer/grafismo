package grafismo.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import grafismo.IntegrationTest;
import grafismo.domain.Competition;
import grafismo.domain.CompetitionPlayer;
import grafismo.domain.Player;
import grafismo.repository.CompetitionPlayerRepository;
import grafismo.service.CompetitionPlayerService;
import grafismo.service.dto.CompetitionPlayerDTO;
import grafismo.service.mapper.CompetitionPlayerMapper;
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
 * Integration tests for the {@link CompetitionPlayerResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CompetitionPlayerResourceIT {

    private static final Integer DEFAULT_PREFERRED_SHIRT_NUMBER = 0;
    private static final Integer UPDATED_PREFERRED_SHIRT_NUMBER = 1;

    private static final String ENTITY_API_URL = "/api/competition-players";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CompetitionPlayerRepository competitionPlayerRepository;

    @Mock
    private CompetitionPlayerRepository competitionPlayerRepositoryMock;

    @Autowired
    private CompetitionPlayerMapper competitionPlayerMapper;

    @Mock
    private CompetitionPlayerService competitionPlayerServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCompetitionPlayerMockMvc;

    private CompetitionPlayer competitionPlayer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CompetitionPlayer createEntity(EntityManager em) {
        CompetitionPlayer competitionPlayer = new CompetitionPlayer().preferredShirtNumber(DEFAULT_PREFERRED_SHIRT_NUMBER);
        // Add required entity
        Player player;
        if (TestUtil.findAll(em, Player.class).isEmpty()) {
            player = PlayerResourceIT.createEntity(em);
            em.persist(player);
            em.flush();
        } else {
            player = TestUtil.findAll(em, Player.class).get(0);
        }
        competitionPlayer.setPlayer(player);
        // Add required entity
        Competition competition;
        if (TestUtil.findAll(em, Competition.class).isEmpty()) {
            competition = CompetitionResourceIT.createEntity(em);
            em.persist(competition);
            em.flush();
        } else {
            competition = TestUtil.findAll(em, Competition.class).get(0);
        }
        competitionPlayer.setCompetition(competition);
        return competitionPlayer;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CompetitionPlayer createUpdatedEntity(EntityManager em) {
        CompetitionPlayer competitionPlayer = new CompetitionPlayer().preferredShirtNumber(UPDATED_PREFERRED_SHIRT_NUMBER);
        // Add required entity
        Player player;
        if (TestUtil.findAll(em, Player.class).isEmpty()) {
            player = PlayerResourceIT.createUpdatedEntity(em);
            em.persist(player);
            em.flush();
        } else {
            player = TestUtil.findAll(em, Player.class).get(0);
        }
        competitionPlayer.setPlayer(player);
        // Add required entity
        Competition competition;
        if (TestUtil.findAll(em, Competition.class).isEmpty()) {
            competition = CompetitionResourceIT.createUpdatedEntity(em);
            em.persist(competition);
            em.flush();
        } else {
            competition = TestUtil.findAll(em, Competition.class).get(0);
        }
        competitionPlayer.setCompetition(competition);
        return competitionPlayer;
    }

    @BeforeEach
    public void initTest() {
        competitionPlayer = createEntity(em);
    }

    @Test
    @Transactional
    void createCompetitionPlayer() throws Exception {
        int databaseSizeBeforeCreate = competitionPlayerRepository.findAll().size();
        // Create the CompetitionPlayer
        CompetitionPlayerDTO competitionPlayerDTO = competitionPlayerMapper.toDto(competitionPlayer);
        restCompetitionPlayerMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(competitionPlayerDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CompetitionPlayer in the database
        List<CompetitionPlayer> competitionPlayerList = competitionPlayerRepository.findAll();
        assertThat(competitionPlayerList).hasSize(databaseSizeBeforeCreate + 1);
        CompetitionPlayer testCompetitionPlayer = competitionPlayerList.get(competitionPlayerList.size() - 1);
        assertThat(testCompetitionPlayer.getPreferredShirtNumber()).isEqualTo(DEFAULT_PREFERRED_SHIRT_NUMBER);
    }

    @Test
    @Transactional
    void createCompetitionPlayerWithExistingId() throws Exception {
        // Create the CompetitionPlayer with an existing ID
        competitionPlayer.setId(1L);
        CompetitionPlayerDTO competitionPlayerDTO = competitionPlayerMapper.toDto(competitionPlayer);

        int databaseSizeBeforeCreate = competitionPlayerRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCompetitionPlayerMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(competitionPlayerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompetitionPlayer in the database
        List<CompetitionPlayer> competitionPlayerList = competitionPlayerRepository.findAll();
        assertThat(competitionPlayerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCompetitionPlayers() throws Exception {
        // Initialize the database
        competitionPlayerRepository.saveAndFlush(competitionPlayer);

        // Get all the competitionPlayerList
        restCompetitionPlayerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(competitionPlayer.getId().intValue())))
            .andExpect(jsonPath("$.[*].preferredShirtNumber").value(hasItem(DEFAULT_PREFERRED_SHIRT_NUMBER)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCompetitionPlayersWithEagerRelationshipsIsEnabled() throws Exception {
        when(competitionPlayerServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCompetitionPlayerMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(competitionPlayerServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCompetitionPlayersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(competitionPlayerServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCompetitionPlayerMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(competitionPlayerServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getCompetitionPlayer() throws Exception {
        // Initialize the database
        competitionPlayerRepository.saveAndFlush(competitionPlayer);

        // Get the competitionPlayer
        restCompetitionPlayerMockMvc
            .perform(get(ENTITY_API_URL_ID, competitionPlayer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(competitionPlayer.getId().intValue()))
            .andExpect(jsonPath("$.preferredShirtNumber").value(DEFAULT_PREFERRED_SHIRT_NUMBER));
    }

    @Test
    @Transactional
    void getNonExistingCompetitionPlayer() throws Exception {
        // Get the competitionPlayer
        restCompetitionPlayerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCompetitionPlayer() throws Exception {
        // Initialize the database
        competitionPlayerRepository.saveAndFlush(competitionPlayer);

        int databaseSizeBeforeUpdate = competitionPlayerRepository.findAll().size();

        // Update the competitionPlayer
        CompetitionPlayer updatedCompetitionPlayer = competitionPlayerRepository.findById(competitionPlayer.getId()).get();
        // Disconnect from session so that the updates on updatedCompetitionPlayer are not directly saved in db
        em.detach(updatedCompetitionPlayer);
        updatedCompetitionPlayer.preferredShirtNumber(UPDATED_PREFERRED_SHIRT_NUMBER);
        CompetitionPlayerDTO competitionPlayerDTO = competitionPlayerMapper.toDto(updatedCompetitionPlayer);

        restCompetitionPlayerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, competitionPlayerDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(competitionPlayerDTO))
            )
            .andExpect(status().isOk());

        // Validate the CompetitionPlayer in the database
        List<CompetitionPlayer> competitionPlayerList = competitionPlayerRepository.findAll();
        assertThat(competitionPlayerList).hasSize(databaseSizeBeforeUpdate);
        CompetitionPlayer testCompetitionPlayer = competitionPlayerList.get(competitionPlayerList.size() - 1);
        assertThat(testCompetitionPlayer.getPreferredShirtNumber()).isEqualTo(UPDATED_PREFERRED_SHIRT_NUMBER);
    }

    @Test
    @Transactional
    void putNonExistingCompetitionPlayer() throws Exception {
        int databaseSizeBeforeUpdate = competitionPlayerRepository.findAll().size();
        competitionPlayer.setId(count.incrementAndGet());

        // Create the CompetitionPlayer
        CompetitionPlayerDTO competitionPlayerDTO = competitionPlayerMapper.toDto(competitionPlayer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompetitionPlayerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, competitionPlayerDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(competitionPlayerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompetitionPlayer in the database
        List<CompetitionPlayer> competitionPlayerList = competitionPlayerRepository.findAll();
        assertThat(competitionPlayerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCompetitionPlayer() throws Exception {
        int databaseSizeBeforeUpdate = competitionPlayerRepository.findAll().size();
        competitionPlayer.setId(count.incrementAndGet());

        // Create the CompetitionPlayer
        CompetitionPlayerDTO competitionPlayerDTO = competitionPlayerMapper.toDto(competitionPlayer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompetitionPlayerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(competitionPlayerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompetitionPlayer in the database
        List<CompetitionPlayer> competitionPlayerList = competitionPlayerRepository.findAll();
        assertThat(competitionPlayerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCompetitionPlayer() throws Exception {
        int databaseSizeBeforeUpdate = competitionPlayerRepository.findAll().size();
        competitionPlayer.setId(count.incrementAndGet());

        // Create the CompetitionPlayer
        CompetitionPlayerDTO competitionPlayerDTO = competitionPlayerMapper.toDto(competitionPlayer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompetitionPlayerMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(competitionPlayerDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CompetitionPlayer in the database
        List<CompetitionPlayer> competitionPlayerList = competitionPlayerRepository.findAll();
        assertThat(competitionPlayerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCompetitionPlayerWithPatch() throws Exception {
        // Initialize the database
        competitionPlayerRepository.saveAndFlush(competitionPlayer);

        int databaseSizeBeforeUpdate = competitionPlayerRepository.findAll().size();

        // Update the competitionPlayer using partial update
        CompetitionPlayer partialUpdatedCompetitionPlayer = new CompetitionPlayer();
        partialUpdatedCompetitionPlayer.setId(competitionPlayer.getId());

        restCompetitionPlayerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompetitionPlayer.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCompetitionPlayer))
            )
            .andExpect(status().isOk());

        // Validate the CompetitionPlayer in the database
        List<CompetitionPlayer> competitionPlayerList = competitionPlayerRepository.findAll();
        assertThat(competitionPlayerList).hasSize(databaseSizeBeforeUpdate);
        CompetitionPlayer testCompetitionPlayer = competitionPlayerList.get(competitionPlayerList.size() - 1);
        assertThat(testCompetitionPlayer.getPreferredShirtNumber()).isEqualTo(DEFAULT_PREFERRED_SHIRT_NUMBER);
    }

    @Test
    @Transactional
    void fullUpdateCompetitionPlayerWithPatch() throws Exception {
        // Initialize the database
        competitionPlayerRepository.saveAndFlush(competitionPlayer);

        int databaseSizeBeforeUpdate = competitionPlayerRepository.findAll().size();

        // Update the competitionPlayer using partial update
        CompetitionPlayer partialUpdatedCompetitionPlayer = new CompetitionPlayer();
        partialUpdatedCompetitionPlayer.setId(competitionPlayer.getId());

        partialUpdatedCompetitionPlayer.preferredShirtNumber(UPDATED_PREFERRED_SHIRT_NUMBER);

        restCompetitionPlayerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompetitionPlayer.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCompetitionPlayer))
            )
            .andExpect(status().isOk());

        // Validate the CompetitionPlayer in the database
        List<CompetitionPlayer> competitionPlayerList = competitionPlayerRepository.findAll();
        assertThat(competitionPlayerList).hasSize(databaseSizeBeforeUpdate);
        CompetitionPlayer testCompetitionPlayer = competitionPlayerList.get(competitionPlayerList.size() - 1);
        assertThat(testCompetitionPlayer.getPreferredShirtNumber()).isEqualTo(UPDATED_PREFERRED_SHIRT_NUMBER);
    }

    @Test
    @Transactional
    void patchNonExistingCompetitionPlayer() throws Exception {
        int databaseSizeBeforeUpdate = competitionPlayerRepository.findAll().size();
        competitionPlayer.setId(count.incrementAndGet());

        // Create the CompetitionPlayer
        CompetitionPlayerDTO competitionPlayerDTO = competitionPlayerMapper.toDto(competitionPlayer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompetitionPlayerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, competitionPlayerDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(competitionPlayerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompetitionPlayer in the database
        List<CompetitionPlayer> competitionPlayerList = competitionPlayerRepository.findAll();
        assertThat(competitionPlayerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCompetitionPlayer() throws Exception {
        int databaseSizeBeforeUpdate = competitionPlayerRepository.findAll().size();
        competitionPlayer.setId(count.incrementAndGet());

        // Create the CompetitionPlayer
        CompetitionPlayerDTO competitionPlayerDTO = competitionPlayerMapper.toDto(competitionPlayer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompetitionPlayerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(competitionPlayerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompetitionPlayer in the database
        List<CompetitionPlayer> competitionPlayerList = competitionPlayerRepository.findAll();
        assertThat(competitionPlayerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCompetitionPlayer() throws Exception {
        int databaseSizeBeforeUpdate = competitionPlayerRepository.findAll().size();
        competitionPlayer.setId(count.incrementAndGet());

        // Create the CompetitionPlayer
        CompetitionPlayerDTO competitionPlayerDTO = competitionPlayerMapper.toDto(competitionPlayer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompetitionPlayerMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(competitionPlayerDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CompetitionPlayer in the database
        List<CompetitionPlayer> competitionPlayerList = competitionPlayerRepository.findAll();
        assertThat(competitionPlayerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCompetitionPlayer() throws Exception {
        // Initialize the database
        competitionPlayerRepository.saveAndFlush(competitionPlayer);

        int databaseSizeBeforeDelete = competitionPlayerRepository.findAll().size();

        // Delete the competitionPlayer
        restCompetitionPlayerMockMvc
            .perform(delete(ENTITY_API_URL_ID, competitionPlayer.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CompetitionPlayer> competitionPlayerList = competitionPlayerRepository.findAll();
        assertThat(competitionPlayerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
