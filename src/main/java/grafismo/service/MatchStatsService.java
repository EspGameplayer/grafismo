package grafismo.service;

import grafismo.domain.MatchStats;
import grafismo.repository.MatchStatsRepository;
import grafismo.service.dto.MatchStatsDTO;
import grafismo.service.mapper.MatchStatsMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link MatchStats}.
 */
@Service
@Transactional
public class MatchStatsService {

    private final Logger log = LoggerFactory.getLogger(MatchStatsService.class);

    private final MatchStatsRepository matchStatsRepository;

    private final MatchStatsMapper matchStatsMapper;

    public MatchStatsService(MatchStatsRepository matchStatsRepository, MatchStatsMapper matchStatsMapper) {
        this.matchStatsRepository = matchStatsRepository;
        this.matchStatsMapper = matchStatsMapper;
    }

    /**
     * Save a matchStats.
     *
     * @param matchStatsDTO the entity to save.
     * @return the persisted entity.
     */
    public MatchStatsDTO save(MatchStatsDTO matchStatsDTO) {
        log.debug("Request to save MatchStats : {}", matchStatsDTO);
        MatchStats matchStats = matchStatsMapper.toEntity(matchStatsDTO);
        matchStats = matchStatsRepository.save(matchStats);
        return matchStatsMapper.toDto(matchStats);
    }

    /**
     * Update a matchStats.
     *
     * @param matchStatsDTO the entity to save.
     * @return the persisted entity.
     */
    public MatchStatsDTO update(MatchStatsDTO matchStatsDTO) {
        log.debug("Request to save MatchStats : {}", matchStatsDTO);
        MatchStats matchStats = matchStatsMapper.toEntity(matchStatsDTO);
        matchStats = matchStatsRepository.save(matchStats);
        return matchStatsMapper.toDto(matchStats);
    }

    /**
     * Partially update a matchStats.
     *
     * @param matchStatsDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MatchStatsDTO> partialUpdate(MatchStatsDTO matchStatsDTO) {
        log.debug("Request to partially update MatchStats : {}", matchStatsDTO);

        return matchStatsRepository
            .findById(matchStatsDTO.getId())
            .map(existingMatchStats -> {
                matchStatsMapper.partialUpdate(existingMatchStats, matchStatsDTO);

                return existingMatchStats;
            })
            .map(matchStatsRepository::save)
            .map(matchStatsMapper::toDto);
    }

    /**
     * Get all the matchStats.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<MatchStatsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all MatchStats");
        return matchStatsRepository.findAll(pageable).map(matchStatsMapper::toDto);
    }

    /**
     * Get one matchStats by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MatchStatsDTO> findOne(Long id) {
        log.debug("Request to get MatchStats : {}", id);
        return matchStatsRepository.findById(id).map(matchStatsMapper::toDto);
    }

    /**
     * Delete the matchStats by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete MatchStats : {}", id);
        matchStatsRepository.deleteById(id);
    }
}
