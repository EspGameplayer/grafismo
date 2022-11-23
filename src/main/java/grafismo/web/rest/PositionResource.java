package grafismo.web.rest;

import grafismo.repository.PositionRepository;
import grafismo.service.PositionService;
import grafismo.service.dto.PositionDTO;
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
 * REST controller for managing {@link grafismo.domain.Position}.
 */
@RestController
@RequestMapping("/api")
public class PositionResource {

    private final Logger log = LoggerFactory.getLogger(PositionResource.class);

    private static final String ENTITY_NAME = "position";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PositionService positionService;

    private final PositionRepository positionRepository;

    public PositionResource(PositionService positionService, PositionRepository positionRepository) {
        this.positionService = positionService;
        this.positionRepository = positionRepository;
    }

    /**
     * {@code POST  /positions} : Create a new position.
     *
     * @param positionDTO the positionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new positionDTO, or with status {@code 400 (Bad Request)} if the position has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/positions")
    public ResponseEntity<PositionDTO> createPosition(@Valid @RequestBody PositionDTO positionDTO) throws URISyntaxException {
        log.debug("REST request to save Position : {}", positionDTO);
        if (positionDTO.getId() != null) {
            throw new BadRequestAlertException("A new position cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PositionDTO result = positionService.save(positionDTO);
        return ResponseEntity
            .created(new URI("/api/positions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /positions/:id} : Updates an existing position.
     *
     * @param id the id of the positionDTO to save.
     * @param positionDTO the positionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated positionDTO,
     * or with status {@code 400 (Bad Request)} if the positionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the positionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/positions/{id}")
    public ResponseEntity<PositionDTO> updatePosition(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PositionDTO positionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Position : {}, {}", id, positionDTO);
        if (positionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, positionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!positionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PositionDTO result = positionService.update(positionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, positionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /positions/:id} : Partial updates given fields of an existing position, field will ignore if it is null
     *
     * @param id the id of the positionDTO to save.
     * @param positionDTO the positionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated positionDTO,
     * or with status {@code 400 (Bad Request)} if the positionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the positionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the positionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/positions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PositionDTO> partialUpdatePosition(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PositionDTO positionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Position partially : {}, {}", id, positionDTO);
        if (positionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, positionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!positionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PositionDTO> result = positionService.partialUpdate(positionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, positionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /positions} : get all the positions.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of positions in body.
     */
    @GetMapping("/positions")
    public ResponseEntity<List<PositionDTO>> getAllPositions(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of Positions");
        Page<PositionDTO> page;
        if (eagerload) {
            page = positionService.findAllWithEagerRelationships(pageable);
        } else {
            page = positionService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /positions/:id} : get the "id" position.
     *
     * @param id the id of the positionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the positionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/positions/{id}")
    public ResponseEntity<PositionDTO> getPosition(@PathVariable Long id) {
        log.debug("REST request to get Position : {}", id);
        Optional<PositionDTO> positionDTO = positionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(positionDTO);
    }

    /**
     * {@code DELETE  /positions/:id} : delete the "id" position.
     *
     * @param id the id of the positionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/positions/{id}")
    public ResponseEntity<Void> deletePosition(@PathVariable Long id) {
        log.debug("REST request to delete Position : {}", id);
        positionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
