package grafismo.service;

import grafismo.domain.LocalAssociationRegion;
import grafismo.repository.LocalAssociationRegionRepository;
import grafismo.service.dto.LocalAssociationRegionDTO;
import grafismo.service.mapper.LocalAssociationRegionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link LocalAssociationRegion}.
 */
@Service
@Transactional
public class LocalAssociationRegionService {

    private final Logger log = LoggerFactory.getLogger(LocalAssociationRegionService.class);

    private final LocalAssociationRegionRepository localAssociationRegionRepository;

    private final LocalAssociationRegionMapper localAssociationRegionMapper;

    public LocalAssociationRegionService(
        LocalAssociationRegionRepository localAssociationRegionRepository,
        LocalAssociationRegionMapper localAssociationRegionMapper
    ) {
        this.localAssociationRegionRepository = localAssociationRegionRepository;
        this.localAssociationRegionMapper = localAssociationRegionMapper;
    }

    /**
     * Save a localAssociationRegion.
     *
     * @param localAssociationRegionDTO the entity to save.
     * @return the persisted entity.
     */
    public LocalAssociationRegionDTO save(LocalAssociationRegionDTO localAssociationRegionDTO) {
        log.debug("Request to save LocalAssociationRegion : {}", localAssociationRegionDTO);
        LocalAssociationRegion localAssociationRegion = localAssociationRegionMapper.toEntity(localAssociationRegionDTO);
        localAssociationRegion = localAssociationRegionRepository.save(localAssociationRegion);
        return localAssociationRegionMapper.toDto(localAssociationRegion);
    }

    /**
     * Update a localAssociationRegion.
     *
     * @param localAssociationRegionDTO the entity to save.
     * @return the persisted entity.
     */
    public LocalAssociationRegionDTO update(LocalAssociationRegionDTO localAssociationRegionDTO) {
        log.debug("Request to save LocalAssociationRegion : {}", localAssociationRegionDTO);
        LocalAssociationRegion localAssociationRegion = localAssociationRegionMapper.toEntity(localAssociationRegionDTO);
        localAssociationRegion = localAssociationRegionRepository.save(localAssociationRegion);
        return localAssociationRegionMapper.toDto(localAssociationRegion);
    }

    /**
     * Partially update a localAssociationRegion.
     *
     * @param localAssociationRegionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<LocalAssociationRegionDTO> partialUpdate(LocalAssociationRegionDTO localAssociationRegionDTO) {
        log.debug("Request to partially update LocalAssociationRegion : {}", localAssociationRegionDTO);

        return localAssociationRegionRepository
            .findById(localAssociationRegionDTO.getId())
            .map(existingLocalAssociationRegion -> {
                localAssociationRegionMapper.partialUpdate(existingLocalAssociationRegion, localAssociationRegionDTO);

                return existingLocalAssociationRegion;
            })
            .map(localAssociationRegionRepository::save)
            .map(localAssociationRegionMapper::toDto);
    }

    /**
     * Get all the localAssociationRegions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<LocalAssociationRegionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all LocalAssociationRegions");
        return localAssociationRegionRepository.findAll(pageable).map(localAssociationRegionMapper::toDto);
    }

    /**
     * Get all the localAssociationRegions with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<LocalAssociationRegionDTO> findAllWithEagerRelationships(Pageable pageable) {
        return localAssociationRegionRepository.findAllWithEagerRelationships(pageable).map(localAssociationRegionMapper::toDto);
    }

    /**
     * Get one localAssociationRegion by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<LocalAssociationRegionDTO> findOne(Long id) {
        log.debug("Request to get LocalAssociationRegion : {}", id);
        return localAssociationRegionRepository.findOneWithEagerRelationships(id).map(localAssociationRegionMapper::toDto);
    }

    /**
     * Delete the localAssociationRegion by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete LocalAssociationRegion : {}", id);
        localAssociationRegionRepository.deleteById(id);
    }
}
