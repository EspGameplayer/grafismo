package grafismo.service;

import grafismo.domain.SystemConfiguration;
import grafismo.repository.SystemConfigurationRepository;
import grafismo.service.dto.SystemConfigurationDTO;
import grafismo.service.mapper.SystemConfigurationMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SystemConfiguration}.
 */
@Service
@Transactional
public class SystemConfigurationService {

    private final Logger log = LoggerFactory.getLogger(SystemConfigurationService.class);

    private final SystemConfigurationRepository systemConfigurationRepository;

    private final SystemConfigurationMapper systemConfigurationMapper;

    public SystemConfigurationService(
        SystemConfigurationRepository systemConfigurationRepository,
        SystemConfigurationMapper systemConfigurationMapper
    ) {
        this.systemConfigurationRepository = systemConfigurationRepository;
        this.systemConfigurationMapper = systemConfigurationMapper;
    }

    /**
     * Save a systemConfiguration.
     *
     * @param systemConfigurationDTO the entity to save.
     * @return the persisted entity.
     */
    public SystemConfigurationDTO save(SystemConfigurationDTO systemConfigurationDTO) {
        log.debug("Request to save SystemConfiguration : {}", systemConfigurationDTO);
        SystemConfiguration systemConfiguration = systemConfigurationMapper.toEntity(systemConfigurationDTO);
        systemConfiguration = systemConfigurationRepository.save(systemConfiguration);
        return systemConfigurationMapper.toDto(systemConfiguration);
    }

    /**
     * Update a systemConfiguration.
     *
     * @param systemConfigurationDTO the entity to save.
     * @return the persisted entity.
     */
    public SystemConfigurationDTO update(SystemConfigurationDTO systemConfigurationDTO) {
        log.debug("Request to save SystemConfiguration : {}", systemConfigurationDTO);
        SystemConfiguration systemConfiguration = systemConfigurationMapper.toEntity(systemConfigurationDTO);
        systemConfiguration = systemConfigurationRepository.save(systemConfiguration);
        return systemConfigurationMapper.toDto(systemConfiguration);
    }

    /**
     * Partially update a systemConfiguration.
     *
     * @param systemConfigurationDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SystemConfigurationDTO> partialUpdate(SystemConfigurationDTO systemConfigurationDTO) {
        log.debug("Request to partially update SystemConfiguration : {}", systemConfigurationDTO);

        return systemConfigurationRepository
            .findById(systemConfigurationDTO.getId())
            .map(existingSystemConfiguration -> {
                systemConfigurationMapper.partialUpdate(existingSystemConfiguration, systemConfigurationDTO);

                return existingSystemConfiguration;
            })
            .map(systemConfigurationRepository::save)
            .map(systemConfigurationMapper::toDto);
    }

    /**
     * Get all the systemConfigurations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SystemConfigurationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SystemConfigurations");
        return systemConfigurationRepository.findAll(pageable).map(systemConfigurationMapper::toDto);
    }

    /**
     * Get all the systemConfigurations with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<SystemConfigurationDTO> findAllWithEagerRelationships(Pageable pageable) {
        return systemConfigurationRepository.findAllWithEagerRelationships(pageable).map(systemConfigurationMapper::toDto);
    }

    /**
     * Get one systemConfiguration by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SystemConfigurationDTO> findOne(Long id) {
        log.debug("Request to get SystemConfiguration : {}", id);
        return systemConfigurationRepository.findOneWithEagerRelationships(id).map(systemConfigurationMapper::toDto);
    }

    /**
     * Delete the systemConfiguration by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete SystemConfiguration : {}", id);
        systemConfigurationRepository.deleteById(id);
    }
}
