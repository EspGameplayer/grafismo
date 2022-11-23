package grafismo.web.rest;

import grafismo.repository.CompetitionPlayerRepository;
import grafismo.service.CompetitionPlayerService;
import grafismo.service.dto.CompetitionPlayerDTO;
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
 * REST controller for managing {@link grafismo.domain.CompetitionPlayer}.
 */
@RestController
@RequestMapping("/api")
public class CompetitionPlayerResource {

    private final Logger log = LoggerFactory.getLogger(CompetitionPlayerResource.class);

    private static final String ENTITY_NAME = "competitionPlayer";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CompetitionPlayerService competitionPlayerService;

    private final CompetitionPlayerRepository competitionPlayerRepository;

    public CompetitionPlayerResource(
        CompetitionPlayerService competitionPlayerService,
        CompetitionPlayerRepository competitionPlayerRepository
    ) {
        this.competitionPlayerService = competitionPlayerService;
        this.competitionPlayerRepository = competitionPlayerRepository;
    }

    /**
     * {@code POST  /competition-players} : Create a new competitionPlayer.
     *
     * @param competitionPlayerDTO the competitionPlayerDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new competitionPlayerDTO, or with status {@code 400 (Bad Request)} if the competitionPlayer has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/competition-players")
    public ResponseEntity<CompetitionPlayerDTO> createCompetitionPlayer(@Valid @RequestBody CompetitionPlayerDTO competitionPlayerDTO)
        throws URISyntaxException {
        log.debug("REST request to save CompetitionPlayer : {}", competitionPlayerDTO);
        if (competitionPlayerDTO.getId() != null) {
            throw new BadRequestAlertException("A new competitionPlayer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CompetitionPlayerDTO result = competitionPlayerService.save(competitionPlayerDTO);
        return ResponseEntity
            .created(new URI("/api/competition-players/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /competition-players/:id} : Updates an existing competitionPlayer.
     *
     * @param id the id of the competitionPlayerDTO to save.
     * @param competitionPlayerDTO the competitionPlayerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated competitionPlayerDTO,
     * or with status {@code 400 (Bad Request)} if the competitionPlayerDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the competitionPlayerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/competition-players/{id}")
    public ResponseEntity<CompetitionPlayerDTO> updateCompetitionPlayer(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CompetitionPlayerDTO competitionPlayerDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CompetitionPlayer : {}, {}", id, competitionPlayerDTO);
        if (competitionPlayerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, competitionPlayerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!competitionPlayerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CompetitionPlayerDTO result = competitionPlayerService.update(competitionPlayerDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, competitionPlayerDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /competition-players/:id} : Partial updates given fields of an existing competitionPlayer, field will ignore if it is null
     *
     * @param id the id of the competitionPlayerDTO to save.
     * @param competitionPlayerDTO the competitionPlayerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated competitionPlayerDTO,
     * or with status {@code 400 (Bad Request)} if the competitionPlayerDTO is not valid,
     * or with status {@code 404 (Not Found)} if the competitionPlayerDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the competitionPlayerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/competition-players/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CompetitionPlayerDTO> partialUpdateCompetitionPlayer(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CompetitionPlayerDTO competitionPlayerDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CompetitionPlayer partially : {}, {}", id, competitionPlayerDTO);
        if (competitionPlayerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, competitionPlayerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!competitionPlayerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CompetitionPlayerDTO> result = competitionPlayerService.partialUpdate(competitionPlayerDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, competitionPlayerDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /competition-players} : get all the competitionPlayers.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of competitionPlayers in body.
     */
    @GetMapping("/competition-players")
    public ResponseEntity<List<CompetitionPlayerDTO>> getAllCompetitionPlayers(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of CompetitionPlayers");
        Page<CompetitionPlayerDTO> page;
        if (eagerload) {
            page = competitionPlayerService.findAllWithEagerRelationships(pageable);
        } else {
            page = competitionPlayerService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /competition-players/:id} : get the "id" competitionPlayer.
     *
     * @param id the id of the competitionPlayerDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the competitionPlayerDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/competition-players/{id}")
    public ResponseEntity<CompetitionPlayerDTO> getCompetitionPlayer(@PathVariable Long id) {
        log.debug("REST request to get CompetitionPlayer : {}", id);
        Optional<CompetitionPlayerDTO> competitionPlayerDTO = competitionPlayerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(competitionPlayerDTO);
    }

    /**
     * {@code DELETE  /competition-players/:id} : delete the "id" competitionPlayer.
     *
     * @param id the id of the competitionPlayerDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/competition-players/{id}")
    public ResponseEntity<Void> deleteCompetitionPlayer(@PathVariable Long id) {
        log.debug("REST request to delete CompetitionPlayer : {}", id);
        competitionPlayerService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
