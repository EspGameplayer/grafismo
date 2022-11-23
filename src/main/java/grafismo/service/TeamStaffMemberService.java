package grafismo.service;

import grafismo.domain.TeamStaffMember;
import grafismo.repository.TeamStaffMemberRepository;
import grafismo.service.dto.TeamStaffMemberDTO;
import grafismo.service.mapper.TeamStaffMemberMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TeamStaffMember}.
 */
@Service
@Transactional
public class TeamStaffMemberService {

    private final Logger log = LoggerFactory.getLogger(TeamStaffMemberService.class);

    private final TeamStaffMemberRepository teamStaffMemberRepository;

    private final TeamStaffMemberMapper teamStaffMemberMapper;

    public TeamStaffMemberService(TeamStaffMemberRepository teamStaffMemberRepository, TeamStaffMemberMapper teamStaffMemberMapper) {
        this.teamStaffMemberRepository = teamStaffMemberRepository;
        this.teamStaffMemberMapper = teamStaffMemberMapper;
    }

    /**
     * Save a teamStaffMember.
     *
     * @param teamStaffMemberDTO the entity to save.
     * @return the persisted entity.
     */
    public TeamStaffMemberDTO save(TeamStaffMemberDTO teamStaffMemberDTO) {
        log.debug("Request to save TeamStaffMember : {}", teamStaffMemberDTO);
        TeamStaffMember teamStaffMember = teamStaffMemberMapper.toEntity(teamStaffMemberDTO);
        teamStaffMember = teamStaffMemberRepository.save(teamStaffMember);
        return teamStaffMemberMapper.toDto(teamStaffMember);
    }

    /**
     * Update a teamStaffMember.
     *
     * @param teamStaffMemberDTO the entity to save.
     * @return the persisted entity.
     */
    public TeamStaffMemberDTO update(TeamStaffMemberDTO teamStaffMemberDTO) {
        log.debug("Request to save TeamStaffMember : {}", teamStaffMemberDTO);
        TeamStaffMember teamStaffMember = teamStaffMemberMapper.toEntity(teamStaffMemberDTO);
        teamStaffMember = teamStaffMemberRepository.save(teamStaffMember);
        return teamStaffMemberMapper.toDto(teamStaffMember);
    }

    /**
     * Partially update a teamStaffMember.
     *
     * @param teamStaffMemberDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TeamStaffMemberDTO> partialUpdate(TeamStaffMemberDTO teamStaffMemberDTO) {
        log.debug("Request to partially update TeamStaffMember : {}", teamStaffMemberDTO);

        return teamStaffMemberRepository
            .findById(teamStaffMemberDTO.getId())
            .map(existingTeamStaffMember -> {
                teamStaffMemberMapper.partialUpdate(existingTeamStaffMember, teamStaffMemberDTO);

                return existingTeamStaffMember;
            })
            .map(teamStaffMemberRepository::save)
            .map(teamStaffMemberMapper::toDto);
    }

    /**
     * Get all the teamStaffMembers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TeamStaffMemberDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TeamStaffMembers");
        return teamStaffMemberRepository.findAll(pageable).map(teamStaffMemberMapper::toDto);
    }

    /**
     * Get all the teamStaffMembers with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<TeamStaffMemberDTO> findAllWithEagerRelationships(Pageable pageable) {
        return teamStaffMemberRepository.findAllWithEagerRelationships(pageable).map(teamStaffMemberMapper::toDto);
    }

    /**
     * Get one teamStaffMember by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TeamStaffMemberDTO> findOne(Long id) {
        log.debug("Request to get TeamStaffMember : {}", id);
        return teamStaffMemberRepository.findOneWithEagerRelationships(id).map(teamStaffMemberMapper::toDto);
    }

    /**
     * Delete the teamStaffMember by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TeamStaffMember : {}", id);
        teamStaffMemberRepository.deleteById(id);
    }
}
