package grafismo.service;

import grafismo.domain.MatchPlayer;
import grafismo.repository.MatchPlayerRepository;
import grafismo.service.dto.MatchPlayerDTO;
import grafismo.service.mapper.MatchPlayerMapper;
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
 * Service Implementation for managing {@link MatchPlayer}.
 */
@Service
@Transactional
public class MatchPlayerService {

    private final Logger log = LoggerFactory.getLogger(MatchPlayerService.class);

    private final MatchPlayerRepository matchPlayerRepository;

    private final MatchPlayerMapper matchPlayerMapper;

    public MatchPlayerService(MatchPlayerRepository matchPlayerRepository, MatchPlayerMapper matchPlayerMapper) {
        this.matchPlayerRepository = matchPlayerRepository;
        this.matchPlayerMapper = matchPlayerMapper;
    }

    /**
     * Save a matchPlayer.
     *
     * @param matchPlayerDTO the entity to save.
     * @return the persisted entity.
     */
    public MatchPlayerDTO save(MatchPlayerDTO matchPlayerDTO) {
        log.debug("Request to save MatchPlayer : {}", matchPlayerDTO);
        MatchPlayer matchPlayer = matchPlayerMapper.toEntity(matchPlayerDTO);
        matchPlayer = matchPlayerRepository.save(matchPlayer);
        return matchPlayerMapper.toDto(matchPlayer);
    }

    /**
     * Update a matchPlayer.
     *
     * @param matchPlayerDTO the entity to save.
     * @return the persisted entity.
     */
    public MatchPlayerDTO update(MatchPlayerDTO matchPlayerDTO) {
        log.debug("Request to save MatchPlayer : {}", matchPlayerDTO);
        MatchPlayer matchPlayer = matchPlayerMapper.toEntity(matchPlayerDTO);
        matchPlayer = matchPlayerRepository.save(matchPlayer);
        return matchPlayerMapper.toDto(matchPlayer);
    }

    /**
     * Partially update a matchPlayer.
     *
     * @param matchPlayerDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MatchPlayerDTO> partialUpdate(MatchPlayerDTO matchPlayerDTO) {
        log.debug("Request to partially update MatchPlayer : {}", matchPlayerDTO);

        return matchPlayerRepository
            .findById(matchPlayerDTO.getId())
            .map(existingMatchPlayer -> {
                matchPlayerMapper.partialUpdate(existingMatchPlayer, matchPlayerDTO);

                return existingMatchPlayer;
            })
            .map(matchPlayerRepository::save)
            .map(matchPlayerMapper::toDto);
    }

    /**
     * Get all the matchPlayers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<MatchPlayerDTO> findAll(Pageable pageable) {
        log.debug("Request to get all MatchPlayers");
        return matchPlayerRepository.findAll(pageable).map(matchPlayerMapper::toDto);
    }

    /**
     * Get all the matchPlayers with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<MatchPlayerDTO> findAllWithEagerRelationships(Pageable pageable) {
        return matchPlayerRepository.findAllWithEagerRelationships(pageable).map(matchPlayerMapper::toDto);
    }

    /**
     *  Get all the matchPlayers where MotmMatch is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<MatchPlayerDTO> findAllWhereMotmMatchIsNull() {
        log.debug("Request to get all matchPlayers where MotmMatch is null");
        return StreamSupport
            .stream(matchPlayerRepository.findAll().spliterator(), false)
            .filter(matchPlayer -> matchPlayer.getMotmMatch() == null)
            .map(matchPlayerMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get all the matchPlayers where CaptainCallup is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<MatchPlayerDTO> findAllWhereCaptainCallupIsNull() {
        log.debug("Request to get all matchPlayers where CaptainCallup is null");
        return StreamSupport
            .stream(matchPlayerRepository.findAll().spliterator(), false)
            .filter(matchPlayer -> matchPlayer.getCaptainCallup() == null)
            .map(matchPlayerMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one matchPlayer by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MatchPlayerDTO> findOne(Long id) {
        log.debug("Request to get MatchPlayer : {}", id);
        return matchPlayerRepository.findOneWithEagerRelationships(id).map(matchPlayerMapper::toDto);
    }

    /**
     * Delete the matchPlayer by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete MatchPlayer : {}", id);
        matchPlayerRepository.deleteById(id);
    }
}
