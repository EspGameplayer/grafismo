package grafismo.service;

import grafismo.domain.Injury;
import grafismo.repository.InjuryRepository;
import grafismo.service.dto.InjuryDTO;
import grafismo.service.mapper.InjuryMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Injury}.
 */
@Service
@Transactional
public class InjuryService {

    private final Logger log = LoggerFactory.getLogger(InjuryService.class);

    private final InjuryRepository injuryRepository;

    private final InjuryMapper injuryMapper;

    public InjuryService(InjuryRepository injuryRepository, InjuryMapper injuryMapper) {
        this.injuryRepository = injuryRepository;
        this.injuryMapper = injuryMapper;
    }

    /**
     * Save a injury.
     *
     * @param injuryDTO the entity to save.
     * @return the persisted entity.
     */
    public InjuryDTO save(InjuryDTO injuryDTO) {
        log.debug("Request to save Injury : {}", injuryDTO);
        Injury injury = injuryMapper.toEntity(injuryDTO);
        injury = injuryRepository.save(injury);
        return injuryMapper.toDto(injury);
    }

    /**
     * Update a injury.
     *
     * @param injuryDTO the entity to save.
     * @return the persisted entity.
     */
    public InjuryDTO update(InjuryDTO injuryDTO) {
        log.debug("Request to save Injury : {}", injuryDTO);
        Injury injury = injuryMapper.toEntity(injuryDTO);
        injury = injuryRepository.save(injury);
        return injuryMapper.toDto(injury);
    }

    /**
     * Partially update a injury.
     *
     * @param injuryDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<InjuryDTO> partialUpdate(InjuryDTO injuryDTO) {
        log.debug("Request to partially update Injury : {}", injuryDTO);

        return injuryRepository
            .findById(injuryDTO.getId())
            .map(existingInjury -> {
                injuryMapper.partialUpdate(existingInjury, injuryDTO);

                return existingInjury;
            })
            .map(injuryRepository::save)
            .map(injuryMapper::toDto);
    }

    /**
     * Get all the injuries.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<InjuryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Injuries");
        return injuryRepository.findAll(pageable).map(injuryMapper::toDto);
    }

    /**
     * Get one injury by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<InjuryDTO> findOne(Long id) {
        log.debug("Request to get Injury : {}", id);
        return injuryRepository.findById(id).map(injuryMapper::toDto);
    }

    /**
     * Delete the injury by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Injury : {}", id);
        injuryRepository.deleteById(id);
    }
}
