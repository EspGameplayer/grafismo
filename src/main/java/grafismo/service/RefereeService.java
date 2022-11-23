package grafismo.service;

import grafismo.domain.Referee;
import grafismo.repository.RefereeRepository;
import grafismo.service.dto.RefereeDTO;
import grafismo.service.mapper.RefereeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Referee}.
 */
@Service
@Transactional
public class RefereeService {

    private final Logger log = LoggerFactory.getLogger(RefereeService.class);

    private final RefereeRepository refereeRepository;

    private final RefereeMapper refereeMapper;

    public RefereeService(RefereeRepository refereeRepository, RefereeMapper refereeMapper) {
        this.refereeRepository = refereeRepository;
        this.refereeMapper = refereeMapper;
    }

    /**
     * Save a referee.
     *
     * @param refereeDTO the entity to save.
     * @return the persisted entity.
     */
    public RefereeDTO save(RefereeDTO refereeDTO) {
        log.debug("Request to save Referee : {}", refereeDTO);
        Referee referee = refereeMapper.toEntity(refereeDTO);
        referee = refereeRepository.save(referee);
        return refereeMapper.toDto(referee);
    }

    /**
     * Update a referee.
     *
     * @param refereeDTO the entity to save.
     * @return the persisted entity.
     */
    public RefereeDTO update(RefereeDTO refereeDTO) {
        log.debug("Request to save Referee : {}", refereeDTO);
        Referee referee = refereeMapper.toEntity(refereeDTO);
        referee = refereeRepository.save(referee);
        return refereeMapper.toDto(referee);
    }

    /**
     * Partially update a referee.
     *
     * @param refereeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<RefereeDTO> partialUpdate(RefereeDTO refereeDTO) {
        log.debug("Request to partially update Referee : {}", refereeDTO);

        return refereeRepository
            .findById(refereeDTO.getId())
            .map(existingReferee -> {
                refereeMapper.partialUpdate(existingReferee, refereeDTO);

                return existingReferee;
            })
            .map(refereeRepository::save)
            .map(refereeMapper::toDto);
    }

    /**
     * Get all the referees.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<RefereeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Referees");
        return refereeRepository.findAll(pageable).map(refereeMapper::toDto);
    }

    /**
     * Get all the referees with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<RefereeDTO> findAllWithEagerRelationships(Pageable pageable) {
        return refereeRepository.findAllWithEagerRelationships(pageable).map(refereeMapper::toDto);
    }

    /**
     * Get one referee by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RefereeDTO> findOne(Long id) {
        log.debug("Request to get Referee : {}", id);
        return refereeRepository.findOneWithEagerRelationships(id).map(refereeMapper::toDto);
    }

    /**
     * Delete the referee by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Referee : {}", id);
        refereeRepository.deleteById(id);
    }
}
