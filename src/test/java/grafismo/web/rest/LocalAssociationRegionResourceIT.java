package grafismo.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import grafismo.IntegrationTest;
import grafismo.domain.LocalAssociationRegion;
import grafismo.repository.LocalAssociationRegionRepository;
import grafismo.service.LocalAssociationRegionService;
import grafismo.service.dto.LocalAssociationRegionDTO;
import grafismo.service.mapper.LocalAssociationRegionMapper;
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
 * Integration tests for the {@link LocalAssociationRegionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class LocalAssociationRegionResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/local-association-regions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LocalAssociationRegionRepository localAssociationRegionRepository;

    @Mock
    private LocalAssociationRegionRepository localAssociationRegionRepositoryMock;

    @Autowired
    private LocalAssociationRegionMapper localAssociationRegionMapper;

    @Mock
    private LocalAssociationRegionService localAssociationRegionServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLocalAssociationRegionMockMvc;

    private LocalAssociationRegion localAssociationRegion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LocalAssociationRegion createEntity(EntityManager em) {
        LocalAssociationRegion localAssociationRegion = new LocalAssociationRegion().name(DEFAULT_NAME);
        return localAssociationRegion;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LocalAssociationRegion createUpdatedEntity(EntityManager em) {
        LocalAssociationRegion localAssociationRegion = new LocalAssociationRegion().name(UPDATED_NAME);
        return localAssociationRegion;
    }

    @BeforeEach
    public void initTest() {
        localAssociationRegion = createEntity(em);
    }

    @Test
    @Transactional
    void createLocalAssociationRegion() throws Exception {
        int databaseSizeBeforeCreate = localAssociationRegionRepository.findAll().size();
        // Create the LocalAssociationRegion
        LocalAssociationRegionDTO localAssociationRegionDTO = localAssociationRegionMapper.toDto(localAssociationRegion);
        restLocalAssociationRegionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(localAssociationRegionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the LocalAssociationRegion in the database
        List<LocalAssociationRegion> localAssociationRegionList = localAssociationRegionRepository.findAll();
        assertThat(localAssociationRegionList).hasSize(databaseSizeBeforeCreate + 1);
        LocalAssociationRegion testLocalAssociationRegion = localAssociationRegionList.get(localAssociationRegionList.size() - 1);
        assertThat(testLocalAssociationRegion.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createLocalAssociationRegionWithExistingId() throws Exception {
        // Create the LocalAssociationRegion with an existing ID
        localAssociationRegion.setId(1L);
        LocalAssociationRegionDTO localAssociationRegionDTO = localAssociationRegionMapper.toDto(localAssociationRegion);

        int databaseSizeBeforeCreate = localAssociationRegionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLocalAssociationRegionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(localAssociationRegionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LocalAssociationRegion in the database
        List<LocalAssociationRegion> localAssociationRegionList = localAssociationRegionRepository.findAll();
        assertThat(localAssociationRegionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = localAssociationRegionRepository.findAll().size();
        // set the field null
        localAssociationRegion.setName(null);

        // Create the LocalAssociationRegion, which fails.
        LocalAssociationRegionDTO localAssociationRegionDTO = localAssociationRegionMapper.toDto(localAssociationRegion);

        restLocalAssociationRegionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(localAssociationRegionDTO))
            )
            .andExpect(status().isBadRequest());

        List<LocalAssociationRegion> localAssociationRegionList = localAssociationRegionRepository.findAll();
        assertThat(localAssociationRegionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLocalAssociationRegions() throws Exception {
        // Initialize the database
        localAssociationRegionRepository.saveAndFlush(localAssociationRegion);

        // Get all the localAssociationRegionList
        restLocalAssociationRegionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(localAssociationRegion.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllLocalAssociationRegionsWithEagerRelationshipsIsEnabled() throws Exception {
        when(localAssociationRegionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restLocalAssociationRegionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(localAssociationRegionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllLocalAssociationRegionsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(localAssociationRegionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restLocalAssociationRegionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(localAssociationRegionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getLocalAssociationRegion() throws Exception {
        // Initialize the database
        localAssociationRegionRepository.saveAndFlush(localAssociationRegion);

        // Get the localAssociationRegion
        restLocalAssociationRegionMockMvc
            .perform(get(ENTITY_API_URL_ID, localAssociationRegion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(localAssociationRegion.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingLocalAssociationRegion() throws Exception {
        // Get the localAssociationRegion
        restLocalAssociationRegionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLocalAssociationRegion() throws Exception {
        // Initialize the database
        localAssociationRegionRepository.saveAndFlush(localAssociationRegion);

        int databaseSizeBeforeUpdate = localAssociationRegionRepository.findAll().size();

        // Update the localAssociationRegion
        LocalAssociationRegion updatedLocalAssociationRegion = localAssociationRegionRepository
            .findById(localAssociationRegion.getId())
            .get();
        // Disconnect from session so that the updates on updatedLocalAssociationRegion are not directly saved in db
        em.detach(updatedLocalAssociationRegion);
        updatedLocalAssociationRegion.name(UPDATED_NAME);
        LocalAssociationRegionDTO localAssociationRegionDTO = localAssociationRegionMapper.toDto(updatedLocalAssociationRegion);

        restLocalAssociationRegionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, localAssociationRegionDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(localAssociationRegionDTO))
            )
            .andExpect(status().isOk());

        // Validate the LocalAssociationRegion in the database
        List<LocalAssociationRegion> localAssociationRegionList = localAssociationRegionRepository.findAll();
        assertThat(localAssociationRegionList).hasSize(databaseSizeBeforeUpdate);
        LocalAssociationRegion testLocalAssociationRegion = localAssociationRegionList.get(localAssociationRegionList.size() - 1);
        assertThat(testLocalAssociationRegion.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingLocalAssociationRegion() throws Exception {
        int databaseSizeBeforeUpdate = localAssociationRegionRepository.findAll().size();
        localAssociationRegion.setId(count.incrementAndGet());

        // Create the LocalAssociationRegion
        LocalAssociationRegionDTO localAssociationRegionDTO = localAssociationRegionMapper.toDto(localAssociationRegion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLocalAssociationRegionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, localAssociationRegionDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(localAssociationRegionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LocalAssociationRegion in the database
        List<LocalAssociationRegion> localAssociationRegionList = localAssociationRegionRepository.findAll();
        assertThat(localAssociationRegionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLocalAssociationRegion() throws Exception {
        int databaseSizeBeforeUpdate = localAssociationRegionRepository.findAll().size();
        localAssociationRegion.setId(count.incrementAndGet());

        // Create the LocalAssociationRegion
        LocalAssociationRegionDTO localAssociationRegionDTO = localAssociationRegionMapper.toDto(localAssociationRegion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocalAssociationRegionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(localAssociationRegionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LocalAssociationRegion in the database
        List<LocalAssociationRegion> localAssociationRegionList = localAssociationRegionRepository.findAll();
        assertThat(localAssociationRegionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLocalAssociationRegion() throws Exception {
        int databaseSizeBeforeUpdate = localAssociationRegionRepository.findAll().size();
        localAssociationRegion.setId(count.incrementAndGet());

        // Create the LocalAssociationRegion
        LocalAssociationRegionDTO localAssociationRegionDTO = localAssociationRegionMapper.toDto(localAssociationRegion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocalAssociationRegionMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(localAssociationRegionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LocalAssociationRegion in the database
        List<LocalAssociationRegion> localAssociationRegionList = localAssociationRegionRepository.findAll();
        assertThat(localAssociationRegionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLocalAssociationRegionWithPatch() throws Exception {
        // Initialize the database
        localAssociationRegionRepository.saveAndFlush(localAssociationRegion);

        int databaseSizeBeforeUpdate = localAssociationRegionRepository.findAll().size();

        // Update the localAssociationRegion using partial update
        LocalAssociationRegion partialUpdatedLocalAssociationRegion = new LocalAssociationRegion();
        partialUpdatedLocalAssociationRegion.setId(localAssociationRegion.getId());

        restLocalAssociationRegionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLocalAssociationRegion.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLocalAssociationRegion))
            )
            .andExpect(status().isOk());

        // Validate the LocalAssociationRegion in the database
        List<LocalAssociationRegion> localAssociationRegionList = localAssociationRegionRepository.findAll();
        assertThat(localAssociationRegionList).hasSize(databaseSizeBeforeUpdate);
        LocalAssociationRegion testLocalAssociationRegion = localAssociationRegionList.get(localAssociationRegionList.size() - 1);
        assertThat(testLocalAssociationRegion.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void fullUpdateLocalAssociationRegionWithPatch() throws Exception {
        // Initialize the database
        localAssociationRegionRepository.saveAndFlush(localAssociationRegion);

        int databaseSizeBeforeUpdate = localAssociationRegionRepository.findAll().size();

        // Update the localAssociationRegion using partial update
        LocalAssociationRegion partialUpdatedLocalAssociationRegion = new LocalAssociationRegion();
        partialUpdatedLocalAssociationRegion.setId(localAssociationRegion.getId());

        partialUpdatedLocalAssociationRegion.name(UPDATED_NAME);

        restLocalAssociationRegionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLocalAssociationRegion.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLocalAssociationRegion))
            )
            .andExpect(status().isOk());

        // Validate the LocalAssociationRegion in the database
        List<LocalAssociationRegion> localAssociationRegionList = localAssociationRegionRepository.findAll();
        assertThat(localAssociationRegionList).hasSize(databaseSizeBeforeUpdate);
        LocalAssociationRegion testLocalAssociationRegion = localAssociationRegionList.get(localAssociationRegionList.size() - 1);
        assertThat(testLocalAssociationRegion.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingLocalAssociationRegion() throws Exception {
        int databaseSizeBeforeUpdate = localAssociationRegionRepository.findAll().size();
        localAssociationRegion.setId(count.incrementAndGet());

        // Create the LocalAssociationRegion
        LocalAssociationRegionDTO localAssociationRegionDTO = localAssociationRegionMapper.toDto(localAssociationRegion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLocalAssociationRegionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, localAssociationRegionDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(localAssociationRegionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LocalAssociationRegion in the database
        List<LocalAssociationRegion> localAssociationRegionList = localAssociationRegionRepository.findAll();
        assertThat(localAssociationRegionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLocalAssociationRegion() throws Exception {
        int databaseSizeBeforeUpdate = localAssociationRegionRepository.findAll().size();
        localAssociationRegion.setId(count.incrementAndGet());

        // Create the LocalAssociationRegion
        LocalAssociationRegionDTO localAssociationRegionDTO = localAssociationRegionMapper.toDto(localAssociationRegion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocalAssociationRegionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(localAssociationRegionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LocalAssociationRegion in the database
        List<LocalAssociationRegion> localAssociationRegionList = localAssociationRegionRepository.findAll();
        assertThat(localAssociationRegionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLocalAssociationRegion() throws Exception {
        int databaseSizeBeforeUpdate = localAssociationRegionRepository.findAll().size();
        localAssociationRegion.setId(count.incrementAndGet());

        // Create the LocalAssociationRegion
        LocalAssociationRegionDTO localAssociationRegionDTO = localAssociationRegionMapper.toDto(localAssociationRegion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocalAssociationRegionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(localAssociationRegionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LocalAssociationRegion in the database
        List<LocalAssociationRegion> localAssociationRegionList = localAssociationRegionRepository.findAll();
        assertThat(localAssociationRegionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLocalAssociationRegion() throws Exception {
        // Initialize the database
        localAssociationRegionRepository.saveAndFlush(localAssociationRegion);

        int databaseSizeBeforeDelete = localAssociationRegionRepository.findAll().size();

        // Delete the localAssociationRegion
        restLocalAssociationRegionMockMvc
            .perform(delete(ENTITY_API_URL_ID, localAssociationRegion.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LocalAssociationRegion> localAssociationRegionList = localAssociationRegionRepository.findAll();
        assertThat(localAssociationRegionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
