package grafismo.web.rest;

import grafismo.repository.TeamStaffMemberRepository;
import grafismo.service.TeamStaffMemberService;
import grafismo.service.dto.TeamStaffMemberDTO;
import grafismo.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link grafismo.domain.TeamStaffMember}.
 */
@RestController
@RequestMapping("/api")
public class TeamStaffMemberResource {

    private final Logger log = LoggerFactory.getLogger(TeamStaffMemberResource.class);

    private static final String ENTITY_NAME = "teamStaffMember";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TeamStaffMemberService teamStaffMemberService;

    private final TeamStaffMemberRepository teamStaffMemberRepository;

    public TeamStaffMemberResource(TeamStaffMemberService teamStaffMemberService, TeamStaffMemberRepository teamStaffMemberRepository) {
        this.teamStaffMemberService = teamStaffMemberService;
        this.teamStaffMemberRepository = teamStaffMemberRepository;
    }

    /**
     * {@code POST  /team-staff-members} : Create a new teamStaffMember.
     *
     * @param teamStaffMemberDTO the teamStaffMemberDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new teamStaffMemberDTO, or with status {@code 400 (Bad Request)} if the teamStaffMember has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/team-staff-members")
    public ResponseEntity<TeamStaffMemberDTO> createTeamStaffMember(@Valid @RequestBody TeamStaffMemberDTO teamStaffMemberDTO)
        throws URISyntaxException {
        log.debug("REST request to save TeamStaffMember : {}", teamStaffMemberDTO);
        if (teamStaffMemberDTO.getId() != null) {
            throw new BadRequestAlertException("A new teamStaffMember cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TeamStaffMemberDTO result = teamStaffMemberService.save(teamStaffMemberDTO);
        return ResponseEntity
            .created(new URI("/api/team-staff-members/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /team-staff-members/:id} : Updates an existing teamStaffMember.
     *
     * @param id the id of the teamStaffMemberDTO to save.
     * @param teamStaffMemberDTO the teamStaffMemberDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated teamStaffMemberDTO,
     * or with status {@code 400 (Bad Request)} if the teamStaffMemberDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the teamStaffMemberDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/team-staff-members/{id}")
    public ResponseEntity<TeamStaffMemberDTO> updateTeamStaffMember(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TeamStaffMemberDTO teamStaffMemberDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TeamStaffMember : {}, {}", id, teamStaffMemberDTO);
        if (teamStaffMemberDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, teamStaffMemberDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!teamStaffMemberRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TeamStaffMemberDTO result = teamStaffMemberService.update(teamStaffMemberDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, teamStaffMemberDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /team-staff-members/:id} : Partial updates given fields of an existing teamStaffMember, field will ignore if it is null
     *
     * @param id the id of the teamStaffMemberDTO to save.
     * @param teamStaffMemberDTO the teamStaffMemberDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated teamStaffMemberDTO,
     * or with status {@code 400 (Bad Request)} if the teamStaffMemberDTO is not valid,
     * or with status {@code 404 (Not Found)} if the teamStaffMemberDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the teamStaffMemberDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/team-staff-members/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TeamStaffMemberDTO> partialUpdateTeamStaffMember(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TeamStaffMemberDTO teamStaffMemberDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TeamStaffMember partially : {}, {}", id, teamStaffMemberDTO);
        if (teamStaffMemberDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, teamStaffMemberDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!teamStaffMemberRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TeamStaffMemberDTO> result = teamStaffMemberService.partialUpdate(teamStaffMemberDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, teamStaffMemberDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /team-staff-members} : get all the teamStaffMembers.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of teamStaffMembers in body.
     */
    @GetMapping("/team-staff-members")
    public ResponseEntity<List<TeamStaffMemberDTO>> getAllTeamStaffMembers(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of TeamStaffMembers");
        Page<TeamStaffMemberDTO> page;
        if (eagerload) {
            page = teamStaffMemberService.findAllWithEagerRelationships(pageable);
        } else {
            page = teamStaffMemberService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /team-staff-members/:id} : get the "id" teamStaffMember.
     *
     * @param id the id of the teamStaffMemberDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the teamStaffMemberDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/team-staff-members/{id}")
    public ResponseEntity<TeamStaffMemberDTO> getTeamStaffMember(@PathVariable Long id) {
        log.debug("REST request to get TeamStaffMember : {}", id);
        Optional<TeamStaffMemberDTO> teamStaffMemberDTO = teamStaffMemberService.findOne(id);
        return ResponseUtil.wrapOrNotFound(teamStaffMemberDTO);
    }

    /**
     * {@code DELETE  /team-staff-members/:id} : delete the "id" teamStaffMember.
     *
     * @param id the id of the teamStaffMemberDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/team-staff-members/{id}")
    public ResponseEntity<Void> deleteTeamStaffMember(@PathVariable Long id) {
        log.debug("REST request to delete TeamStaffMember : {}", id);
        teamStaffMemberService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
