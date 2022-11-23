package grafismo.web.rest;

import grafismo.repository.CallupRepository;
import grafismo.service.CallupService;
import grafismo.service.dto.CallupDTO;
import grafismo.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
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
 * REST controller for managing {@link grafismo.domain.Callup}.
 */
@RestController
@RequestMapping("/api")
public class CallupResource {

    private final Logger log = LoggerFactory.getLogger(CallupResource.class);

    private static final String ENTITY_NAME = "callup";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CallupService callupService;

    private final CallupRepository callupRepository;

    public CallupResource(CallupService callupService, CallupRepository callupRepository) {
        this.callupService = callupService;
        this.callupRepository = callupRepository;
    }

    /**
     * {@code POST  /callups} : Create a new callup.
     *
     * @param callupDTO the callupDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new callupDTO, or with status {@code 400 (Bad Request)} if the callup has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/callups")
    public ResponseEntity<CallupDTO> createCallup(@Valid @RequestBody CallupDTO callupDTO) throws URISyntaxException {
        log.debug("REST request to save Callup : {}", callupDTO);
        if (callupDTO.getId() != null) {
            throw new BadRequestAlertException("A new callup cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CallupDTO result = callupService.save(callupDTO);
        return ResponseEntity
            .created(new URI("/api/callups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /callups/:id} : Updates an existing callup.
     *
     * @param id the id of the callupDTO to save.
     * @param callupDTO the callupDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated callupDTO,
     * or with status {@code 400 (Bad Request)} if the callupDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the callupDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/callups/{id}")
    public ResponseEntity<CallupDTO> updateCallup(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CallupDTO callupDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Callup : {}, {}", id, callupDTO);
        if (callupDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, callupDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!callupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CallupDTO result = callupService.update(callupDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, callupDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /callups/:id} : Partial updates given fields of an existing callup, field will ignore if it is null
     *
     * @param id the id of the callupDTO to save.
     * @param callupDTO the callupDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated callupDTO,
     * or with status {@code 400 (Bad Request)} if the callupDTO is not valid,
     * or with status {@code 404 (Not Found)} if the callupDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the callupDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/callups/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CallupDTO> partialUpdateCallup(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CallupDTO callupDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Callup partially : {}, {}", id, callupDTO);
        if (callupDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, callupDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!callupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CallupDTO> result = callupService.partialUpdate(callupDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, callupDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /callups} : get all the callups.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of callups in body.
     */
    @GetMapping("/callups")
    public ResponseEntity<List<CallupDTO>> getAllCallups(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false) String filter,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        if ("homematch-is-null".equals(filter)) {
            log.debug("REST request to get all Callups where homeMatch is null");
            return new ResponseEntity<>(callupService.findAllWhereHomeMatchIsNull(), HttpStatus.OK);
        }

        if ("awaymatch-is-null".equals(filter)) {
            log.debug("REST request to get all Callups where awayMatch is null");
            return new ResponseEntity<>(callupService.findAllWhereAwayMatchIsNull(), HttpStatus.OK);
        }
        log.debug("REST request to get a page of Callups");
        Page<CallupDTO> page;
        if (eagerload) {
            page = callupService.findAllWithEagerRelationships(pageable);
        } else {
            page = callupService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /callups/:id} : get the "id" callup.
     *
     * @param id the id of the callupDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the callupDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/callups/{id}")
    public ResponseEntity<CallupDTO> getCallup(@PathVariable Long id) {
        log.debug("REST request to get Callup : {}", id);
        Optional<CallupDTO> callupDTO = callupService.findOne(id);
        return ResponseUtil.wrapOrNotFound(callupDTO);
    }

    /**
     * {@code DELETE  /callups/:id} : delete the "id" callup.
     *
     * @param id the id of the callupDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/callups/{id}")
    public ResponseEntity<Void> deleteCallup(@PathVariable Long id) {
        log.debug("REST request to delete Callup : {}", id);
        callupService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
