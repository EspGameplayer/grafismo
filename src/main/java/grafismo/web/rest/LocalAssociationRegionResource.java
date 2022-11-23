package grafismo.web.rest;

import grafismo.repository.LocalAssociationRegionRepository;
import grafismo.service.LocalAssociationRegionService;
import grafismo.service.dto.LocalAssociationRegionDTO;
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
 * REST controller for managing {@link grafismo.domain.LocalAssociationRegion}.
 */
@RestController
@RequestMapping("/api")
public class LocalAssociationRegionResource {

    private final Logger log = LoggerFactory.getLogger(LocalAssociationRegionResource.class);

    private static final String ENTITY_NAME = "localAssociationRegion";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LocalAssociationRegionService localAssociationRegionService;

    private final LocalAssociationRegionRepository localAssociationRegionRepository;

    public LocalAssociationRegionResource(
        LocalAssociationRegionService localAssociationRegionService,
        LocalAssociationRegionRepository localAssociationRegionRepository
    ) {
        this.localAssociationRegionService = localAssociationRegionService;
        this.localAssociationRegionRepository = localAssociationRegionRepository;
    }

    /**
     * {@code POST  /local-association-regions} : Create a new localAssociationRegion.
     *
     * @param localAssociationRegionDTO the localAssociationRegionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new localAssociationRegionDTO, or with status {@code 400 (Bad Request)} if the localAssociationRegion has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/local-association-regions")
    public ResponseEntity<LocalAssociationRegionDTO> createLocalAssociationRegion(
        @Valid @RequestBody LocalAssociationRegionDTO localAssociationRegionDTO
    ) throws URISyntaxException {
        log.debug("REST request to save LocalAssociationRegion : {}", localAssociationRegionDTO);
        if (localAssociationRegionDTO.getId() != null) {
            throw new BadRequestAlertException("A new localAssociationRegion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LocalAssociationRegionDTO result = localAssociationRegionService.save(localAssociationRegionDTO);
        return ResponseEntity
            .created(new URI("/api/local-association-regions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /local-association-regions/:id} : Updates an existing localAssociationRegion.
     *
     * @param id the id of the localAssociationRegionDTO to save.
     * @param localAssociationRegionDTO the localAssociationRegionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated localAssociationRegionDTO,
     * or with status {@code 400 (Bad Request)} if the localAssociationRegionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the localAssociationRegionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/local-association-regions/{id}")
    public ResponseEntity<LocalAssociationRegionDTO> updateLocalAssociationRegion(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LocalAssociationRegionDTO localAssociationRegionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update LocalAssociationRegion : {}, {}", id, localAssociationRegionDTO);
        if (localAssociationRegionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, localAssociationRegionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!localAssociationRegionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LocalAssociationRegionDTO result = localAssociationRegionService.update(localAssociationRegionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, localAssociationRegionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /local-association-regions/:id} : Partial updates given fields of an existing localAssociationRegion, field will ignore if it is null
     *
     * @param id the id of the localAssociationRegionDTO to save.
     * @param localAssociationRegionDTO the localAssociationRegionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated localAssociationRegionDTO,
     * or with status {@code 400 (Bad Request)} if the localAssociationRegionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the localAssociationRegionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the localAssociationRegionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/local-association-regions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LocalAssociationRegionDTO> partialUpdateLocalAssociationRegion(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LocalAssociationRegionDTO localAssociationRegionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update LocalAssociationRegion partially : {}, {}", id, localAssociationRegionDTO);
        if (localAssociationRegionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, localAssociationRegionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!localAssociationRegionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LocalAssociationRegionDTO> result = localAssociationRegionService.partialUpdate(localAssociationRegionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, localAssociationRegionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /local-association-regions} : get all the localAssociationRegions.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of localAssociationRegions in body.
     */
    @GetMapping("/local-association-regions")
    public ResponseEntity<List<LocalAssociationRegionDTO>> getAllLocalAssociationRegions(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of LocalAssociationRegions");
        Page<LocalAssociationRegionDTO> page;
        if (eagerload) {
            page = localAssociationRegionService.findAllWithEagerRelationships(pageable);
        } else {
            page = localAssociationRegionService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /local-association-regions/:id} : get the "id" localAssociationRegion.
     *
     * @param id the id of the localAssociationRegionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the localAssociationRegionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/local-association-regions/{id}")
    public ResponseEntity<LocalAssociationRegionDTO> getLocalAssociationRegion(@PathVariable Long id) {
        log.debug("REST request to get LocalAssociationRegion : {}", id);
        Optional<LocalAssociationRegionDTO> localAssociationRegionDTO = localAssociationRegionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(localAssociationRegionDTO);
    }

    /**
     * {@code DELETE  /local-association-regions/:id} : delete the "id" localAssociationRegion.
     *
     * @param id the id of the localAssociationRegionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/local-association-regions/{id}")
    public ResponseEntity<Void> deleteLocalAssociationRegion(@PathVariable Long id) {
        log.debug("REST request to delete LocalAssociationRegion : {}", id);
        localAssociationRegionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
