package grafismo.service;

import grafismo.domain.Matchday;
import grafismo.repository.MatchdayRepository;
import grafismo.service.dto.MatchdayDTO;
import grafismo.service.mapper.MatchdayMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Matchday}.
 */
@Service
@Transactional
public class MatchdayService {

    private final Logger log = LoggerFactory.getLogger(MatchdayService.class);

    private final MatchdayRepository matchdayRepository;

    private final MatchdayMapper matchdayMapper;

    public MatchdayService(MatchdayRepository matchdayRepository, MatchdayMapper matchdayMapper) {
        this.matchdayRepository = matchdayRepository;
        this.matchdayMapper = matchdayMapper;
    }

    /**
     * Save a matchday.
     *
     * @param matchdayDTO the entity to save.
     * @return the persisted entity.
     */
    public MatchdayDTO save(MatchdayDTO matchdayDTO) {
        log.debug("Request to save Matchday : {}", matchdayDTO);
        Matchday matchday = matchdayMapper.toEntity(matchdayDTO);
        matchday = matchdayRepository.save(matchday);
        return matchdayMapper.toDto(matchday);
    }

    /**
     * Update a matchday.
     *
     * @param matchdayDTO the entity to save.
     * @return the persisted entity.
     */
    public MatchdayDTO update(MatchdayDTO matchdayDTO) {
        log.debug("Request to save Matchday : {}", matchdayDTO);
        Matchday matchday = matchdayMapper.toEntity(matchdayDTO);
        matchday = matchdayRepository.save(matchday);
        return matchdayMapper.toDto(matchday);
    }

    /**
     * Partially update a matchday.
     *
     * @param matchdayDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MatchdayDTO> partialUpdate(MatchdayDTO matchdayDTO) {
        log.debug("Request to partially update Matchday : {}", matchdayDTO);

        return matchdayRepository
            .findById(matchdayDTO.getId())
            .map(existingMatchday -> {
                matchdayMapper.partialUpdate(existingMatchday, matchdayDTO);

                return existingMatchday;
            })
            .map(matchdayRepository::save)
            .map(matchdayMapper::toDto);
    }

    /**
     * Get all the matchdays.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<MatchdayDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Matchdays");
        return matchdayRepository.findAll(pageable).map(matchdayMapper::toDto);
    }

    /**
     * Get all the matchdays with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<MatchdayDTO> findAllWithEagerRelationships(Pageable pageable) {
        return matchdayRepository.findAllWithEagerRelationships(pageable).map(matchdayMapper::toDto);
    }

    /**
     * Get one matchday by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MatchdayDTO> findOne(Long id) {
        log.debug("Request to get Matchday : {}", id);
        return matchdayRepository.findOneWithEagerRelationships(id).map(matchdayMapper::toDto);
    }

    /**
     * Delete the matchday by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Matchday : {}", id);
        matchdayRepository.deleteById(id);
    }
}
