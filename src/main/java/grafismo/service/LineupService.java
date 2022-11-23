package grafismo.service;

import grafismo.domain.Lineup;
import grafismo.repository.LineupRepository;
import grafismo.service.dto.LineupDTO;
import grafismo.service.mapper.LineupMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Lineup}.
 */
@Service
@Transactional
public class LineupService {

    private final Logger log = LoggerFactory.getLogger(LineupService.class);

    private final LineupRepository lineupRepository;

    private final LineupMapper lineupMapper;

    public LineupService(LineupRepository lineupRepository, LineupMapper lineupMapper) {
        this.lineupRepository = lineupRepository;
        this.lineupMapper = lineupMapper;
    }

    /**
     * Save a lineup.
     *
     * @param lineupDTO the entity to save.
     * @return the persisted entity.
     */
    public LineupDTO save(LineupDTO lineupDTO) {
        log.debug("Request to save Lineup : {}", lineupDTO);
        Lineup lineup = lineupMapper.toEntity(lineupDTO);
        lineup = lineupRepository.save(lineup);
        return lineupMapper.toDto(lineup);
    }

    /**
     * Update a lineup.
     *
     * @param lineupDTO the entity to save.
     * @return the persisted entity.
     */
    public LineupDTO update(LineupDTO lineupDTO) {
        log.debug("Request to save Lineup : {}", lineupDTO);
        Lineup lineup = lineupMapper.toEntity(lineupDTO);
        lineup = lineupRepository.save(lineup);
        return lineupMapper.toDto(lineup);
    }

    /**
     * Partially update a lineup.
     *
     * @param lineupDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<LineupDTO> partialUpdate(LineupDTO lineupDTO) {
        log.debug("Request to partially update Lineup : {}", lineupDTO);

        return lineupRepository
            .findById(lineupDTO.getId())
            .map(existingLineup -> {
                lineupMapper.partialUpdate(existingLineup, lineupDTO);

                return existingLineup;
            })
            .map(lineupRepository::save)
            .map(lineupMapper::toDto);
    }

    /**
     * Get all the lineups.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<LineupDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Lineups");
        return lineupRepository.findAll(pageable).map(lineupMapper::toDto);
    }

    /**
     * Get all the lineups with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<LineupDTO> findAllWithEagerRelationships(Pageable pageable) {
        return lineupRepository.findAllWithEagerRelationships(pageable).map(lineupMapper::toDto);
    }

    /**
     * Get one lineup by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<LineupDTO> findOne(Long id) {
        log.debug("Request to get Lineup : {}", id);
        return lineupRepository.findOneWithEagerRelationships(id).map(lineupMapper::toDto);
    }

    /**
     * Delete the lineup by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Lineup : {}", id);
        lineupRepository.deleteById(id);
    }
}
