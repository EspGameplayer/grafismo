package grafismo.web.rest;

import grafismo.repository.LocalAssociationProvinceRepository;
import grafismo.service.LocalAssociationProvinceService;
import grafismo.service.dto.LocalAssociationProvinceDTO;
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
 * REST controller for managing {@link grafismo.domain.LocalAssociationProvince}.
 */
@RestController
@RequestMapping("/api")
public class LocalAssociationProvinceResource {

    private final Logger log = LoggerFactory.getLogger(LocalAssociationProvinceResource.class);

    private static final String ENTITY_NAME = "localAssociationProvince";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LocalAssociationProvinceService localAssociationProvinceService;

    private final LocalAssociationProvinceRepository localAssociationProvinceRepository;

    public LocalAssociationProvinceResource(
        LocalAssociationProvinceService localAssociationProvinceService,
        LocalAssociationProvinceRepository localAssociationProvinceRepository
    ) {
        this.localAssociationProvinceService = localAssociationProvinceService;
        this.localAssociationProvinceRepository = localAssociationProvinceRepository;
    }

    /**
     * {@code POST  /local-association-provinces} : Create a new localAssociationProvince.
     *
     * @param localAssociationProvinceDTO the localAssociationProvinceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new localAssociationProvinceDTO, or with status {@code 400 (Bad Request)} if the localAssociationProvince has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/local-association-provinces")
    public ResponseEntity<LocalAssociationProvinceDTO> createLocalAssociationProvince(
        @Valid @RequestBody LocalAssociationProvinceDTO localAssociationProvinceDTO
    ) throws URISyntaxException {
        log.debug("REST request to save LocalAssociationProvince : {}", localAssociationProvinceDTO);
        if (localAssociationProvinceDTO.getId() != null) {
            throw new BadRequestAlertException("A new localAssociationProvince cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LocalAssociationProvinceDTO result = localAssociationProvinceService.save(localAssociationProvinceDTO);
        return ResponseEntity
            .created(new URI("/api/local-association-provinces/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /local-association-provinces/:id} : Updates an existing localAssociationProvince.
     *
     * @param id the id of the localAssociationProvinceDTO to save.
     * @param localAssociationProvinceDTO the localAssociationProvinceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated localAssociationProvinceDTO,
     * or with status {@code 400 (Bad Request)} if the localAssociationProvinceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the localAssociationProvinceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/local-association-provinces/{id}")
    public ResponseEntity<LocalAssociationProvinceDTO> updateLocalAssociationProvince(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LocalAssociationProvinceDTO localAssociationProvinceDTO
    ) throws URISyntaxException {
        log.debug("REST request to update LocalAssociationProvince : {}, {}", id, localAssociationProvinceDTO);
        if (localAssociationProvinceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, localAssociationProvinceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!localAssociationProvinceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LocalAssociationProvinceDTO result = localAssociationProvinceService.update(localAssociationProvinceDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, localAssociationProvinceDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /local-association-provinces/:id} : Partial updates given fields of an existing localAssociationProvince, field will ignore if it is null
     *
     * @param id the id of the localAssociationProvinceDTO to save.
     * @param localAssociationProvinceDTO the localAssociationProvinceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated localAssociationProvinceDTO,
     * or with status {@code 400 (Bad Request)} if the localAssociationProvinceDTO is not valid,
     * or with status {@code 404 (Not Found)} if the localAssociationProvinceDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the localAssociationProvinceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/local-association-provinces/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LocalAssociationProvinceDTO> partialUpdateLocalAssociationProvince(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LocalAssociationProvinceDTO localAssociationProvinceDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update LocalAssociationProvince partially : {}, {}", id, localAssociationProvinceDTO);
        if (localAssociationProvinceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, localAssociationProvinceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!localAssociationProvinceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LocalAssociationProvinceDTO> result = localAssociationProvinceService.partialUpdate(localAssociationProvinceDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, localAssociationProvinceDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /local-association-provinces} : get all the localAssociationProvinces.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of localAssociationProvinces in body.
     */
    @GetMapping("/local-association-provinces")
    public ResponseEntity<List<LocalAssociationProvinceDTO>> getAllLocalAssociationProvinces(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of LocalAssociationProvinces");
        Page<LocalAssociationProvinceDTO> page;
        if (eagerload) {
            page = localAssociationProvinceService.findAllWithEagerRelationships(pageable);
        } else {
            page = localAssociationProvinceService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /local-association-provinces/:id} : get the "id" localAssociationProvince.
     *
     * @param id the id of the localAssociationProvinceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the localAssociationProvinceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/local-association-provinces/{id}")
    public ResponseEntity<LocalAssociationProvinceDTO> getLocalAssociationProvince(@PathVariable Long id) {
        log.debug("REST request to get LocalAssociationProvince : {}", id);
        Optional<LocalAssociationProvinceDTO> localAssociationProvinceDTO = localAssociationProvinceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(localAssociationProvinceDTO);
    }

    /**
     * {@code DELETE  /local-association-provinces/:id} : delete the "id" localAssociationProvince.
     *
     * @param id the id of the localAssociationProvinceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/local-association-provinces/{id}")
    public ResponseEntity<Void> deleteLocalAssociationProvince(@PathVariable Long id) {
        log.debug("REST request to delete LocalAssociationProvince : {}", id);
        localAssociationProvinceService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
