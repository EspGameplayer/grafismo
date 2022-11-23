package grafismo.web.rest;

import grafismo.repository.BroadcastStaffMemberRepository;
import grafismo.service.BroadcastStaffMemberService;
import grafismo.service.dto.BroadcastStaffMemberDTO;
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
 * REST controller for managing {@link grafismo.domain.BroadcastStaffMember}.
 */
@RestController
@RequestMapping("/api")
public class BroadcastStaffMemberResource {

    private final Logger log = LoggerFactory.getLogger(BroadcastStaffMemberResource.class);

    private static final String ENTITY_NAME = "broadcastStaffMember";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BroadcastStaffMemberService broadcastStaffMemberService;

    private final BroadcastStaffMemberRepository broadcastStaffMemberRepository;

    public BroadcastStaffMemberResource(
        BroadcastStaffMemberService broadcastStaffMemberService,
        BroadcastStaffMemberRepository broadcastStaffMemberRepository
    ) {
        this.broadcastStaffMemberService = broadcastStaffMemberService;
        this.broadcastStaffMemberRepository = broadcastStaffMemberRepository;
    }

    /**
     * {@code POST  /broadcast-staff-members} : Create a new broadcastStaffMember.
     *
     * @param broadcastStaffMemberDTO the broadcastStaffMemberDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new broadcastStaffMemberDTO, or with status {@code 400 (Bad Request)} if the broadcastStaffMember has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/broadcast-staff-members")
    public ResponseEntity<BroadcastStaffMemberDTO> createBroadcastStaffMember(
        @Valid @RequestBody BroadcastStaffMemberDTO broadcastStaffMemberDTO
    ) throws URISyntaxException {
        log.debug("REST request to save BroadcastStaffMember : {}", broadcastStaffMemberDTO);
        if (broadcastStaffMemberDTO.getId() != null) {
            throw new BadRequestAlertException("A new broadcastStaffMember cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BroadcastStaffMemberDTO result = broadcastStaffMemberService.save(broadcastStaffMemberDTO);
        return ResponseEntity
            .created(new URI("/api/broadcast-staff-members/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /broadcast-staff-members/:id} : Updates an existing broadcastStaffMember.
     *
     * @param id the id of the broadcastStaffMemberDTO to save.
     * @param broadcastStaffMemberDTO the broadcastStaffMemberDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated broadcastStaffMemberDTO,
     * or with status {@code 400 (Bad Request)} if the broadcastStaffMemberDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the broadcastStaffMemberDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/broadcast-staff-members/{id}")
    public ResponseEntity<BroadcastStaffMemberDTO> updateBroadcastStaffMember(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody BroadcastStaffMemberDTO broadcastStaffMemberDTO
    ) throws URISyntaxException {
        log.debug("REST request to update BroadcastStaffMember : {}, {}", id, broadcastStaffMemberDTO);
        if (broadcastStaffMemberDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, broadcastStaffMemberDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!broadcastStaffMemberRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BroadcastStaffMemberDTO result = broadcastStaffMemberService.update(broadcastStaffMemberDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, broadcastStaffMemberDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /broadcast-staff-members/:id} : Partial updates given fields of an existing broadcastStaffMember, field will ignore if it is null
     *
     * @param id the id of the broadcastStaffMemberDTO to save.
     * @param broadcastStaffMemberDTO the broadcastStaffMemberDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated broadcastStaffMemberDTO,
     * or with status {@code 400 (Bad Request)} if the broadcastStaffMemberDTO is not valid,
     * or with status {@code 404 (Not Found)} if the broadcastStaffMemberDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the broadcastStaffMemberDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/broadcast-staff-members/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BroadcastStaffMemberDTO> partialUpdateBroadcastStaffMember(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody BroadcastStaffMemberDTO broadcastStaffMemberDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update BroadcastStaffMember partially : {}, {}", id, broadcastStaffMemberDTO);
        if (broadcastStaffMemberDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, broadcastStaffMemberDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!broadcastStaffMemberRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BroadcastStaffMemberDTO> result = broadcastStaffMemberService.partialUpdate(broadcastStaffMemberDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, broadcastStaffMemberDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /broadcast-staff-members} : get all the broadcastStaffMembers.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of broadcastStaffMembers in body.
     */
    @GetMapping("/broadcast-staff-members")
    public ResponseEntity<List<BroadcastStaffMemberDTO>> getAllBroadcastStaffMembers(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of BroadcastStaffMembers");
        Page<BroadcastStaffMemberDTO> page;
        if (eagerload) {
            page = broadcastStaffMemberService.findAllWithEagerRelationships(pageable);
        } else {
            page = broadcastStaffMemberService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /broadcast-staff-members/:id} : get the "id" broadcastStaffMember.
     *
     * @param id the id of the broadcastStaffMemberDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the broadcastStaffMemberDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/broadcast-staff-members/{id}")
    public ResponseEntity<BroadcastStaffMemberDTO> getBroadcastStaffMember(@PathVariable Long id) {
        log.debug("REST request to get BroadcastStaffMember : {}", id);
        Optional<BroadcastStaffMemberDTO> broadcastStaffMemberDTO = broadcastStaffMemberService.findOne(id);
        return ResponseUtil.wrapOrNotFound(broadcastStaffMemberDTO);
    }

    /**
     * {@code DELETE  /broadcast-staff-members/:id} : delete the "id" broadcastStaffMember.
     *
     * @param id the id of the broadcastStaffMemberDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/broadcast-staff-members/{id}")
    public ResponseEntity<Void> deleteBroadcastStaffMember(@PathVariable Long id) {
        log.debug("REST request to delete BroadcastStaffMember : {}", id);
        broadcastStaffMemberService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
