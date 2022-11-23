package grafismo.service;

import grafismo.domain.Match;
import grafismo.repository.MatchRepository;
import grafismo.service.dto.MatchDTO;
import grafismo.service.mapper.MatchMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Match}.
 */
@Service
@Transactional
public class MatchService {

    private final Logger log = LoggerFactory.getLogger(MatchService.class);

    private final MatchRepository matchRepository;

    private final MatchMapper matchMapper;

    public MatchService(MatchRepository matchRepository, MatchMapper matchMapper) {
        this.matchRepository = matchRepository;
        this.matchMapper = matchMapper;
    }

    /**
     * Save a match.
     *
     * @param matchDTO the entity to save.
     * @return the persisted entity.
     */
    public MatchDTO save(MatchDTO matchDTO) {
        log.debug("Request to save Match : {}", matchDTO);
        Match match = matchMapper.toEntity(matchDTO);
        match = matchRepository.save(match);
        return matchMapper.toDto(match);
    }

    /**
     * Update a match.
     *
     * @param matchDTO the entity to save.
     * @return the persisted entity.
     */
    public MatchDTO update(MatchDTO matchDTO) {
        log.debug("Request to save Match : {}", matchDTO);
        Match match = matchMapper.toEntity(matchDTO);
        match = matchRepository.save(match);
        return matchMapper.toDto(match);
    }

    /**
     * Partially update a match.
     *
     * @param matchDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MatchDTO> partialUpdate(MatchDTO matchDTO) {
        log.debug("Request to partially update Match : {}", matchDTO);

        return matchRepository
            .findById(matchDTO.getId())
            .map(existingMatch -> {
                matchMapper.partialUpdate(existingMatch, matchDTO);

                return existingMatch;
            })
            .map(matchRepository::save)
            .map(matchMapper::toDto);
    }

    /**
     * Get all the matches.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<MatchDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Matches");
        return matchRepository.findAll(pageable).map(matchMapper::toDto);
    }

    /**
     * Get all the matches with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<MatchDTO> findAllWithEagerRelationships(Pageable pageable) {
        return matchRepository.findAllWithEagerRelationships(pageable).map(matchMapper::toDto);
    }

    /**
     * Get one match by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MatchDTO> findOne(Long id) {
        log.debug("Request to get Match : {}", id);
        return matchRepository.findOneWithEagerRelationships(id).map(matchMapper::toDto);
    }

    /**
     * Delete the match by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Match : {}", id);
        matchRepository.deleteById(id);
    }
}
