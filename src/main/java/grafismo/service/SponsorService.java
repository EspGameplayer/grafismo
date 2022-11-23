package grafismo.service;

import grafismo.domain.Sponsor;
import grafismo.repository.SponsorRepository;
import grafismo.service.dto.SponsorDTO;
import grafismo.service.mapper.SponsorMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Sponsor}.
 */
@Service
@Transactional
public class SponsorService {

    private final Logger log = LoggerFactory.getLogger(SponsorService.class);

    private final SponsorRepository sponsorRepository;

    private final SponsorMapper sponsorMapper;

    public SponsorService(SponsorRepository sponsorRepository, SponsorMapper sponsorMapper) {
        this.sponsorRepository = sponsorRepository;
        this.sponsorMapper = sponsorMapper;
    }

    /**
     * Save a sponsor.
     *
     * @param sponsorDTO the entity to save.
     * @return the persisted entity.
     */
    public SponsorDTO save(SponsorDTO sponsorDTO) {
        log.debug("Request to save Sponsor : {}", sponsorDTO);
        Sponsor sponsor = sponsorMapper.toEntity(sponsorDTO);
        sponsor = sponsorRepository.save(sponsor);
        return sponsorMapper.toDto(sponsor);
    }

    /**
     * Update a sponsor.
     *
     * @param sponsorDTO the entity to save.
     * @return the persisted entity.
     */
    public SponsorDTO update(SponsorDTO sponsorDTO) {
        log.debug("Request to save Sponsor : {}", sponsorDTO);
        Sponsor sponsor = sponsorMapper.toEntity(sponsorDTO);
        sponsor = sponsorRepository.save(sponsor);
        return sponsorMapper.toDto(sponsor);
    }

    /**
     * Partially update a sponsor.
     *
     * @param sponsorDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SponsorDTO> partialUpdate(SponsorDTO sponsorDTO) {
        log.debug("Request to partially update Sponsor : {}", sponsorDTO);

        return sponsorRepository
            .findById(sponsorDTO.getId())
            .map(existingSponsor -> {
                sponsorMapper.partialUpdate(existingSponsor, sponsorDTO);

                return existingSponsor;
            })
            .map(sponsorRepository::save)
            .map(sponsorMapper::toDto);
    }

    /**
     * Get all the sponsors.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SponsorDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Sponsors");
        return sponsorRepository.findAll(pageable).map(sponsorMapper::toDto);
    }

    /**
     * Get one sponsor by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SponsorDTO> findOne(Long id) {
        log.debug("Request to get Sponsor : {}", id);
        return sponsorRepository.findById(id).map(sponsorMapper::toDto);
    }

    /**
     * Delete the sponsor by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Sponsor : {}", id);
        sponsorRepository.deleteById(id);
    }
}
