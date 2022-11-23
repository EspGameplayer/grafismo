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
import grafismo.repository.CallupRepository;
import grafismo.service.CallupService;
import grafismo.service.dto.CallupDTO;
import grafismo.service.mapper.CallupMapper;
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
 * Integration tests for the {@link CallupResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CallupResourceIT {

    private static final String ENTITY_API_URL = "/api/callups";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CallupRepository callupRepository;

    @Mock
    private CallupRepository callupRepositoryMock;

    @Autowired
    private CallupMapper callupMapper;

    @Mock
    private CallupService callupServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCallupMockMvc;

    private Callup callup;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Callup createEntity(EntityManager em) {
        Callup callup = new Callup();
        // Add required entity
        Match match;
        if (TestUtil.findAll(em, Match.class).isEmpty()) {
            match = MatchResourceIT.createEntity(em);
            em.persist(match);
            em.flush();
        } else {
            match = TestUtil.findAll(em, Match.class).get(0);
        }
        callup.setHomeMatch(match);
        // Add required entity
        callup.setAwayMatch(match);
        return callup;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Callup createUpdatedEntity(EntityManager em) {
        Callup callup = new Callup();
        // Add required entity
        Match match;
        if (TestUtil.findAll(em, Match.class).isEmpty()) {
            match = MatchResourceIT.createUpdatedEntity(em);
            em.persist(match);
            em.flush();
        } else {
            match = TestUtil.findAll(em, Match.class).get(0);
        }
        callup.setHomeMatch(match);
        // Add required entity
        callup.setAwayMatch(match);
        return callup;
    }

    @BeforeEach
    public void initTest() {
        callup = createEntity(em);
    }

    @Test
    @Transactional
    void createCallup() throws Exception {
        int databaseSizeBeforeCreate = callupRepository.findAll().size();
        // Create the Callup
        CallupDTO callupDTO = callupMapper.toDto(callup);
        restCallupMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(callupDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Callup in the database
        List<Callup> callupList = callupRepository.findAll();
        assertThat(callupList).hasSize(databaseSizeBeforeCreate + 1);
        Callup testCallup = callupList.get(callupList.size() - 1);
    }

    @Test
    @Transactional
    void createCallupWithExistingId() throws Exception {
        // Create the Callup with an existing ID
        callup.setId(1L);
        CallupDTO callupDTO = callupMapper.toDto(callup);

        int databaseSizeBeforeCreate = callupRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCallupMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(callupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Callup in the database
        List<Callup> callupList = callupRepository.findAll();
        assertThat(callupList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCallups() throws Exception {
        // Initialize the database
        callupRepository.saveAndFlush(callup);

        // Get all the callupList
        restCallupMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(callup.getId().intValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCallupsWithEagerRelationshipsIsEnabled() throws Exception {
        when(callupServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCallupMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(callupServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCallupsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(callupServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCallupMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(callupServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getCallup() throws Exception {
        // Initialize the database
        callupRepository.saveAndFlush(callup);

        // Get the callup
        restCallupMockMvc
            .perform(get(ENTITY_API_URL_ID, callup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(callup.getId().intValue()));
    }

    @Test
    @Transactional
    void getNonExistingCallup() throws Exception {
        // Get the callup
        restCallupMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCallup() throws Exception {
        // Initialize the database
        callupRepository.saveAndFlush(callup);

        int databaseSizeBeforeUpdate = callupRepository.findAll().size();

        // Update the callup
        Callup updatedCallup = callupRepository.findById(callup.getId()).get();
        // Disconnect from session so that the updates on updatedCallup are not directly saved in db
        em.detach(updatedCallup);
        CallupDTO callupDTO = callupMapper.toDto(updatedCallup);

        restCallupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, callupDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(callupDTO))
            )
            .andExpect(status().isOk());

        // Validate the Callup in the database
        List<Callup> callupList = callupRepository.findAll();
        assertThat(callupList).hasSize(databaseSizeBeforeUpdate);
        Callup testCallup = callupList.get(callupList.size() - 1);
    }

    @Test
    @Transactional
    void putNonExistingCallup() throws Exception {
        int databaseSizeBeforeUpdate = callupRepository.findAll().size();
        callup.setId(count.incrementAndGet());

        // Create the Callup
        CallupDTO callupDTO = callupMapper.toDto(callup);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCallupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, callupDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(callupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Callup in the database
        List<Callup> callupList = callupRepository.findAll();
        assertThat(callupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCallup() throws Exception {
        int databaseSizeBeforeUpdate = callupRepository.findAll().size();
        callup.setId(count.incrementAndGet());

        // Create the Callup
        CallupDTO callupDTO = callupMapper.toDto(callup);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCallupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(callupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Callup in the database
        List<Callup> callupList = callupRepository.findAll();
        assertThat(callupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCallup() throws Exception {
        int databaseSizeBeforeUpdate = callupRepository.findAll().size();
        callup.setId(count.incrementAndGet());

        // Create the Callup
        CallupDTO callupDTO = callupMapper.toDto(callup);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCallupMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(callupDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Callup in the database
        List<Callup> callupList = callupRepository.findAll();
        assertThat(callupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCallupWithPatch() throws Exception {
        // Initialize the database
        callupRepository.saveAndFlush(callup);

        int databaseSizeBeforeUpdate = callupRepository.findAll().size();

        // Update the callup using partial update
        Callup partialUpdatedCallup = new Callup();
        partialUpdatedCallup.setId(callup.getId());

        restCallupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCallup.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCallup))
            )
            .andExpect(status().isOk());

        // Validate the Callup in the database
        List<Callup> callupList = callupRepository.findAll();
        assertThat(callupList).hasSize(databaseSizeBeforeUpdate);
        Callup testCallup = callupList.get(callupList.size() - 1);
    }

    @Test
    @Transactional
    void fullUpdateCallupWithPatch() throws Exception {
        // Initialize the database
        callupRepository.saveAndFlush(callup);

        int databaseSizeBeforeUpdate = callupRepository.findAll().size();

        // Update the callup using partial update
        Callup partialUpdatedCallup = new Callup();
        partialUpdatedCallup.setId(callup.getId());

        restCallupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCallup.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCallup))
            )
            .andExpect(status().isOk());

        // Validate the Callup in the database
        List<Callup> callupList = callupRepository.findAll();
        assertThat(callupList).hasSize(databaseSizeBeforeUpdate);
        Callup testCallup = callupList.get(callupList.size() - 1);
    }

    @Test
    @Transactional
    void patchNonExistingCallup() throws Exception {
        int databaseSizeBeforeUpdate = callupRepository.findAll().size();
        callup.setId(count.incrementAndGet());

        // Create the Callup
        CallupDTO callupDTO = callupMapper.toDto(callup);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCallupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, callupDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(callupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Callup in the database
        List<Callup> callupList = callupRepository.findAll();
        assertThat(callupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCallup() throws Exception {
        int databaseSizeBeforeUpdate = callupRepository.findAll().size();
        callup.setId(count.incrementAndGet());

        // Create the Callup
        CallupDTO callupDTO = callupMapper.toDto(callup);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCallupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(callupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Callup in the database
        List<Callup> callupList = callupRepository.findAll();
        assertThat(callupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCallup() throws Exception {
        int databaseSizeBeforeUpdate = callupRepository.findAll().size();
        callup.setId(count.incrementAndGet());

        // Create the Callup
        CallupDTO callupDTO = callupMapper.toDto(callup);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCallupMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(callupDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Callup in the database
        List<Callup> callupList = callupRepository.findAll();
        assertThat(callupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCallup() throws Exception {
        // Initialize the database
        callupRepository.saveAndFlush(callup);

        int databaseSizeBeforeDelete = callupRepository.findAll().size();

        // Delete the callup
        restCallupMockMvc
            .perform(delete(ENTITY_API_URL_ID, callup.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Callup> callupList = callupRepository.findAll();
        assertThat(callupList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
