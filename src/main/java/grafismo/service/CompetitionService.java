package grafismo.service;

import grafismo.domain.Competition;
import grafismo.repository.CompetitionRepository;
import grafismo.service.dto.CompetitionDTO;
import grafismo.service.mapper.CompetitionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Competition}.
 */
@Service
@Transactional
public class CompetitionService {

    private final Logger log = LoggerFactory.getLogger(CompetitionService.class);

    private final CompetitionRepository competitionRepository;

    private final CompetitionMapper competitionMapper;

    public CompetitionService(CompetitionRepository competitionRepository, CompetitionMapper competitionMapper) {
        this.competitionRepository = competitionRepository;
        this.competitionMapper = competitionMapper;
    }

    /**
     * Save a competition.
     *
     * @param competitionDTO the entity to save.
     * @return the persisted entity.
     */
    public CompetitionDTO save(CompetitionDTO competitionDTO) {
        log.debug("Request to save Competition : {}", competitionDTO);
        Competition competition = competitionMapper.toEntity(competitionDTO);
        competition = competitionRepository.save(competition);
        return competitionMapper.toDto(competition);
    }

    /**
     * Update a competition.
     *
     * @param competitionDTO the entity to save.
     * @return the persisted entity.
     */
    public CompetitionDTO update(CompetitionDTO competitionDTO) {
        log.debug("Request to save Competition : {}", competitionDTO);
        Competition competition = competitionMapper.toEntity(competitionDTO);
        competition = competitionRepository.save(competition);
        return competitionMapper.toDto(competition);
    }

    /**
     * Partially update a competition.
     *
     * @param competitionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CompetitionDTO> partialUpdate(CompetitionDTO competitionDTO) {
        log.debug("Request to partially update Competition : {}", competitionDTO);

        return competitionRepository
            .findById(competitionDTO.getId())
            .map(existingCompetition -> {
                competitionMapper.partialUpdate(existingCompetition, competitionDTO);

                return existingCompetition;
            })
            .map(competitionRepository::save)
            .map(competitionMapper::toDto);
    }

    /**
     * Get all the competitions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CompetitionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Competitions");
        return competitionRepository.findAll(pageable).map(competitionMapper::toDto);
    }

    /**
     * Get all the competitions with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<CompetitionDTO> findAllWithEagerRelationships(Pageable pageable) {
        return competitionRepository.findAllWithEagerRelationships(pageable).map(competitionMapper::toDto);
    }

    /**
     * Get one competition by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CompetitionDTO> findOne(Long id) {
        log.debug("Request to get Competition : {}", id);
        return competitionRepository.findOneWithEagerRelationships(id).map(competitionMapper::toDto);
    }

    /**
     * Delete the competition by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Competition : {}", id);
        competitionRepository.deleteById(id);
    }
}
