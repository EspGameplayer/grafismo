package grafismo.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import grafismo.IntegrationTest;
import grafismo.domain.Callup;
import grafismo.domain.Lineup;
import grafismo.repository.LineupRepository;
import grafismo.service.LineupService;
import grafismo.service.dto.LineupDTO;
import grafismo.service.mapper.LineupMapper;
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
 * Integration tests for the {@link LineupResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class LineupResourceIT {

    private static final String ENTITY_API_URL = "/api/lineups";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LineupRepository lineupRepository;

    @Mock
    private LineupRepository lineupRepositoryMock;

    @Autowired
    private LineupMapper lineupMapper;

    @Mock
    private LineupService lineupServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLineupMockMvc;

    private Lineup lineup;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Lineup createEntity(EntityManager em) {
        Lineup lineup = new Lineup();
        // Add required entity
        Callup callup;
        if (TestUtil.findAll(em, Callup.class).isEmpty()) {
            callup = CallupResourceIT.createEntity(em);
            em.persist(callup);
            em.flush();
        } else {
            callup = TestUtil.findAll(em, Callup.class).get(0);
        }
        lineup.setCallup(callup);
        return lineup;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Lineup createUpdatedEntity(EntityManager em) {
        Lineup lineup = new Lineup();
        // Add required entity
        Callup callup;
        if (TestUtil.findAll(em, Callup.class).isEmpty()) {
            callup = CallupResourceIT.createUpdatedEntity(em);
            em.persist(callup);
            em.flush();
        } else {
            callup = TestUtil.findAll(em, Callup.class).get(0);
        }
        lineup.setCallup(callup);
        return lineup;
    }

    @BeforeEach
    public void initTest() {
        lineup = createEntity(em);
    }

    @Test
    @Transactional
    void createLineup() throws Exception {
        int databaseSizeBeforeCreate = lineupRepository.findAll().size();
        // Create the Lineup
        LineupDTO lineupDTO = lineupMapper.toDto(lineup);
        restLineupMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lineupDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Lineup in the database
        List<Lineup> lineupList = lineupRepository.findAll();
        assertThat(lineupList).hasSize(databaseSizeBeforeCreate + 1);
        Lineup testLineup = lineupList.get(lineupList.size() - 1);
    }

    @Test
    @Transactional
    void createLineupWithExistingId() throws Exception {
        // Create the Lineup with an existing ID
        lineup.setId(1L);
        LineupDTO lineupDTO = lineupMapper.toDto(lineup);

        int databaseSizeBeforeCreate = lineupRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLineupMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lineupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Lineup in the database
        List<Lineup> lineupList = lineupRepository.findAll();
        assertThat(lineupList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllLineups() throws Exception {
        // Initialize the database
        lineupRepository.saveAndFlush(lineup);

        // Get all the lineupList
        restLineupMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lineup.getId().intValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllLineupsWithEagerRelationshipsIsEnabled() throws Exception {
        when(lineupServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restLineupMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(lineupServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllLineupsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(lineupServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restLineupMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(lineupServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getLineup() throws Exception {
        // Initialize the database
        lineupRepository.saveAndFlush(lineup);

        // Get the lineup
        restLineupMockMvc
            .perform(get(ENTITY_API_URL_ID, lineup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(lineup.getId().intValue()));
    }

    @Test
    @Transactional
    void getNonExistingLineup() throws Exception {
        // Get the lineup
        restLineupMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLineup() throws Exception {
        // Initialize the database
        lineupRepository.saveAndFlush(lineup);

        int databaseSizeBeforeUpdate = lineupRepository.findAll().size();

        // Update the lineup
        Lineup updatedLineup = lineupRepository.findById(lineup.getId()).get();
        // Disconnect from session so that the updates on updatedLineup are not directly saved in db
        em.detach(updatedLineup);
        LineupDTO lineupDTO = lineupMapper.toDto(updatedLineup);

        restLineupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, lineupDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lineupDTO))
            )
            .andExpect(status().isOk());

        // Validate the Lineup in the database
        List<Lineup> lineupList = lineupRepository.findAll();
        assertThat(lineupList).hasSize(databaseSizeBeforeUpdate);
        Lineup testLineup = lineupList.get(lineupList.size() - 1);
    }

    @Test
    @Transactional
    void putNonExistingLineup() throws Exception {
        int databaseSizeBeforeUpdate = lineupRepository.findAll().size();
        lineup.setId(count.incrementAndGet());

        // Create the Lineup
        LineupDTO lineupDTO = lineupMapper.toDto(lineup);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLineupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, lineupDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lineupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Lineup in the database
        List<Lineup> lineupList = lineupRepository.findAll();
        assertThat(lineupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLineup() throws Exception {
        int databaseSizeBeforeUpdate = lineupRepository.findAll().size();
        lineup.setId(count.incrementAndGet());

        // Create the Lineup
        LineupDTO lineupDTO = lineupMapper.toDto(lineup);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLineupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lineupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Lineup in the database
        List<Lineup> lineupList = lineupRepository.findAll();
        assertThat(lineupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLineup() throws Exception {
        int databaseSizeBeforeUpdate = lineupRepository.findAll().size();
        lineup.setId(count.incrementAndGet());

        // Create the Lineup
        LineupDTO lineupDTO = lineupMapper.toDto(lineup);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLineupMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lineupDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Lineup in the database
        List<Lineup> lineupList = lineupRepository.findAll();
        assertThat(lineupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLineupWithPatch() throws Exception {
        // Initialize the database
        lineupRepository.saveAndFlush(lineup);

        int databaseSizeBeforeUpdate = lineupRepository.findAll().size();

        // Update the lineup using partial update
        Lineup partialUpdatedLineup = new Lineup();
        partialUpdatedLineup.setId(lineup.getId());

        restLineupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLineup.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLineup))
            )
            .andExpect(status().isOk());

        // Validate the Lineup in the database
        List<Lineup> lineupList = lineupRepository.findAll();
        assertThat(lineupList).hasSize(databaseSizeBeforeUpdate);
        Lineup testLineup = lineupList.get(lineupList.size() - 1);
    }

    @Test
    @Transactional
    void fullUpdateLineupWithPatch() throws Exception {
        // Initialize the database
        lineupRepository.saveAndFlush(lineup);

        int databaseSizeBeforeUpdate = lineupRepository.findAll().size();

        // Update the lineup using partial update
        Lineup partialUpdatedLineup = new Lineup();
        partialUpdatedLineup.setId(lineup.getId());

        restLineupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLineup.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLineup))
            )
            .andExpect(status().isOk());

        // Validate the Lineup in the database
        List<Lineup> lineupList = lineupRepository.findAll();
        assertThat(lineupList).hasSize(databaseSizeBeforeUpdate);
        Lineup testLineup = lineupList.get(lineupList.size() - 1);
    }

    @Test
    @Transactional
    void patchNonExistingLineup() throws Exception {
        int databaseSizeBeforeUpdate = lineupRepository.findAll().size();
        lineup.setId(count.incrementAndGet());

        // Create the Lineup
        LineupDTO lineupDTO = lineupMapper.toDto(lineup);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLineupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, lineupDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lineupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Lineup in the database
        List<Lineup> lineupList = lineupRepository.findAll();
        assertThat(lineupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLineup() throws Exception {
        int databaseSizeBeforeUpdate = lineupRepository.findAll().size();
        lineup.setId(count.incrementAndGet());

        // Create the Lineup
        LineupDTO lineupDTO = lineupMapper.toDto(lineup);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLineupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lineupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Lineup in the database
        List<Lineup> lineupList = lineupRepository.findAll();
        assertThat(lineupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLineup() throws Exception {
        int databaseSizeBeforeUpdate = lineupRepository.findAll().size();
        lineup.setId(count.incrementAndGet());

        // Create the Lineup
        LineupDTO lineupDTO = lineupMapper.toDto(lineup);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLineupMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lineupDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Lineup in the database
        List<Lineup> lineupList = lineupRepository.findAll();
        assertThat(lineupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLineup() throws Exception {
        // Initialize the database
        lineupRepository.saveAndFlush(lineup);

        int databaseSizeBeforeDelete = lineupRepository.findAll().size();

        // Delete the lineup
        restLineupMockMvc
            .perform(delete(ENTITY_API_URL_ID, lineup.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Lineup> lineupList = lineupRepository.findAll();
        assertThat(lineupList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
