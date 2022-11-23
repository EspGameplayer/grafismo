package grafismo.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import grafismo.IntegrationTest;
import grafismo.domain.Callup;
import grafismo.domain.Match;
import grafismo.domain.MatchPlayer;
import grafismo.domain.Player;
import grafismo.repository.MatchPlayerRepository;
import grafismo.service.MatchPlayerService;
import grafismo.service.dto.MatchPlayerDTO;
import grafismo.service.mapper.MatchPlayerMapper;
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
 * Integration tests for the {@link MatchPlayerResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class MatchPlayerResourceIT {

    private static final Integer DEFAULT_SHIRT_NUMBER = 0;
    private static final Integer UPDATED_SHIRT_NUMBER = 1;

    private static final Integer DEFAULT_IS_WARNED = 0;
    private static final Integer UPDATED_IS_WARNED = 1;

    private static final String ENTITY_API_URL = "/api/match-players";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MatchPlayerRepository matchPlayerRepository;

    @Mock
    private MatchPlayerRepository matchPlayerRepositoryMock;

    @Autowired
    private MatchPlayerMapper matchPlayerMapper;

    @Mock
    private MatchPlayerService matchPlayerServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMatchPlayerMockMvc;

    private MatchPlayer matchPlayer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MatchPlayer createEntity(EntityManager em) {
        MatchPlayer matchPlayer = new MatchPlayer().shirtNumber(DEFAULT_SHIRT_NUMBER).isWarned(DEFAULT_IS_WARNED);
        // Add required entity
        Player player;
        if (TestUtil.findAll(em, Player.class).isEmpty()) {
            player = PlayerResourceIT.createEntity(em);
            em.persist(player);
            em.flush();
        } else {
            player = TestUtil.findAll(em, Player.class).get(0);
        }
        matchPlayer.setPlayer(player);
        // Add required entity
        Match match;
        if (TestUtil.findAll(em, Match.class).isEmpty()) {
            match = MatchResourceIT.createEntity(em);
            em.persist(match);
            em.flush();
        } else {
            match = TestUtil.findAll(em, Match.class).get(0);
        }
        matchPlayer.setMotmMatch(match);
        // Add required entity
        Callup callup;
        if (TestUtil.findAll(em, Callup.class).isEmpty()) {
            callup = CallupResourceIT.createEntity(em);
            em.persist(callup);
            em.flush();
        } else {
            callup = TestUtil.findAll(em, Callup.class).get(0);
        }
        matchPlayer.setCaptainCallup(callup);
        return matchPlayer;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MatchPlayer createUpdatedEntity(EntityManager em) {
        MatchPlayer matchPlayer = new MatchPlayer().shirtNumber(UPDATED_SHIRT_NUMBER).isWarned(UPDATED_IS_WARNED);
        // Add required entity
        Player player;
        if (TestUtil.findAll(em, Player.class).isEmpty()) {
            player = PlayerResourceIT.createUpdatedEntity(em);
            em.persist(player);
            em.flush();
        } else {
            player = TestUtil.findAll(em, Player.class).get(0);
        }
        matchPlayer.setPlayer(player);
        // Add required entity
        Match match;
        if (TestUtil.findAll(em, Match.class).isEmpty()) {
            match = MatchResourceIT.createUpdatedEntity(em);
            em.persist(match);
            em.flush();
        } else {
            match = TestUtil.findAll(em, Match.class).get(0);
        }
        matchPlayer.setMotmMatch(match);
        // Add required entity
        Callup callup;
        if (TestUtil.findAll(em, Callup.class).isEmpty()) {
            callup = CallupResourceIT.createUpdatedEntity(em);
            em.persist(callup);
            em.flush();
        } else {
            callup = TestUtil.findAll(em, Callup.class).get(0);
        }
        matchPlayer.setCaptainCallup(callup);
        return matchPlayer;
    }

    @BeforeEach
    public void initTest() {
        matchPlayer = createEntity(em);
    }

    @Test
    @Transactional
    void createMatchPlayer() throws Exception {
        int databaseSizeBeforeCreate = matchPlayerRepository.findAll().size();
        // Create the MatchPlayer
        MatchPlayerDTO matchPlayerDTO = matchPlayerMapper.toDto(matchPlayer);
        restMatchPlayerMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(matchPlayerDTO))
            )
            .andExpect(status().isCreated());

        // Validate the MatchPlayer in the database
        List<MatchPlayer> matchPlayerList = matchPlayerRepository.findAll();
        assertThat(matchPlayerList).hasSize(databaseSizeBeforeCreate + 1);
        MatchPlayer testMatchPlayer = matchPlayerList.get(matchPlayerList.size() - 1);
        assertThat(testMatchPlayer.getShirtNumber()).isEqualTo(DEFAULT_SHIRT_NUMBER);
        assertThat(testMatchPlayer.getIsWarned()).isEqualTo(DEFAULT_IS_WARNED);
    }

    @Test
    @Transactional
    void createMatchPlayerWithExistingId() throws Exception {
        // Create the MatchPlayer with an existing ID
        matchPlayer.setId(1L);
        MatchPlayerDTO matchPlayerDTO = matchPlayerMapper.toDto(matchPlayer);

        int databaseSizeBeforeCreate = matchPlayerRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMatchPlayerMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(matchPlayerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MatchPlayer in the database
        List<MatchPlayer> matchPlayerList = matchPlayerRepository.findAll();
        assertThat(matchPlayerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMatchPlayers() throws Exception {
        // Initialize the database
        matchPlayerRepository.saveAndFlush(matchPlayer);

        // Get all the matchPlayerList
        restMatchPlayerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(matchPlayer.getId().intValue())))
            .andExpect(jsonPath("$.[*].shirtNumber").value(hasItem(DEFAULT_SHIRT_NUMBER)))
            .andExpect(jsonPath("$.[*].isWarned").value(hasItem(DEFAULT_IS_WARNED)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMatchPlayersWithEagerRelationshipsIsEnabled() throws Exception {
        when(matchPlayerServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMatchPlayerMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(matchPlayerServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMatchPlayersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(matchPlayerServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMatchPlayerMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(matchPlayerServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getMatchPlayer() throws Exception {
        // Initialize the database
        matchPlayerRepository.saveAndFlush(matchPlayer);

        // Get the matchPlayer
        restMatchPlayerMockMvc
            .perform(get(ENTITY_API_URL_ID, matchPlayer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(matchPlayer.getId().intValue()))
            .andExpect(jsonPath("$.shirtNumber").value(DEFAULT_SHIRT_NUMBER))
            .andExpect(jsonPath("$.isWarned").value(DEFAULT_IS_WARNED));
    }

    @Test
    @Transactional
    void getNonExistingMatchPlayer() throws Exception {
        // Get the matchPlayer
        restMatchPlayerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMatchPlayer() throws Exception {
        // Initialize the database
        matchPlayerRepository.saveAndFlush(matchPlayer);

        int databaseSizeBeforeUpdate = matchPlayerRepository.findAll().size();

        // Update the matchPlayer
        MatchPlayer updatedMatchPlayer = matchPlayerRepository.findById(matchPlayer.getId()).get();
        // Disconnect from session so that the updates on updatedMatchPlayer are not directly saved in db
        em.detach(updatedMatchPlayer);
        updatedMatchPlayer.shirtNumber(UPDATED_SHIRT_NUMBER).isWarned(UPDATED_IS_WARNED);
        MatchPlayerDTO matchPlayerDTO = matchPlayerMapper.toDto(updatedMatchPlayer);

        restMatchPlayerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, matchPlayerDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(matchPlayerDTO))
            )
            .andExpect(status().isOk());

        // Validate the MatchPlayer in the database
        List<MatchPlayer> matchPlayerList = matchPlayerRepository.findAll();
        assertThat(matchPlayerList).hasSize(databaseSizeBeforeUpdate);
        MatchPlayer testMatchPlayer = matchPlayerList.get(matchPlayerList.size() - 1);
        assertThat(testMatchPlayer.getShirtNumber()).isEqualTo(UPDATED_SHIRT_NUMBER);
        assertThat(testMatchPlayer.getIsWarned()).isEqualTo(UPDATED_IS_WARNED);
    }

    @Test
    @Transactional
    void putNonExistingMatchPlayer() throws Exception {
        int databaseSizeBeforeUpdate = matchPlayerRepository.findAll().size();
        matchPlayer.setId(count.incrementAndGet());

        // Create the MatchPlayer
        MatchPlayerDTO matchPlayerDTO = matchPlayerMapper.toDto(matchPlayer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMatchPlayerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, matchPlayerDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(matchPlayerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MatchPlayer in the database
        List<MatchPlayer> matchPlayerList = matchPlayerRepository.findAll();
        assertThat(matchPlayerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMatchPlayer() throws Exception {
        int databaseSizeBeforeUpdate = matchPlayerRepository.findAll().size();
        matchPlayer.setId(count.incrementAndGet());

        // Create the MatchPlayer
        MatchPlayerDTO matchPlayerDTO = matchPlayerMapper.toDto(matchPlayer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMatchPlayerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(matchPlayerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MatchPlayer in the database
        List<MatchPlayer> matchPlayerList = matchPlayerRepository.findAll();
        assertThat(matchPlayerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMatchPlayer() throws Exception {
        int databaseSizeBeforeUpdate = matchPlayerRepository.findAll().size();
        matchPlayer.setId(count.incrementAndGet());

        // Create the MatchPlayer
        MatchPlayerDTO matchPlayerDTO = matchPlayerMapper.toDto(matchPlayer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMatchPlayerMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(matchPlayerDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MatchPlayer in the database
        List<MatchPlayer> matchPlayerList = matchPlayerRepository.findAll();
        assertThat(matchPlayerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMatchPlayerWithPatch() throws Exception {
        // Initialize the database
        matchPlayerRepository.saveAndFlush(matchPlayer);

        int databaseSizeBeforeUpdate = matchPlayerRepository.findAll().size();

        // Update the matchPlayer using partial update
        MatchPlayer partialUpdatedMatchPlayer = new MatchPlayer();
        partialUpdatedMatchPlayer.setId(matchPlayer.getId());

        restMatchPlayerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMatchPlayer.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMatchPlayer))
            )
            .andExpect(status().isOk());

        // Validate the MatchPlayer in the database
        List<MatchPlayer> matchPlayerList = matchPlayerRepository.findAll();
        assertThat(matchPlayerList).hasSize(databaseSizeBeforeUpdate);
        MatchPlayer testMatchPlayer = matchPlayerList.get(matchPlayerList.size() - 1);
        assertThat(testMatchPlayer.getShirtNumber()).isEqualTo(DEFAULT_SHIRT_NUMBER);
        assertThat(testMatchPlayer.getIsWarned()).isEqualTo(DEFAULT_IS_WARNED);
    }

    @Test
    @Transactional
    void fullUpdateMatchPlayerWithPatch() throws Exception {
        // Initialize the database
        matchPlayerRepository.saveAndFlush(matchPlayer);

        int databaseSizeBeforeUpdate = matchPlayerRepository.findAll().size();

        // Update the matchPlayer using partial update
        MatchPlayer partialUpdatedMatchPlayer = new MatchPlayer();
        partialUpdatedMatchPlayer.setId(matchPlayer.getId());

        partialUpdatedMatchPlayer.shirtNumber(UPDATED_SHIRT_NUMBER).isWarned(UPDATED_IS_WARNED);

        restMatchPlayerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMatchPlayer.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMatchPlayer))
            )
            .andExpect(status().isOk());

        // Validate the MatchPlayer in the database
        List<MatchPlayer> matchPlayerList = matchPlayerRepository.findAll();
        assertThat(matchPlayerList).hasSize(databaseSizeBeforeUpdate);
        MatchPlayer testMatchPlayer = matchPlayerList.get(matchPlayerList.size() - 1);
        assertThat(testMatchPlayer.getShirtNumber()).isEqualTo(UPDATED_SHIRT_NUMBER);
        assertThat(testMatchPlayer.getIsWarned()).isEqualTo(UPDATED_IS_WARNED);
    }

    @Test
    @Transactional
    void patchNonExistingMatchPlayer() throws Exception {
        int databaseSizeBeforeUpdate = matchPlayerRepository.findAll().size();
        matchPlayer.setId(count.incrementAndGet());

        // Create the MatchPlayer
        MatchPlayerDTO matchPlayerDTO = matchPlayerMapper.toDto(matchPlayer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMatchPlayerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, matchPlayerDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(matchPlayerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MatchPlayer in the database
        List<MatchPlayer> matchPlayerList = matchPlayerRepository.findAll();
        assertThat(matchPlayerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMatchPlayer() throws Exception {
        int databaseSizeBeforeUpdate = matchPlayerRepository.findAll().size();
        matchPlayer.setId(count.incrementAndGet());

        // Create the MatchPlayer
        MatchPlayerDTO matchPlayerDTO = matchPlayerMapper.toDto(matchPlayer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMatchPlayerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(matchPlayerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MatchPlayer in the database
        List<MatchPlayer> matchPlayerList = matchPlayerRepository.findAll();
        assertThat(matchPlayerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMatchPlayer() throws Exception {
        int databaseSizeBeforeUpdate = matchPlayerRepository.findAll().size();
        matchPlayer.setId(count.incrementAndGet());

        // Create the MatchPlayer
        MatchPlayerDTO matchPlayerDTO = matchPlayerMapper.toDto(matchPlayer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMatchPlayerMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(matchPlayerDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MatchPlayer in the database
        List<MatchPlayer> matchPlayerList = matchPlayerRepository.findAll();
        assertThat(matchPlayerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMatchPlayer() throws Exception {
        // Initialize the database
        matchPlayerRepository.saveAndFlush(matchPlayer);

        int databaseSizeBeforeDelete = matchPlayerRepository.findAll().size();

        // Delete the matchPlayer
        restMatchPlayerMockMvc
            .perform(delete(ENTITY_API_URL_ID, matchPlayer.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MatchPlayer> matchPlayerList = matchPlayerRepository.findAll();
        assertThat(matchPlayerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
