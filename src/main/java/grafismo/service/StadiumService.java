package grafismo.service;

import grafismo.domain.Stadium;
import grafismo.repository.StadiumRepository;
import grafismo.service.dto.StadiumDTO;
import grafismo.service.mapper.StadiumMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Stadium}.
 */
@Service
@Transactional
public class StadiumService {

    private final Logger log = LoggerFactory.getLogger(StadiumService.class);

    private final StadiumRepository stadiumRepository;

    private final StadiumMapper stadiumMapper;

    public StadiumService(StadiumRepository stadiumRepository, StadiumMapper stadiumMapper) {
        this.stadiumRepository = stadiumRepository;
        this.stadiumMapper = stadiumMapper;
    }

    /**
     * Save a stadium.
     *
     * @param stadiumDTO the entity to save.
     * @return the persisted entity.
     */
    public StadiumDTO save(StadiumDTO stadiumDTO) {
        log.debug("Request to save Stadium : {}", stadiumDTO);
        Stadium stadium = stadiumMapper.toEntity(stadiumDTO);
        stadium = stadiumRepository.save(stadium);
        return stadiumMapper.toDto(stadium);
    }

    /**
     * Update a stadium.
     *
     * @param stadiumDTO the entity to save.
     * @return the persisted entity.
     */
    public StadiumDTO update(StadiumDTO stadiumDTO) {
        log.debug("Request to save Stadium : {}", stadiumDTO);
        Stadium stadium = stadiumMapper.toEntity(stadiumDTO);
        stadium = stadiumRepository.save(stadium);
        return stadiumMapper.toDto(stadium);
    }

    /**
     * Partially update a stadium.
     *
     * @param stadiumDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<StadiumDTO> partialUpdate(StadiumDTO stadiumDTO) {
        log.debug("Request to partially update Stadium : {}", stadiumDTO);

        return stadiumRepository
            .findById(stadiumDTO.getId())
            .map(existingStadium -> {
                stadiumMapper.partialUpdate(existingStadium, stadiumDTO);

                return existingStadium;
            })
            .map(stadiumRepository::save)
            .map(stadiumMapper::toDto);
    }

    /**
     * Get all the stadiums.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<StadiumDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Stadiums");
        return stadiumRepository.findAll(pageable).map(stadiumMapper::toDto);
    }

    /**
     * Get one stadium by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<StadiumDTO> findOne(Long id) {
        log.debug("Request to get Stadium : {}", id);
        return stadiumRepository.findById(id).map(stadiumMapper::toDto);
    }

    /**
     * Delete the stadium by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Stadium : {}", id);
        stadiumRepository.deleteById(id);
    }
}
