package grafismo.service;

import grafismo.domain.ActionKey;
import grafismo.repository.ActionKeyRepository;
import grafismo.service.dto.ActionKeyDTO;
import grafismo.service.mapper.ActionKeyMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ActionKey}.
 */
@Service
@Transactional
public class ActionKeyService {

    private final Logger log = LoggerFactory.getLogger(ActionKeyService.class);

    private final ActionKeyRepository actionKeyRepository;

    private final ActionKeyMapper actionKeyMapper;

    public ActionKeyService(ActionKeyRepository actionKeyRepository, ActionKeyMapper actionKeyMapper) {
        this.actionKeyRepository = actionKeyRepository;
        this.actionKeyMapper = actionKeyMapper;
    }

    /**
     * Save a actionKey.
     *
     * @param actionKeyDTO the entity to save.
     * @return the persisted entity.
     */
    public ActionKeyDTO save(ActionKeyDTO actionKeyDTO) {
        log.debug("Request to save ActionKey : {}", actionKeyDTO);
        ActionKey actionKey = actionKeyMapper.toEntity(actionKeyDTO);
        actionKey = actionKeyRepository.save(actionKey);
        return actionKeyMapper.toDto(actionKey);
    }

    /**
     * Update a actionKey.
     *
     * @param actionKeyDTO the entity to save.
     * @return the persisted entity.
     */
    public ActionKeyDTO update(ActionKeyDTO actionKeyDTO) {
        log.debug("Request to save ActionKey : {}", actionKeyDTO);
        ActionKey actionKey = actionKeyMapper.toEntity(actionKeyDTO);
        actionKey = actionKeyRepository.save(actionKey);
        return actionKeyMapper.toDto(actionKey);
    }

    /**
     * Partially update a actionKey.
     *
     * @param actionKeyDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ActionKeyDTO> partialUpdate(ActionKeyDTO actionKeyDTO) {
        log.debug("Request to partially update ActionKey : {}", actionKeyDTO);

        return actionKeyRepository
            .findById(actionKeyDTO.getId())
            .map(existingActionKey -> {
                actionKeyMapper.partialUpdate(existingActionKey, actionKeyDTO);

                return existingActionKey;
            })
            .map(actionKeyRepository::save)
            .map(actionKeyMapper::toDto);
    }

    /**
     * Get all the actionKeys.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ActionKeyDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ActionKeys");
        return actionKeyRepository.findAll(pageable).map(actionKeyMapper::toDto);
    }

    /**
     * Get one actionKey by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ActionKeyDTO> findOne(Long id) {
        log.debug("Request to get ActionKey : {}", id);
        return actionKeyRepository.findById(id).map(actionKeyMapper::toDto);
    }

    /**
     * Delete the actionKey by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ActionKey : {}", id);
        actionKeyRepository.deleteById(id);
    }
}
