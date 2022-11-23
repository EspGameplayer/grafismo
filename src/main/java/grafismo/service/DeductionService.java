package grafismo.service;

import grafismo.domain.Deduction;
import grafismo.repository.DeductionRepository;
import grafismo.service.dto.DeductionDTO;
import grafismo.service.mapper.DeductionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Deduction}.
 */
@Service
@Transactional
public class DeductionService {

    private final Logger log = LoggerFactory.getLogger(DeductionService.class);

    private final DeductionRepository deductionRepository;

    private final DeductionMapper deductionMapper;

    public DeductionService(DeductionRepository deductionRepository, DeductionMapper deductionMapper) {
        this.deductionRepository = deductionRepository;
        this.deductionMapper = deductionMapper;
    }

    /**
     * Save a deduction.
     *
     * @param deductionDTO the entity to save.
     * @return the persisted entity.
     */
    public DeductionDTO save(DeductionDTO deductionDTO) {
        log.debug("Request to save Deduction : {}", deductionDTO);
        Deduction deduction = deductionMapper.toEntity(deductionDTO);
        deduction = deductionRepository.save(deduction);
        return deductionMapper.toDto(deduction);
    }

    /**
     * Update a deduction.
     *
     * @param deductionDTO the entity to save.
     * @return the persisted entity.
     */
    public DeductionDTO update(DeductionDTO deductionDTO) {
        log.debug("Request to save Deduction : {}", deductionDTO);
        Deduction deduction = deductionMapper.toEntity(deductionDTO);
        deduction = deductionRepository.save(deduction);
        return deductionMapper.toDto(deduction);
    }

    /**
     * Partially update a deduction.
     *
     * @param deductionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DeductionDTO> partialUpdate(DeductionDTO deductionDTO) {
        log.debug("Request to partially update Deduction : {}", deductionDTO);

        return deductionRepository
            .findById(deductionDTO.getId())
            .map(existingDeduction -> {
                deductionMapper.partialUpdate(existingDeduction, deductionDTO);

                return existingDeduction;
            })
            .map(deductionRepository::save)
            .map(deductionMapper::toDto);
    }

    /**
     * Get all the deductions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DeductionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Deductions");
        return deductionRepository.findAll(pageable).map(deductionMapper::toDto);
    }

    /**
     * Get all the deductions with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<DeductionDTO> findAllWithEagerRelationships(Pageable pageable) {
        return deductionRepository.findAllWithEagerRelationships(pageable).map(deductionMapper::toDto);
    }

    /**
     * Get one deduction by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DeductionDTO> findOne(Long id) {
        log.debug("Request to get Deduction : {}", id);
        return deductionRepository.findOneWithEagerRelationships(id).map(deductionMapper::toDto);
    }

    /**
     * Delete the deduction by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Deduction : {}", id);
        deductionRepository.deleteById(id);
    }
}
