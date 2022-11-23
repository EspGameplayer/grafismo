package grafismo.web.rest;

import grafismo.repository.ShirtRepository;
import grafismo.service.ShirtService;
import grafismo.service.dto.ShirtDTO;
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
 * REST controller for managing {@link grafismo.domain.Shirt}.
 */
@RestController
@RequestMapping("/api")
public class ShirtResource {

    private final Logger log = LoggerFactory.getLogger(ShirtResource.class);

    private static final String ENTITY_NAME = "shirt";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ShirtService shirtService;

    private final ShirtRepository shirtRepository;

    public ShirtResource(ShirtService shirtService, ShirtRepository shirtRepository) {
        this.shirtService = shirtService;
        this.shirtRepository = shirtRepository;
    }

    /**
     * {@code POST  /shirts} : Create a new shirt.
     *
     * @param shirtDTO the shirtDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new shirtDTO, or with status {@code 400 (Bad Request)} if the shirt has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/shirts")
    public ResponseEntity<ShirtDTO> createShirt(@Valid @RequestBody ShirtDTO shirtDTO) throws URISyntaxException {
        log.debug("REST request to save Shirt : {}", shirtDTO);
        if (shirtDTO.getId() != null) {
            throw new BadRequestAlertException("A new shirt cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ShirtDTO result = shirtService.save(shirtDTO);
        return ResponseEntity
            .created(new URI("/api/shirts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /shirts/:id} : Updates an existing shirt.
     *
     * @param id the id of the shirtDTO to save.
     * @param shirtDTO the shirtDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shirtDTO,
     * or with status {@code 400 (Bad Request)} if the shirtDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the shirtDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/shirts/{id}")
    public ResponseEntity<ShirtDTO> updateShirt(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ShirtDTO shirtDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Shirt : {}, {}", id, shirtDTO);
        if (shirtDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, shirtDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!shirtRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ShirtDTO result = shirtService.update(shirtDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, shirtDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /shirts/:id} : Partial updates given fields of an existing shirt, field will ignore if it is null
     *
     * @param id the id of the shirtDTO to save.
     * @param shirtDTO the shirtDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shirtDTO,
     * or with status {@code 400 (Bad Request)} if the shirtDTO is not valid,
     * or with status {@code 404 (Not Found)} if the shirtDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the shirtDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/shirts/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ShirtDTO> partialUpdateShirt(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ShirtDTO shirtDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Shirt partially : {}, {}", id, shirtDTO);
        if (shirtDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, shirtDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!shirtRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ShirtDTO> result = shirtService.partialUpdate(shirtDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, shirtDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /shirts} : get all the shirts.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of shirts in body.
     */
    @GetMapping("/shirts")
    public ResponseEntity<List<ShirtDTO>> getAllShirts(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of Shirts");
        Page<ShirtDTO> page;
        if (eagerload) {
            page = shirtService.findAllWithEagerRelationships(pageable);
        } else {
            page = shirtService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /shirts/:id} : get the "id" shirt.
     *
     * @param id the id of the shirtDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the shirtDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/shirts/{id}")
    public ResponseEntity<ShirtDTO> getShirt(@PathVariable Long id) {
        log.debug("REST request to get Shirt : {}", id);
        Optional<ShirtDTO> shirtDTO = shirtService.findOne(id);
        return ResponseUtil.wrapOrNotFound(shirtDTO);
    }

    /**
     * {@code DELETE  /shirts/:id} : delete the "id" shirt.
     *
     * @param id the id of the shirtDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/shirts/{id}")
    public ResponseEntity<Void> deleteShirt(@PathVariable Long id) {
        log.debug("REST request to delete Shirt : {}", id);
        shirtService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
