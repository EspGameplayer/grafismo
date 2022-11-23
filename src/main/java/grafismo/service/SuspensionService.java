package grafismo.service;

import grafismo.domain.Suspension;
import grafismo.repository.SuspensionRepository;
import grafismo.service.dto.SuspensionDTO;
import grafismo.service.mapper.SuspensionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Suspension}.
 */
@Service
@Transactional
public class SuspensionService {

    private final Logger log = LoggerFactory.getLogger(SuspensionService.class);

    private final SuspensionRepository suspensionRepository;

    private final SuspensionMapper suspensionMapper;

    public SuspensionService(SuspensionRepository suspensionRepository, SuspensionMapper suspensionMapper) {
        this.suspensionRepository = suspensionRepository;
        this.suspensionMapper = suspensionMapper;
    }

    /**
     * Save a suspension.
     *
     * @param suspensionDTO the entity to save.
     * @return the persisted entity.
     */
    public SuspensionDTO save(SuspensionDTO suspensionDTO) {
        log.debug("Request to save Suspension : {}", suspensionDTO);
        Suspension suspension = suspensionMapper.toEntity(suspensionDTO);
        suspension = suspensionRepository.save(suspension);
        return suspensionMapper.toDto(suspension);
    }

    /**
     * Update a suspension.
     *
     * @param suspensionDTO the entity to save.
     * @return the persisted entity.
     */
    public SuspensionDTO update(SuspensionDTO suspensionDTO) {
        log.debug("Request to save Suspension : {}", suspensionDTO);
        Suspension suspension = suspensionMapper.toEntity(suspensionDTO);
        suspension = suspensionRepository.save(suspension);
        return suspensionMapper.toDto(suspension);
    }

    /**
     * Partially update a suspension.
     *
     * @param suspensionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SuspensionDTO> partialUpdate(SuspensionDTO suspensionDTO) {
        log.debug("Request to partially update Suspension : {}", suspensionDTO);

        return suspensionRepository
            .findById(suspensionDTO.getId())
            .map(existingSuspension -> {
                suspensionMapper.partialUpdate(existingSuspension, suspensionDTO);

                return existingSuspension;
            })
            .map(suspensionRepository::save)
            .map(suspensionMapper::toDto);
    }

    /**
     * Get all the suspensions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SuspensionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Suspensions");
        return suspensionRepository.findAll(pageable).map(suspensionMapper::toDto);
    }

    /**
     * Get all the suspensions with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<SuspensionDTO> findAllWithEagerRelationships(Pageable pageable) {
        return suspensionRepository.findAllWithEagerRelationships(pageable).map(suspensionMapper::toDto);
    }

    /**
     * Get one suspension by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SuspensionDTO> findOne(Long id) {
        log.debug("Request to get Suspension : {}", id);
        return suspensionRepository.findOneWithEagerRelationships(id).map(suspensionMapper::toDto);
    }

    /**
     * Delete the suspension by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Suspension : {}", id);
        suspensionRepository.deleteById(id);
    }
}
