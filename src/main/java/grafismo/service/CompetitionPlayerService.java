package grafismo.service;

import grafismo.domain.CompetitionPlayer;
import grafismo.repository.CompetitionPlayerRepository;
import grafismo.service.dto.CompetitionPlayerDTO;
import grafismo.service.mapper.CompetitionPlayerMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CompetitionPlayer}.
 */
@Service
@Transactional
public class CompetitionPlayerService {

    private final Logger log = LoggerFactory.getLogger(CompetitionPlayerService.class);

    private final CompetitionPlayerRepository competitionPlayerRepository;

    private final CompetitionPlayerMapper competitionPlayerMapper;

    public CompetitionPlayerService(
        CompetitionPlayerRepository competitionPlayerRepository,
        CompetitionPlayerMapper competitionPlayerMapper
    ) {
        this.competitionPlayerRepository = competitionPlayerRepository;
        this.competitionPlayerMapper = competitionPlayerMapper;
    }

    /**
     * Save a competitionPlayer.
     *
     * @param competitionPlayerDTO the entity to save.
     * @return the persisted entity.
     */
    public CompetitionPlayerDTO save(CompetitionPlayerDTO competitionPlayerDTO) {
        log.debug("Request to save CompetitionPlayer : {}", competitionPlayerDTO);
        CompetitionPlayer competitionPlayer = competitionPlayerMapper.toEntity(competitionPlayerDTO);
        competitionPlayer = competitionPlayerRepository.save(competitionPlayer);
        return competitionPlayerMapper.toDto(competitionPlayer);
    }

    /**
     * Update a competitionPlayer.
     *
     * @param competitionPlayerDTO the entity to save.
     * @return the persisted entity.
     */
    public CompetitionPlayerDTO update(CompetitionPlayerDTO competitionPlayerDTO) {
        log.debug("Request to save CompetitionPlayer : {}", competitionPlayerDTO);
        CompetitionPlayer competitionPlayer = competitionPlayerMapper.toEntity(competitionPlayerDTO);
        competitionPlayer = competitionPlayerRepository.save(competitionPlayer);
        return competitionPlayerMapper.toDto(competitionPlayer);
    }

    /**
     * Partially update a competitionPlayer.
     *
     * @param competitionPlayerDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CompetitionPlayerDTO> partialUpdate(CompetitionPlayerDTO competitionPlayerDTO) {
        log.debug("Request to partially update CompetitionPlayer : {}", competitionPlayerDTO);

        return competitionPlayerRepository
            .findById(competitionPlayerDTO.getId())
            .map(existingCompetitionPlayer -> {
                competitionPlayerMapper.partialUpdate(existingCompetitionPlayer, competitionPlayerDTO);

                return existingCompetitionPlayer;
            })
            .map(competitionPlayerRepository::save)
            .map(competitionPlayerMapper::toDto);
    }

    /**
     * Get all the competitionPlayers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CompetitionPlayerDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CompetitionPlayers");
        return competitionPlayerRepository.findAll(pageable).map(competitionPlayerMapper::toDto);
    }

    /**
     * Get all the competitionPlayers with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<CompetitionPlayerDTO> findAllWithEagerRelationships(Pageable pageable) {
        return competitionPlayerRepository.findAllWithEagerRelationships(pageable).map(competitionPlayerMapper::toDto);
    }

    /**
     * Get one competitionPlayer by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CompetitionPlayerDTO> findOne(Long id) {
        log.debug("Request to get CompetitionPlayer : {}", id);
        return competitionPlayerRepository.findOneWithEagerRelationships(id).map(competitionPlayerMapper::toDto);
    }

    /**
     * Delete the competitionPlayer by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CompetitionPlayer : {}", id);
        competitionPlayerRepository.deleteById(id);
    }
}
