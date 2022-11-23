package grafismo.service;

import grafismo.domain.Callup;
import grafismo.repository.CallupRepository;
import grafismo.service.dto.CallupDTO;
import grafismo.service.mapper.CallupMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Callup}.
 */
@Service
@Transactional
public class CallupService {

    private final Logger log = LoggerFactory.getLogger(CallupService.class);

    private final CallupRepository callupRepository;

    private final CallupMapper callupMapper;

    public CallupService(CallupRepository callupRepository, CallupMapper callupMapper) {
        this.callupRepository = callupRepository;
        this.callupMapper = callupMapper;
    }

    /**
     * Save a callup.
     *
     * @param callupDTO the entity to save.
     * @return the persisted entity.
     */
    public CallupDTO save(CallupDTO callupDTO) {
        log.debug("Request to save Callup : {}", callupDTO);
        Callup callup = callupMapper.toEntity(callupDTO);
        callup = callupRepository.save(callup);
        return callupMapper.toDto(callup);
    }

    /**
     * Update a callup.
     *
     * @param callupDTO the entity to save.
     * @return the persisted entity.
     */
    public CallupDTO update(CallupDTO callupDTO) {
        log.debug("Request to save Callup : {}", callupDTO);
        Callup callup = callupMapper.toEntity(callupDTO);
        callup = callupRepository.save(callup);
        return callupMapper.toDto(callup);
    }

    /**
     * Partially update a callup.
     *
     * @param callupDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CallupDTO> partialUpdate(CallupDTO callupDTO) {
        log.debug("Request to partially update Callup : {}", callupDTO);

        return callupRepository
            .findById(callupDTO.getId())
            .map(existingCallup -> {
                callupMapper.partialUpdate(existingCallup, callupDTO);

                return existingCallup;
            })
            .map(callupRepository::save)
            .map(callupMapper::toDto);
    }

    /**
     * Get all the callups.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CallupDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Callups");
        return callupRepository.findAll(pageable).map(callupMapper::toDto);
    }

    /**
     * Get all the callups with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<CallupDTO> findAllWithEagerRelationships(Pageable pageable) {
        return callupRepository.findAllWithEagerRelationships(pageable).map(callupMapper::toDto);
    }

    /**
     *  Get all the callups where HomeMatch is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<CallupDTO> findAllWhereHomeMatchIsNull() {
        log.debug("Request to get all callups where HomeMatch is null");
        return StreamSupport
            .stream(callupRepository.findAll().spliterator(), false)
            .filter(callup -> callup.getHomeMatch() == null)
            .map(callupMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get all the callups where AwayMatch is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<CallupDTO> findAllWhereAwayMatchIsNull() {
        log.debug("Request to get all callups where AwayMatch is null");
        return StreamSupport
            .stream(callupRepository.findAll().spliterator(), false)
            .filter(callup -> callup.getAwayMatch() == null)
            .map(callupMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one callup by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CallupDTO> findOne(Long id) {
        log.debug("Request to get Callup : {}", id);
        return callupRepository.findOneWithEagerRelationships(id).map(callupMapper::toDto);
    }

    /**
     * Delete the callup by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Callup : {}", id);
        callupRepository.deleteById(id);
    }
}
