package grafismo.service;

import grafismo.domain.Action;
import grafismo.repository.ActionRepository;
import grafismo.service.dto.ActionDTO;
import grafismo.service.mapper.ActionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Action}.
 */
@Service
@Transactional
public class ActionService {

    private final Logger log = LoggerFactory.getLogger(ActionService.class);

    private final ActionRepository actionRepository;

    private final ActionMapper actionMapper;

    public ActionService(ActionRepository actionRepository, ActionMapper actionMapper) {
        this.actionRepository = actionRepository;
        this.actionMapper = actionMapper;
    }

    /**
     * Save a action.
     *
     * @param actionDTO the entity to save.
     * @return the persisted entity.
     */
    public ActionDTO save(ActionDTO actionDTO) {
        log.debug("Request to save Action : {}", actionDTO);
        Action action = actionMapper.toEntity(actionDTO);
        action = actionRepository.save(action);
        return actionMapper.toDto(action);
    }

    /**
     * Update a action.
     *
     * @param actionDTO the entity to save.
     * @return the persisted entity.
     */
    public ActionDTO update(ActionDTO actionDTO) {
        log.debug("Request to save Action : {}", actionDTO);
        Action action = actionMapper.toEntity(actionDTO);
        action = actionRepository.save(action);
        return actionMapper.toDto(action);
    }

    /**
     * Partially update a action.
     *
     * @param actionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ActionDTO> partialUpdate(ActionDTO actionDTO) {
        log.debug("Request to partially update Action : {}", actionDTO);

        return actionRepository
            .findById(actionDTO.getId())
            .map(existingAction -> {
                actionMapper.partialUpdate(existingAction, actionDTO);

                return existingAction;
            })
            .map(actionRepository::save)
            .map(actionMapper::toDto);
    }

    /**
     * Get all the actions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ActionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Actions");
        return actionRepository.findAll(pageable).map(actionMapper::toDto);
    }

    /**
     * Get all the actions with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ActionDTO> findAllWithEagerRelationships(Pageable pageable) {
        return actionRepository.findAllWithEagerRelationships(pageable).map(actionMapper::toDto);
    }

    /**
     * Get one action by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ActionDTO> findOne(Long id) {
        log.debug("Request to get Action : {}", id);
        return actionRepository.findOneWithEagerRelationships(id).map(actionMapper::toDto);
    }

    /**
     * Delete the action by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Action : {}", id);
        actionRepository.deleteById(id);
    }
}
