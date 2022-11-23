package grafismo.web.rest;

import grafismo.repository.CompetitionRepository;
import grafismo.service.CompetitionService;
import grafismo.service.dto.CompetitionDTO;
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
 * REST controller for managing {@link grafismo.domain.Competition}.
 */
@RestController
@RequestMapping("/api")
public class CompetitionResource {

    private final Logger log = LoggerFactory.getLogger(CompetitionResource.class);

    private static final String ENTITY_NAME = "competition";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CompetitionService competitionService;

    private final CompetitionRepository competitionRepository;

    public CompetitionResource(CompetitionService competitionService, CompetitionRepository competitionRepository) {
        this.competitionService = competitionService;
        this.competitionRepository = competitionRepository;
    }

    /**
     * {@code POST  /competitions} : Create a new competition.
     *
     * @param competitionDTO the competitionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new competitionDTO, or with status {@code 400 (Bad Request)} if the competition has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/competitions")
    public ResponseEntity<CompetitionDTO> createCompetition(@Valid @RequestBody CompetitionDTO competitionDTO) throws URISyntaxException {
        log.debug("REST request to save Competition : {}", competitionDTO);
        if (competitionDTO.getId() != null) {
            throw new BadRequestAlertException("A new competition cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CompetitionDTO result = competitionService.save(competitionDTO);
        return ResponseEntity
            .created(new URI("/api/competitions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /competitions/:id} : Updates an existing competition.
     *
     * @param id the id of the competitionDTO to save.
     * @param competitionDTO the competitionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated competitionDTO,
     * or with status {@code 400 (Bad Request)} if the competitionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the competitionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/competitions/{id}")
    public ResponseEntity<CompetitionDTO> updateCompetition(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CompetitionDTO competitionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Competition : {}, {}", id, competitionDTO);
        if (competitionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, competitionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!competitionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CompetitionDTO result = competitionService.update(competitionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, competitionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /competitions/:id} : Partial updates given fields of an existing competition, field will ignore if it is null
     *
     * @param id the id of the competitionDTO to save.
     * @param competitionDTO the competitionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated competitionDTO,
     * or with status {@code 400 (Bad Request)} if the competitionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the competitionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the competitionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/competitions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CompetitionDTO> partialUpdateCompetition(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CompetitionDTO competitionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Competition partially : {}, {}", id, competitionDTO);
        if (competitionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, competitionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!competitionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CompetitionDTO> result = competitionService.partialUpdate(competitionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, competitionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /competitions} : get all the competitions.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of competitions in body.
     */
    @GetMapping("/competitions")
    public ResponseEntity<List<CompetitionDTO>> getAllCompetitions(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of Competitions");
        Page<CompetitionDTO> page;
        if (eagerload) {
            page = competitionService.findAllWithEagerRelationships(pageable);
        } else {
            page = competitionService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /competitions/:id} : get the "id" competition.
     *
     * @param id the id of the competitionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the competitionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/competitions/{id}")
    public ResponseEntity<CompetitionDTO> getCompetition(@PathVariable Long id) {
        log.debug("REST request to get Competition : {}", id);
        Optional<CompetitionDTO> competitionDTO = competitionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(competitionDTO);
    }

    /**
     * {@code DELETE  /competitions/:id} : delete the "id" competition.
     *
     * @param id the id of the competitionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/competitions/{id}")
    public ResponseEntity<Void> deleteCompetition(@PathVariable Long id) {
        log.debug("REST request to delete Competition : {}", id);
        competitionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
