package grafismo.web.rest;

import grafismo.repository.DeductionRepository;
import grafismo.service.DeductionService;
import grafismo.service.dto.DeductionDTO;
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
 * REST controller for managing {@link grafismo.domain.Deduction}.
 */
@RestController
@RequestMapping("/api")
public class DeductionResource {

    private final Logger log = LoggerFactory.getLogger(DeductionResource.class);

    private static final String ENTITY_NAME = "deduction";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DeductionService deductionService;

    private final DeductionRepository deductionRepository;

    public DeductionResource(DeductionService deductionService, DeductionRepository deductionRepository) {
        this.deductionService = deductionService;
        this.deductionRepository = deductionRepository;
    }

    /**
     * {@code POST  /deductions} : Create a new deduction.
     *
     * @param deductionDTO the deductionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new deductionDTO, or with status {@code 400 (Bad Request)} if the deduction has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/deductions")
    public ResponseEntity<DeductionDTO> createDeduction(@Valid @RequestBody DeductionDTO deductionDTO) throws URISyntaxException {
        log.debug("REST request to save Deduction : {}", deductionDTO);
        if (deductionDTO.getId() != null) {
            throw new BadRequestAlertException("A new deduction cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DeductionDTO result = deductionService.save(deductionDTO);
        return ResponseEntity
            .created(new URI("/api/deductions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /deductions/:id} : Updates an existing deduction.
     *
     * @param id the id of the deductionDTO to save.
     * @param deductionDTO the deductionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated deductionDTO,
     * or with status {@code 400 (Bad Request)} if the deductionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the deductionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/deductions/{id}")
    public ResponseEntity<DeductionDTO> updateDeduction(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DeductionDTO deductionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Deduction : {}, {}", id, deductionDTO);
        if (deductionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, deductionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!deductionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DeductionDTO result = deductionService.update(deductionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, deductionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /deductions/:id} : Partial updates given fields of an existing deduction, field will ignore if it is null
     *
     * @param id the id of the deductionDTO to save.
     * @param deductionDTO the deductionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated deductionDTO,
     * or with status {@code 400 (Bad Request)} if the deductionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the deductionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the deductionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/deductions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DeductionDTO> partialUpdateDeduction(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DeductionDTO deductionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Deduction partially : {}, {}", id, deductionDTO);
        if (deductionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, deductionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!deductionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DeductionDTO> result = deductionService.partialUpdate(deductionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, deductionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /deductions} : get all the deductions.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of deductions in body.
     */
    @GetMapping("/deductions")
    public ResponseEntity<List<DeductionDTO>> getAllDeductions(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of Deductions");
        Page<DeductionDTO> page;
        if (eagerload) {
            page = deductionService.findAllWithEagerRelationships(pageable);
        } else {
            page = deductionService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /deductions/:id} : get the "id" deduction.
     *
     * @param id the id of the deductionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the deductionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/deductions/{id}")
    public ResponseEntity<DeductionDTO> getDeduction(@PathVariable Long id) {
        log.debug("REST request to get Deduction : {}", id);
        Optional<DeductionDTO> deductionDTO = deductionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(deductionDTO);
    }

    /**
     * {@code DELETE  /deductions/:id} : delete the "id" deduction.
     *
     * @param id the id of the deductionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/deductions/{id}")
    public ResponseEntity<Void> deleteDeduction(@PathVariable Long id) {
        log.debug("REST request to delete Deduction : {}", id);
        deductionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
