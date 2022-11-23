package grafismo.web.rest;

import grafismo.repository.LineupRepository;
import grafismo.service.LineupService;
import grafismo.service.dto.LineupDTO;
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
 * REST controller for managing {@link grafismo.domain.Lineup}.
 */
@RestController
@RequestMapping("/api")
public class LineupResource {

    private final Logger log = LoggerFactory.getLogger(LineupResource.class);

    private static final String ENTITY_NAME = "lineup";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LineupService lineupService;

    private final LineupRepository lineupRepository;

    public LineupResource(LineupService lineupService, LineupRepository lineupRepository) {
        this.lineupService = lineupService;
        this.lineupRepository = lineupRepository;
    }

    /**
     * {@code POST  /lineups} : Create a new lineup.
     *
     * @param lineupDTO the lineupDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new lineupDTO, or with status {@code 400 (Bad Request)} if the lineup has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/lineups")
    public ResponseEntity<LineupDTO> createLineup(@Valid @RequestBody LineupDTO lineupDTO) throws URISyntaxException {
        log.debug("REST request to save Lineup : {}", lineupDTO);
        if (lineupDTO.getId() != null) {
            throw new BadRequestAlertException("A new lineup cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LineupDTO result = lineupService.save(lineupDTO);
        return ResponseEntity
            .created(new URI("/api/lineups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /lineups/:id} : Updates an existing lineup.
     *
     * @param id the id of the lineupDTO to save.
     * @param lineupDTO the lineupDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lineupDTO,
     * or with status {@code 400 (Bad Request)} if the lineupDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the lineupDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/lineups/{id}")
    public ResponseEntity<LineupDTO> updateLineup(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LineupDTO lineupDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Lineup : {}, {}", id, lineupDTO);
        if (lineupDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lineupDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!lineupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LineupDTO result = lineupService.update(lineupDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, lineupDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /lineups/:id} : Partial updates given fields of an existing lineup, field will ignore if it is null
     *
     * @param id the id of the lineupDTO to save.
     * @param lineupDTO the lineupDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lineupDTO,
     * or with status {@code 400 (Bad Request)} if the lineupDTO is not valid,
     * or with status {@code 404 (Not Found)} if the lineupDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the lineupDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/lineups/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LineupDTO> partialUpdateLineup(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LineupDTO lineupDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Lineup partially : {}, {}", id, lineupDTO);
        if (lineupDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lineupDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!lineupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LineupDTO> result = lineupService.partialUpdate(lineupDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, lineupDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /lineups} : get all the lineups.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of lineups in body.
     */
    @GetMapping("/lineups")
    public ResponseEntity<List<LineupDTO>> getAllLineups(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of Lineups");
        Page<LineupDTO> page;
        if (eagerload) {
            page = lineupService.findAllWithEagerRelationships(pageable);
        } else {
            page = lineupService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /lineups/:id} : get the "id" lineup.
     *
     * @param id the id of the lineupDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the lineupDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/lineups/{id}")
    public ResponseEntity<LineupDTO> getLineup(@PathVariable Long id) {
        log.debug("REST request to get Lineup : {}", id);
        Optional<LineupDTO> lineupDTO = lineupService.findOne(id);
        return ResponseUtil.wrapOrNotFound(lineupDTO);
    }

    /**
     * {@code DELETE  /lineups/:id} : delete the "id" lineup.
     *
     * @param id the id of the lineupDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/lineups/{id}")
    public ResponseEntity<Void> deleteLineup(@PathVariable Long id) {
        log.debug("REST request to delete Lineup : {}", id);
        lineupService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
