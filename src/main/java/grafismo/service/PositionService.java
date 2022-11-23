package grafismo.service;

import grafismo.domain.Position;
import grafismo.repository.PositionRepository;
import grafismo.service.dto.PositionDTO;
import grafismo.service.mapper.PositionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Position}.
 */
@Service
@Transactional
public class PositionService {

    private final Logger log = LoggerFactory.getLogger(PositionService.class);

    private final PositionRepository positionRepository;

    private final PositionMapper positionMapper;

    public PositionService(PositionRepository positionRepository, PositionMapper positionMapper) {
        this.positionRepository = positionRepository;
        this.positionMapper = positionMapper;
    }

    /**
     * Save a position.
     *
     * @param positionDTO the entity to save.
     * @return the persisted entity.
     */
    public PositionDTO save(PositionDTO positionDTO) {
        log.debug("Request to save Position : {}", positionDTO);
        Position position = positionMapper.toEntity(positionDTO);
        position = positionRepository.save(position);
        return positionMapper.toDto(position);
    }

    /**
     * Update a position.
     *
     * @param positionDTO the entity to save.
     * @return the persisted entity.
     */
    public PositionDTO update(PositionDTO positionDTO) {
        log.debug("Request to save Position : {}", positionDTO);
        Position position = positionMapper.toEntity(positionDTO);
        position = positionRepository.save(position);
        return positionMapper.toDto(position);
    }

    /**
     * Partially update a position.
     *
     * @param positionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PositionDTO> partialUpdate(PositionDTO positionDTO) {
        log.debug("Request to partially update Position : {}", positionDTO);

        return positionRepository
            .findById(positionDTO.getId())
            .map(existingPosition -> {
                positionMapper.partialUpdate(existingPosition, positionDTO);

                return existingPosition;
            })
            .map(positionRepository::save)
            .map(positionMapper::toDto);
    }

    /**
     * Get all the positions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PositionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Positions");
        return positionRepository.findAll(pageable).map(positionMapper::toDto);
    }

    /**
     * Get all the positions with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<PositionDTO> findAllWithEagerRelationships(Pageable pageable) {
        return positionRepository.findAllWithEagerRelationships(pageable).map(positionMapper::toDto);
    }

    /**
     * Get one position by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PositionDTO> findOne(Long id) {
        log.debug("Request to get Position : {}", id);
        return positionRepository.findOneWithEagerRelationships(id).map(positionMapper::toDto);
    }

    /**
     * Delete the position by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Position : {}", id);
        positionRepository.deleteById(id);
    }
}
