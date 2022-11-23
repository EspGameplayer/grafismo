package grafismo.service;

import grafismo.domain.Shirt;
import grafismo.repository.ShirtRepository;
import grafismo.service.dto.ShirtDTO;
import grafismo.service.mapper.ShirtMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Shirt}.
 */
@Service
@Transactional
public class ShirtService {

    private final Logger log = LoggerFactory.getLogger(ShirtService.class);

    private final ShirtRepository shirtRepository;

    private final ShirtMapper shirtMapper;

    public ShirtService(ShirtRepository shirtRepository, ShirtMapper shirtMapper) {
        this.shirtRepository = shirtRepository;
        this.shirtMapper = shirtMapper;
    }

    /**
     * Save a shirt.
     *
     * @param shirtDTO the entity to save.
     * @return the persisted entity.
     */
    public ShirtDTO save(ShirtDTO shirtDTO) {
        log.debug("Request to save Shirt : {}", shirtDTO);
        Shirt shirt = shirtMapper.toEntity(shirtDTO);
        shirt = shirtRepository.save(shirt);
        return shirtMapper.toDto(shirt);
    }

    /**
     * Update a shirt.
     *
     * @param shirtDTO the entity to save.
     * @return the persisted entity.
     */
    public ShirtDTO update(ShirtDTO shirtDTO) {
        log.debug("Request to save Shirt : {}", shirtDTO);
        Shirt shirt = shirtMapper.toEntity(shirtDTO);
        shirt = shirtRepository.save(shirt);
        return shirtMapper.toDto(shirt);
    }

    /**
     * Partially update a shirt.
     *
     * @param shirtDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ShirtDTO> partialUpdate(ShirtDTO shirtDTO) {
        log.debug("Request to partially update Shirt : {}", shirtDTO);

        return shirtRepository
            .findById(shirtDTO.getId())
            .map(existingShirt -> {
                shirtMapper.partialUpdate(existingShirt, shirtDTO);

                return existingShirt;
            })
            .map(shirtRepository::save)
            .map(shirtMapper::toDto);
    }

    /**
     * Get all the shirts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ShirtDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Shirts");
        return shirtRepository.findAll(pageable).map(shirtMapper::toDto);
    }

    /**
     * Get all the shirts with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ShirtDTO> findAllWithEagerRelationships(Pageable pageable) {
        return shirtRepository.findAllWithEagerRelationships(pageable).map(shirtMapper::toDto);
    }

    /**
     * Get one shirt by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ShirtDTO> findOne(Long id) {
        log.debug("Request to get Shirt : {}", id);
        return shirtRepository.findOneWithEagerRelationships(id).map(shirtMapper::toDto);
    }

    /**
     * Delete the shirt by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Shirt : {}", id);
        shirtRepository.deleteById(id);
    }
}
