package grafismo.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import grafismo.IntegrationTest;
import grafismo.domain.LocalAssociationProvince;
import grafismo.repository.LocalAssociationProvinceRepository;
import grafismo.service.LocalAssociationProvinceService;
import grafismo.service.dto.LocalAssociationProvinceDTO;
import grafismo.service.mapper.LocalAssociationProvinceMapper;
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
 * Integration tests for the {@link LocalAssociationProvinceResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class LocalAssociationProvinceResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/local-association-provinces";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LocalAssociationProvinceRepository localAssociationProvinceRepository;

    @Mock
    private LocalAssociationProvinceRepository localAssociationProvinceRepositoryMock;

    @Autowired
    private LocalAssociationProvinceMapper localAssociationProvinceMapper;

    @Mock
    private LocalAssociationProvinceService localAssociationProvinceServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLocalAssociationProvinceMockMvc;

    private LocalAssociationProvince localAssociationProvince;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LocalAssociationProvince createEntity(EntityManager em) {
        LocalAssociationProvince localAssociationProvince = new LocalAssociationProvince().name(DEFAULT_NAME);
        return localAssociationProvince;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LocalAssociationProvince createUpdatedEntity(EntityManager em) {
        LocalAssociationProvince localAssociationProvince = new LocalAssociationProvince().name(UPDATED_NAME);
        return localAssociationProvince;
    }

    @BeforeEach
    public void initTest() {
        localAssociationProvince = createEntity(em);
    }

    @Test
    @Transactional
    void createLocalAssociationProvince() throws Exception {
        int databaseSizeBeforeCreate = localAssociationProvinceRepository.findAll().size();
        // Create the LocalAssociationProvince
        LocalAssociationProvinceDTO localAssociationProvinceDTO = localAssociationProvinceMapper.toDto(localAssociationProvince);
        restLocalAssociationProvinceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(localAssociationProvinceDTO))
            )
            .andExpect(status().isCreated());

        // Validate the LocalAssociationProvince in the database
        List<LocalAssociationProvince> localAssociationProvinceList = localAssociationProvinceRepository.findAll();
        assertThat(localAssociationProvinceList).hasSize(databaseSizeBeforeCreate + 1);
        LocalAssociationProvince testLocalAssociationProvince = localAssociationProvinceList.get(localAssociationProvinceList.size() - 1);
        assertThat(testLocalAssociationProvince.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createLocalAssociationProvinceWithExistingId() throws Exception {
        // Create the LocalAssociationProvince with an existing ID
        localAssociationProvince.setId(1L);
        LocalAssociationProvinceDTO localAssociationProvinceDTO = localAssociationProvinceMapper.toDto(localAssociationProvince);

        int databaseSizeBeforeCreate = localAssociationProvinceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLocalAssociationProvinceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(localAssociationProvinceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LocalAssociationProvince in the database
        List<LocalAssociationProvince> localAssociationProvinceList = localAssociationProvinceRepository.findAll();
        assertThat(localAssociationProvinceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = localAssociationProvinceRepository.findAll().size();
        // set the field null
        localAssociationProvince.setName(null);

        // Create the LocalAssociationProvince, which fails.
        LocalAssociationProvinceDTO localAssociationProvinceDTO = localAssociationProvinceMapper.toDto(localAssociationProvince);

        restLocalAssociationProvinceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(localAssociationProvinceDTO))
            )
            .andExpect(status().isBadRequest());

        List<LocalAssociationProvince> localAssociationProvinceList = localAssociationProvinceRepository.findAll();
        assertThat(localAssociationProvinceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLocalAssociationProvinces() throws Exception {
        // Initialize the database
        localAssociationProvinceRepository.saveAndFlush(localAssociationProvince);

        // Get all the localAssociationProvinceList
        restLocalAssociationProvinceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(localAssociationProvince.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllLocalAssociationProvincesWithEagerRelationshipsIsEnabled() throws Exception {
        when(localAssociationProvinceServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restLocalAssociationProvinceMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(localAssociationProvinceServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllLocalAssociationProvincesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(localAssociationProvinceServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restLocalAssociationProvinceMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(localAssociationProvinceServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getLocalAssociationProvince() throws Exception {
        // Initialize the database
        localAssociationProvinceRepository.saveAndFlush(localAssociationProvince);

        // Get the localAssociationProvince
        restLocalAssociationProvinceMockMvc
            .perform(get(ENTITY_API_URL_ID, localAssociationProvince.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(localAssociationProvince.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingLocalAssociationProvince() throws Exception {
        // Get the localAssociationProvince
        restLocalAssociationProvinceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLocalAssociationProvince() throws Exception {
        // Initialize the database
        localAssociationProvinceRepository.saveAndFlush(localAssociationProvince);

        int databaseSizeBeforeUpdate = localAssociationProvinceRepository.findAll().size();

        // Update the localAssociationProvince
        LocalAssociationProvince updatedLocalAssociationProvince = localAssociationProvinceRepository
            .findById(localAssociationProvince.getId())
            .get();
        // Disconnect from session so that the updates on updatedLocalAssociationProvince are not directly saved in db
        em.detach(updatedLocalAssociationProvince);
        updatedLocalAssociationProvince.name(UPDATED_NAME);
        LocalAssociationProvinceDTO localAssociationProvinceDTO = localAssociationProvinceMapper.toDto(updatedLocalAssociationProvince);

        restLocalAssociationProvinceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, localAssociationProvinceDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(localAssociationProvinceDTO))
            )
            .andExpect(status().isOk());

        // Validate the LocalAssociationProvince in the database
        List<LocalAssociationProvince> localAssociationProvinceList = localAssociationProvinceRepository.findAll();
        assertThat(localAssociationProvinceList).hasSize(databaseSizeBeforeUpdate);
        LocalAssociationProvince testLocalAssociationProvince = localAssociationProvinceList.get(localAssociationProvinceList.size() - 1);
        assertThat(testLocalAssociationProvince.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingLocalAssociationProvince() throws Exception {
        int databaseSizeBeforeUpdate = localAssociationProvinceRepository.findAll().size();
        localAssociationProvince.setId(count.incrementAndGet());

        // Create the LocalAssociationProvince
        LocalAssociationProvinceDTO localAssociationProvinceDTO = localAssociationProvinceMapper.toDto(localAssociationProvince);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLocalAssociationProvinceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, localAssociationProvinceDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(localAssociationProvinceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LocalAssociationProvince in the database
        List<LocalAssociationProvince> localAssociationProvinceList = localAssociationProvinceRepository.findAll();
        assertThat(localAssociationProvinceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLocalAssociationProvince() throws Exception {
        int databaseSizeBeforeUpdate = localAssociationProvinceRepository.findAll().size();
        localAssociationProvince.setId(count.incrementAndGet());

        // Create the LocalAssociationProvince
        LocalAssociationProvinceDTO localAssociationProvinceDTO = localAssociationProvinceMapper.toDto(localAssociationProvince);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocalAssociationProvinceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(localAssociationProvinceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LocalAssociationProvince in the database
        List<LocalAssociationProvince> localAssociationProvinceList = localAssociationProvinceRepository.findAll();
        assertThat(localAssociationProvinceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLocalAssociationProvince() throws Exception {
        int databaseSizeBeforeUpdate = localAssociationProvinceRepository.findAll().size();
        localAssociationProvince.setId(count.incrementAndGet());

        // Create the LocalAssociationProvince
        LocalAssociationProvinceDTO localAssociationProvinceDTO = localAssociationProvinceMapper.toDto(localAssociationProvince);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocalAssociationProvinceMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(localAssociationProvinceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LocalAssociationProvince in the database
        List<LocalAssociationProvince> localAssociationProvinceList = localAssociationProvinceRepository.findAll();
        assertThat(localAssociationProvinceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLocalAssociationProvinceWithPatch() throws Exception {
        // Initialize the database
        localAssociationProvinceRepository.saveAndFlush(localAssociationProvince);

        int databaseSizeBeforeUpdate = localAssociationProvinceRepository.findAll().size();

        // Update the localAssociationProvince using partial update
        LocalAssociationProvince partialUpdatedLocalAssociationProvince = new LocalAssociationProvince();
        partialUpdatedLocalAssociationProvince.setId(localAssociationProvince.getId());

        partialUpdatedLocalAssociationProvince.name(UPDATED_NAME);

        restLocalAssociationProvinceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLocalAssociationProvince.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLocalAssociationProvince))
            )
            .andExpect(status().isOk());

        // Validate the LocalAssociationProvince in the database
        List<LocalAssociationProvince> localAssociationProvinceList = localAssociationProvinceRepository.findAll();
        assertThat(localAssociationProvinceList).hasSize(databaseSizeBeforeUpdate);
        LocalAssociationProvince testLocalAssociationProvince = localAssociationProvinceList.get(localAssociationProvinceList.size() - 1);
        assertThat(testLocalAssociationProvince.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void fullUpdateLocalAssociationProvinceWithPatch() throws Exception {
        // Initialize the database
        localAssociationProvinceRepository.saveAndFlush(localAssociationProvince);

        int databaseSizeBeforeUpdate = localAssociationProvinceRepository.findAll().size();

        // Update the localAssociationProvince using partial update
        LocalAssociationProvince partialUpdatedLocalAssociationProvince = new LocalAssociationProvince();
        partialUpdatedLocalAssociationProvince.setId(localAssociationProvince.getId());

        partialUpdatedLocalAssociationProvince.name(UPDATED_NAME);

        restLocalAssociationProvinceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLocalAssociationProvince.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLocalAssociationProvince))
            )
            .andExpect(status().isOk());

        // Validate the LocalAssociationProvince in the database
        List<LocalAssociationProvince> localAssociationProvinceList = localAssociationProvinceRepository.findAll();
        assertThat(localAssociationProvinceList).hasSize(databaseSizeBeforeUpdate);
        LocalAssociationProvince testLocalAssociationProvince = localAssociationProvinceList.get(localAssociationProvinceList.size() - 1);
        assertThat(testLocalAssociationProvince.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingLocalAssociationProvince() throws Exception {
        int databaseSizeBeforeUpdate = localAssociationProvinceRepository.findAll().size();
        localAssociationProvince.setId(count.incrementAndGet());

        // Create the LocalAssociationProvince
        LocalAssociationProvinceDTO localAssociationProvinceDTO = localAssociationProvinceMapper.toDto(localAssociationProvince);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLocalAssociationProvinceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, localAssociationProvinceDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(localAssociationProvinceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LocalAssociationProvince in the database
        List<LocalAssociationProvince> localAssociationProvinceList = localAssociationProvinceRepository.findAll();
        assertThat(localAssociationProvinceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLocalAssociationProvince() throws Exception {
        int databaseSizeBeforeUpdate = localAssociationProvinceRepository.findAll().size();
        localAssociationProvince.setId(count.incrementAndGet());

        // Create the LocalAssociationProvince
        LocalAssociationProvinceDTO localAssociationProvinceDTO = localAssociationProvinceMapper.toDto(localAssociationProvince);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocalAssociationProvinceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(localAssociationProvinceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LocalAssociationProvince in the database
        List<LocalAssociationProvince> localAssociationProvinceList = localAssociationProvinceRepository.findAll();
        assertThat(localAssociationProvinceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLocalAssociationProvince() throws Exception {
        int databaseSizeBeforeUpdate = localAssociationProvinceRepository.findAll().size();
        localAssociationProvince.setId(count.incrementAndGet());

        // Create the LocalAssociationProvince
        LocalAssociationProvinceDTO localAssociationProvinceDTO = localAssociationProvinceMapper.toDto(localAssociationProvince);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocalAssociationProvinceMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(localAssociationProvinceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LocalAssociationProvince in the database
        List<LocalAssociationProvince> localAssociationProvinceList = localAssociationProvinceRepository.findAll();
        assertThat(localAssociationProvinceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLocalAssociationProvince() throws Exception {
        // Initialize the database
        localAssociationProvinceRepository.saveAndFlush(localAssociationProvince);

        int databaseSizeBeforeDelete = localAssociationProvinceRepository.findAll().size();

        // Delete the localAssociationProvince
        restLocalAssociationProvinceMockMvc
            .perform(delete(ENTITY_API_URL_ID, localAssociationProvince.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LocalAssociationProvince> localAssociationProvinceList = localAssociationProvinceRepository.findAll();
        assertThat(localAssociationProvinceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
