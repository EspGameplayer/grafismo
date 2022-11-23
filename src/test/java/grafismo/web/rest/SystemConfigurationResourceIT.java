package grafismo.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import grafismo.IntegrationTest;
import grafismo.domain.SystemConfiguration;
import grafismo.repository.SystemConfigurationRepository;
import grafismo.service.SystemConfigurationService;
import grafismo.service.dto.SystemConfigurationDTO;
import grafismo.service.mapper.SystemConfigurationMapper;
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
 * Integration tests for the {@link SystemConfigurationResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SystemConfigurationResourceIT {

    private static final Instant DEFAULT_CURRENT_PERIOD_START_MOMENT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CURRENT_PERIOD_START_MOMENT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/system-configurations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SystemConfigurationRepository systemConfigurationRepository;

    @Mock
    private SystemConfigurationRepository systemConfigurationRepositoryMock;

    @Autowired
    private SystemConfigurationMapper systemConfigurationMapper;

    @Mock
    private SystemConfigurationService systemConfigurationServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSystemConfigurationMockMvc;

    private SystemConfiguration systemConfiguration;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SystemConfiguration createEntity(EntityManager em) {
        SystemConfiguration systemConfiguration = new SystemConfiguration().currentPeriodStartMoment(DEFAULT_CURRENT_PERIOD_START_MOMENT);
        return systemConfiguration;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SystemConfiguration createUpdatedEntity(EntityManager em) {
        SystemConfiguration systemConfiguration = new SystemConfiguration().currentPeriodStartMoment(UPDATED_CURRENT_PERIOD_START_MOMENT);
        return systemConfiguration;
    }

    @BeforeEach
    public void initTest() {
        systemConfiguration = createEntity(em);
    }

    @Test
    @Transactional
    void createSystemConfiguration() throws Exception {
        int databaseSizeBeforeCreate = systemConfigurationRepository.findAll().size();
        // Create the SystemConfiguration
        SystemConfigurationDTO systemConfigurationDTO = systemConfigurationMapper.toDto(systemConfiguration);
        restSystemConfigurationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(systemConfigurationDTO))
            )
            .andExpect(status().isCreated());

        // Validate the SystemConfiguration in the database
        List<SystemConfiguration> systemConfigurationList = systemConfigurationRepository.findAll();
        assertThat(systemConfigurationList).hasSize(databaseSizeBeforeCreate + 1);
        SystemConfiguration testSystemConfiguration = systemConfigurationList.get(systemConfigurationList.size() - 1);
        assertThat(testSystemConfiguration.getCurrentPeriodStartMoment()).isEqualTo(DEFAULT_CURRENT_PERIOD_START_MOMENT);
    }

    @Test
    @Transactional
    void createSystemConfigurationWithExistingId() throws Exception {
        // Create the SystemConfiguration with an existing ID
        systemConfiguration.setId(1L);
        SystemConfigurationDTO systemConfigurationDTO = systemConfigurationMapper.toDto(systemConfiguration);

        int databaseSizeBeforeCreate = systemConfigurationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSystemConfigurationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(systemConfigurationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SystemConfiguration in the database
        List<SystemConfiguration> systemConfigurationList = systemConfigurationRepository.findAll();
        assertThat(systemConfigurationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSystemConfigurations() throws Exception {
        // Initialize the database
        systemConfigurationRepository.saveAndFlush(systemConfiguration);

        // Get all the systemConfigurationList
        restSystemConfigurationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(systemConfiguration.getId().intValue())))
            .andExpect(jsonPath("$.[*].currentPeriodStartMoment").value(hasItem(DEFAULT_CURRENT_PERIOD_START_MOMENT.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSystemConfigurationsWithEagerRelationshipsIsEnabled() throws Exception {
        when(systemConfigurationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSystemConfigurationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(systemConfigurationServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSystemConfigurationsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(systemConfigurationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSystemConfigurationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(systemConfigurationServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getSystemConfiguration() throws Exception {
        // Initialize the database
        systemConfigurationRepository.saveAndFlush(systemConfiguration);

        // Get the systemConfiguration
        restSystemConfigurationMockMvc
            .perform(get(ENTITY_API_URL_ID, systemConfiguration.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(systemConfiguration.getId().intValue()))
            .andExpect(jsonPath("$.currentPeriodStartMoment").value(DEFAULT_CURRENT_PERIOD_START_MOMENT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingSystemConfiguration() throws Exception {
        // Get the systemConfiguration
        restSystemConfigurationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSystemConfiguration() throws Exception {
        // Initialize the database
        systemConfigurationRepository.saveAndFlush(systemConfiguration);

        int databaseSizeBeforeUpdate = systemConfigurationRepository.findAll().size();

        // Update the systemConfiguration
        SystemConfiguration updatedSystemConfiguration = systemConfigurationRepository.findById(systemConfiguration.getId()).get();
        // Disconnect from session so that the updates on updatedSystemConfiguration are not directly saved in db
        em.detach(updatedSystemConfiguration);
        updatedSystemConfiguration.currentPeriodStartMoment(UPDATED_CURRENT_PERIOD_START_MOMENT);
        SystemConfigurationDTO systemConfigurationDTO = systemConfigurationMapper.toDto(updatedSystemConfiguration);

        restSystemConfigurationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, systemConfigurationDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(systemConfigurationDTO))
            )
            .andExpect(status().isOk());

        // Validate the SystemConfiguration in the database
        List<SystemConfiguration> systemConfigurationList = systemConfigurationRepository.findAll();
        assertThat(systemConfigurationList).hasSize(databaseSizeBeforeUpdate);
        SystemConfiguration testSystemConfiguration = systemConfigurationList.get(systemConfigurationList.size() - 1);
        assertThat(testSystemConfiguration.getCurrentPeriodStartMoment()).isEqualTo(UPDATED_CURRENT_PERIOD_START_MOMENT);
    }

    @Test
    @Transactional
    void putNonExistingSystemConfiguration() throws Exception {
        int databaseSizeBeforeUpdate = systemConfigurationRepository.findAll().size();
        systemConfiguration.setId(count.incrementAndGet());

        // Create the SystemConfiguration
        SystemConfigurationDTO systemConfigurationDTO = systemConfigurationMapper.toDto(systemConfiguration);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSystemConfigurationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, systemConfigurationDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(systemConfigurationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SystemConfiguration in the database
        List<SystemConfiguration> systemConfigurationList = systemConfigurationRepository.findAll();
        assertThat(systemConfigurationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSystemConfiguration() throws Exception {
        int databaseSizeBeforeUpdate = systemConfigurationRepository.findAll().size();
        systemConfiguration.setId(count.incrementAndGet());

        // Create the SystemConfiguration
        SystemConfigurationDTO systemConfigurationDTO = systemConfigurationMapper.toDto(systemConfiguration);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSystemConfigurationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(systemConfigurationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SystemConfiguration in the database
        List<SystemConfiguration> systemConfigurationList = systemConfigurationRepository.findAll();
        assertThat(systemConfigurationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSystemConfiguration() throws Exception {
        int databaseSizeBeforeUpdate = systemConfigurationRepository.findAll().size();
        systemConfiguration.setId(count.incrementAndGet());

        // Create the SystemConfiguration
        SystemConfigurationDTO systemConfigurationDTO = systemConfigurationMapper.toDto(systemConfiguration);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSystemConfigurationMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(systemConfigurationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SystemConfiguration in the database
        List<SystemConfiguration> systemConfigurationList = systemConfigurationRepository.findAll();
        assertThat(systemConfigurationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSystemConfigurationWithPatch() throws Exception {
        // Initialize the database
        systemConfigurationRepository.saveAndFlush(systemConfiguration);

        int databaseSizeBeforeUpdate = systemConfigurationRepository.findAll().size();

        // Update the systemConfiguration using partial update
        SystemConfiguration partialUpdatedSystemConfiguration = new SystemConfiguration();
        partialUpdatedSystemConfiguration.setId(systemConfiguration.getId());

        restSystemConfigurationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSystemConfiguration.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSystemConfiguration))
            )
            .andExpect(status().isOk());

        // Validate the SystemConfiguration in the database
        List<SystemConfiguration> systemConfigurationList = systemConfigurationRepository.findAll();
        assertThat(systemConfigurationList).hasSize(databaseSizeBeforeUpdate);
        SystemConfiguration testSystemConfiguration = systemConfigurationList.get(systemConfigurationList.size() - 1);
        assertThat(testSystemConfiguration.getCurrentPeriodStartMoment()).isEqualTo(DEFAULT_CURRENT_PERIOD_START_MOMENT);
    }

    @Test
    @Transactional
    void fullUpdateSystemConfigurationWithPatch() throws Exception {
        // Initialize the database
        systemConfigurationRepository.saveAndFlush(systemConfiguration);

        int databaseSizeBeforeUpdate = systemConfigurationRepository.findAll().size();

        // Update the systemConfiguration using partial update
        SystemConfiguration partialUpdatedSystemConfiguration = new SystemConfiguration();
        partialUpdatedSystemConfiguration.setId(systemConfiguration.getId());

        partialUpdatedSystemConfiguration.currentPeriodStartMoment(UPDATED_CURRENT_PERIOD_START_MOMENT);

        restSystemConfigurationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSystemConfiguration.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSystemConfiguration))
            )
            .andExpect(status().isOk());

        // Validate the SystemConfiguration in the database
        List<SystemConfiguration> systemConfigurationList = systemConfigurationRepository.findAll();
        assertThat(systemConfigurationList).hasSize(databaseSizeBeforeUpdate);
        SystemConfiguration testSystemConfiguration = systemConfigurationList.get(systemConfigurationList.size() - 1);
        assertThat(testSystemConfiguration.getCurrentPeriodStartMoment()).isEqualTo(UPDATED_CURRENT_PERIOD_START_MOMENT);
    }

    @Test
    @Transactional
    void patchNonExistingSystemConfiguration() throws Exception {
        int databaseSizeBeforeUpdate = systemConfigurationRepository.findAll().size();
        systemConfiguration.setId(count.incrementAndGet());

        // Create the SystemConfiguration
        SystemConfigurationDTO systemConfigurationDTO = systemConfigurationMapper.toDto(systemConfiguration);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSystemConfigurationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, systemConfigurationDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(systemConfigurationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SystemConfiguration in the database
        List<SystemConfiguration> systemConfigurationList = systemConfigurationRepository.findAll();
        assertThat(systemConfigurationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSystemConfiguration() throws Exception {
        int databaseSizeBeforeUpdate = systemConfigurationRepository.findAll().size();
        systemConfiguration.setId(count.incrementAndGet());

        // Create the SystemConfiguration
        SystemConfigurationDTO systemConfigurationDTO = systemConfigurationMapper.toDto(systemConfiguration);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSystemConfigurationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(systemConfigurationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SystemConfiguration in the database
        List<SystemConfiguration> systemConfigurationList = systemConfigurationRepository.findAll();
        assertThat(systemConfigurationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSystemConfiguration() throws Exception {
        int databaseSizeBeforeUpdate = systemConfigurationRepository.findAll().size();
        systemConfiguration.setId(count.incrementAndGet());

        // Create the SystemConfiguration
        SystemConfigurationDTO systemConfigurationDTO = systemConfigurationMapper.toDto(systemConfiguration);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSystemConfigurationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(systemConfigurationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SystemConfiguration in the database
        List<SystemConfiguration> systemConfigurationList = systemConfigurationRepository.findAll();
        assertThat(systemConfigurationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSystemConfiguration() throws Exception {
        // Initialize the database
        systemConfigurationRepository.saveAndFlush(systemConfiguration);

        int databaseSizeBeforeDelete = systemConfigurationRepository.findAll().size();

        // Delete the systemConfiguration
        restSystemConfigurationMockMvc
            .perform(delete(ENTITY_API_URL_ID, systemConfiguration.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SystemConfiguration> systemConfigurationList = systemConfigurationRepository.findAll();
        assertThat(systemConfigurationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
