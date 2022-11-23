package grafismo.service;

import grafismo.domain.LocalAssociationProvince;
import grafismo.repository.LocalAssociationProvinceRepository;
import grafismo.service.dto.LocalAssociationProvinceDTO;
import grafismo.service.mapper.LocalAssociationProvinceMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link LocalAssociationProvince}.
 */
@Service
@Transactional
public class LocalAssociationProvinceService {

    private final Logger log = LoggerFactory.getLogger(LocalAssociationProvinceService.class);

    private final LocalAssociationProvinceRepository localAssociationProvinceRepository;

    private final LocalAssociationProvinceMapper localAssociationProvinceMapper;

    public LocalAssociationProvinceService(
        LocalAssociationProvinceRepository localAssociationProvinceRepository,
        LocalAssociationProvinceMapper localAssociationProvinceMapper
    ) {
        this.localAssociationProvinceRepository = localAssociationProvinceRepository;
        this.localAssociationProvinceMapper = localAssociationProvinceMapper;
    }

    /**
     * Save a localAssociationProvince.
     *
     * @param localAssociationProvinceDTO the entity to save.
     * @return the persisted entity.
     */
    public LocalAssociationProvinceDTO save(LocalAssociationProvinceDTO localAssociationProvinceDTO) {
        log.debug("Request to save LocalAssociationProvince : {}", localAssociationProvinceDTO);
        LocalAssociationProvince localAssociationProvince = localAssociationProvinceMapper.toEntity(localAssociationProvinceDTO);
        localAssociationProvince = localAssociationProvinceRepository.save(localAssociationProvince);
        return localAssociationProvinceMapper.toDto(localAssociationProvince);
    }

    /**
     * Update a localAssociationProvince.
     *
     * @param localAssociationProvinceDTO the entity to save.
     * @return the persisted entity.
     */
    public LocalAssociationProvinceDTO update(LocalAssociationProvinceDTO localAssociationProvinceDTO) {
        log.debug("Request to save LocalAssociationProvince : {}", localAssociationProvinceDTO);
        LocalAssociationProvince localAssociationProvince = localAssociationProvinceMapper.toEntity(localAssociationProvinceDTO);
        localAssociationProvince = localAssociationProvinceRepository.save(localAssociationProvince);
        return localAssociationProvinceMapper.toDto(localAssociationProvince);
    }

    /**
     * Partially update a localAssociationProvince.
     *
     * @param localAssociationProvinceDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<LocalAssociationProvinceDTO> partialUpdate(LocalAssociationProvinceDTO localAssociationProvinceDTO) {
        log.debug("Request to partially update LocalAssociationProvince : {}", localAssociationProvinceDTO);

        return localAssociationProvinceRepository
            .findById(localAssociationProvinceDTO.getId())
            .map(existingLocalAssociationProvince -> {
                localAssociationProvinceMapper.partialUpdate(existingLocalAssociationProvince, localAssociationProvinceDTO);

                return existingLocalAssociationProvince;
            })
            .map(localAssociationProvinceRepository::save)
            .map(localAssociationProvinceMapper::toDto);
    }

    /**
     * Get all the localAssociationProvinces.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<LocalAssociationProvinceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all LocalAssociationProvinces");
        return localAssociationProvinceRepository.findAll(pageable).map(localAssociationProvinceMapper::toDto);
    }

    /**
     * Get all the localAssociationProvinces with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<LocalAssociationProvinceDTO> findAllWithEagerRelationships(Pageable pageable) {
        return localAssociationProvinceRepository.findAllWithEagerRelationships(pageable).map(localAssociationProvinceMapper::toDto);
    }

    /**
     * Get one localAssociationProvince by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<LocalAssociationProvinceDTO> findOne(Long id) {
        log.debug("Request to get LocalAssociationProvince : {}", id);
        return localAssociationProvinceRepository.findOneWithEagerRelationships(id).map(localAssociationProvinceMapper::toDto);
    }

    /**
     * Delete the localAssociationProvince by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete LocalAssociationProvince : {}", id);
        localAssociationProvinceRepository.deleteById(id);
    }
}
