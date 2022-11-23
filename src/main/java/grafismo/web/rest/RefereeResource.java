package grafismo.web.rest;

import grafismo.repository.RefereeRepository;
import grafismo.service.RefereeService;
import grafismo.service.dto.RefereeDTO;
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
 * REST controller for managing {@link grafismo.domain.Referee}.
 */
@RestController
@RequestMapping("/api")
public class RefereeResource {

    private final Logger log = LoggerFactory.getLogger(RefereeResource.class);

    private static final String ENTITY_NAME = "referee";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RefereeService refereeService;

    private final RefereeRepository refereeRepository;

    public RefereeResource(RefereeService refereeService, RefereeRepository refereeRepository) {
        this.refereeService = refereeService;
        this.refereeRepository = refereeRepository;
    }

    /**
     * {@code POST  /referees} : Create a new referee.
     *
     * @param refereeDTO the refereeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new refereeDTO, or with status {@code 400 (Bad Request)} if the referee has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/referees")
    public ResponseEntity<RefereeDTO> createReferee(@Valid @RequestBody RefereeDTO refereeDTO) throws URISyntaxException {
        log.debug("REST request to save Referee : {}", refereeDTO);
        if (refereeDTO.getId() != null) {
            throw new BadRequestAlertException("A new referee cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RefereeDTO result = refereeService.save(refereeDTO);
        return ResponseEntity
            .created(new URI("/api/referees/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /referees/:id} : Updates an existing referee.
     *
     * @param id the id of the refereeDTO to save.
     * @param refereeDTO the refereeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated refereeDTO,
     * or with status {@code 400 (Bad Request)} if the refereeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the refereeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/referees/{id}")
    public ResponseEntity<RefereeDTO> updateReferee(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RefereeDTO refereeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Referee : {}, {}", id, refereeDTO);
        if (refereeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, refereeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!refereeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RefereeDTO result = refereeService.update(refereeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, refereeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /referees/:id} : Partial updates given fields of an existing referee, field will ignore if it is null
     *
     * @param id the id of the refereeDTO to save.
     * @param refereeDTO the refereeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated refereeDTO,
     * or with status {@code 400 (Bad Request)} if the refereeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the refereeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the refereeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/referees/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RefereeDTO> partialUpdateReferee(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RefereeDTO refereeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Referee partially : {}, {}", id, refereeDTO);
        if (refereeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, refereeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!refereeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RefereeDTO> result = refereeService.partialUpdate(refereeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, refereeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /referees} : get all the referees.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of referees in body.
     */
    @GetMapping("/referees")
    public ResponseEntity<List<RefereeDTO>> getAllReferees(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of Referees");
        Page<RefereeDTO> page;
        if (eagerload) {
            page = refereeService.findAllWithEagerRelationships(pageable);
        } else {
            page = refereeService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /referees/:id} : get the "id" referee.
     *
     * @param id the id of the refereeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the refereeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/referees/{id}")
    public ResponseEntity<RefereeDTO> getReferee(@PathVariable Long id) {
        log.debug("REST request to get Referee : {}", id);
        Optional<RefereeDTO> refereeDTO = refereeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(refereeDTO);
    }

    /**
     * {@code DELETE  /referees/:id} : delete the "id" referee.
     *
     * @param id the id of the refereeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/referees/{id}")
    public ResponseEntity<Void> deleteReferee(@PathVariable Long id) {
        log.debug("REST request to delete Referee : {}", id);
        refereeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
