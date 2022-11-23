package grafismo.service;

import grafismo.domain.BroadcastStaffMember;
import grafismo.repository.BroadcastStaffMemberRepository;
import grafismo.service.dto.BroadcastStaffMemberDTO;
import grafismo.service.mapper.BroadcastStaffMemberMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link BroadcastStaffMember}.
 */
@Service
@Transactional
public class BroadcastStaffMemberService {

    private final Logger log = LoggerFactory.getLogger(BroadcastStaffMemberService.class);

    private final BroadcastStaffMemberRepository broadcastStaffMemberRepository;

    private final BroadcastStaffMemberMapper broadcastStaffMemberMapper;

    public BroadcastStaffMemberService(
        BroadcastStaffMemberRepository broadcastStaffMemberRepository,
        BroadcastStaffMemberMapper broadcastStaffMemberMapper
    ) {
        this.broadcastStaffMemberRepository = broadcastStaffMemberRepository;
        this.broadcastStaffMemberMapper = broadcastStaffMemberMapper;
    }

    /**
     * Save a broadcastStaffMember.
     *
     * @param broadcastStaffMemberDTO the entity to save.
     * @return the persisted entity.
     */
    public BroadcastStaffMemberDTO save(BroadcastStaffMemberDTO broadcastStaffMemberDTO) {
        log.debug("Request to save BroadcastStaffMember : {}", broadcastStaffMemberDTO);
        BroadcastStaffMember broadcastStaffMember = broadcastStaffMemberMapper.toEntity(broadcastStaffMemberDTO);
        broadcastStaffMember = broadcastStaffMemberRepository.save(broadcastStaffMember);
        return broadcastStaffMemberMapper.toDto(broadcastStaffMember);
    }

    /**
     * Update a broadcastStaffMember.
     *
     * @param broadcastStaffMemberDTO the entity to save.
     * @return the persisted entity.
     */
    public BroadcastStaffMemberDTO update(BroadcastStaffMemberDTO broadcastStaffMemberDTO) {
        log.debug("Request to save BroadcastStaffMember : {}", broadcastStaffMemberDTO);
        BroadcastStaffMember broadcastStaffMember = broadcastStaffMemberMapper.toEntity(broadcastStaffMemberDTO);
        broadcastStaffMember = broadcastStaffMemberRepository.save(broadcastStaffMember);
        return broadcastStaffMemberMapper.toDto(broadcastStaffMember);
    }

    /**
     * Partially update a broadcastStaffMember.
     *
     * @param broadcastStaffMemberDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<BroadcastStaffMemberDTO> partialUpdate(BroadcastStaffMemberDTO broadcastStaffMemberDTO) {
        log.debug("Request to partially update BroadcastStaffMember : {}", broadcastStaffMemberDTO);

        return broadcastStaffMemberRepository
            .findById(broadcastStaffMemberDTO.getId())
            .map(existingBroadcastStaffMember -> {
                broadcastStaffMemberMapper.partialUpdate(existingBroadcastStaffMember, broadcastStaffMemberDTO);

                return existingBroadcastStaffMember;
            })
            .map(broadcastStaffMemberRepository::save)
            .map(broadcastStaffMemberMapper::toDto);
    }

    /**
     * Get all the broadcastStaffMembers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<BroadcastStaffMemberDTO> findAll(Pageable pageable) {
        log.debug("Request to get all BroadcastStaffMembers");
        return broadcastStaffMemberRepository.findAll(pageable).map(broadcastStaffMemberMapper::toDto);
    }

    /**
     * Get all the broadcastStaffMembers with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<BroadcastStaffMemberDTO> findAllWithEagerRelationships(Pageable pageable) {
        return broadcastStaffMemberRepository.findAllWithEagerRelationships(pageable).map(broadcastStaffMemberMapper::toDto);
    }

    /**
     * Get one broadcastStaffMember by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<BroadcastStaffMemberDTO> findOne(Long id) {
        log.debug("Request to get BroadcastStaffMember : {}", id);
        return broadcastStaffMemberRepository.findOneWithEagerRelationships(id).map(broadcastStaffMemberMapper::toDto);
    }

    /**
     * Delete the broadcastStaffMember by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete BroadcastStaffMember : {}", id);
        broadcastStaffMemberRepository.deleteById(id);
    }
}
