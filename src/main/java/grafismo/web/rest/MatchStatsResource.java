package grafismo.web.rest;

import grafismo.repository.MatchStatsRepository;
import grafismo.service.MatchStatsService;
import grafismo.service.dto.MatchStatsDTO;
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
 * REST controller for managing {@link grafismo.domain.MatchStats}.
 */
@RestController
@RequestMapping("/api")
public class MatchStatsResource {

    private final Logger log = LoggerFactory.getLogger(MatchStatsResource.class);

    private static final String ENTITY_NAME = "matchStats";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MatchStatsService matchStatsService;

    private final MatchStatsRepository matchStatsRepository;

    public MatchStatsResource(MatchStatsService matchStatsService, MatchStatsRepository matchStatsRepository) {
        this.matchStatsService = matchStatsService;
        this.matchStatsRepository = matchStatsRepository;
    }

    /**
     * {@code POST  /match-stats} : Create a new matchStats.
     *
     * @param matchStatsDTO the matchStatsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new matchStatsDTO, or with status {@code 400 (Bad Request)} if the matchStats has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/match-stats")
    public ResponseEntity<MatchStatsDTO> createMatchStats(@Valid @RequestBody MatchStatsDTO matchStatsDTO) throws URISyntaxException {
        log.debug("REST request to save MatchStats : {}", matchStatsDTO);
        if (matchStatsDTO.getId() != null) {
            throw new BadRequestAlertException("A new matchStats cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MatchStatsDTO result = matchStatsService.save(matchStatsDTO);
        return ResponseEntity
            .created(new URI("/api/match-stats/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /match-stats/:id} : Updates an existing matchStats.
     *
     * @param id the id of the matchStatsDTO to save.
     * @param matchStatsDTO the matchStatsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated matchStatsDTO,
     * or with status {@code 400 (Bad Request)} if the matchStatsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the matchStatsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/match-stats/{id}")
    public ResponseEntity<MatchStatsDTO> updateMatchStats(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MatchStatsDTO matchStatsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update MatchStats : {}, {}", id, matchStatsDTO);
        if (matchStatsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, matchStatsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!matchStatsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MatchStatsDTO result = matchStatsService.update(matchStatsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, matchStatsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /match-stats/:id} : Partial updates given fields of an existing matchStats, field will ignore if it is null
     *
     * @param id the id of the matchStatsDTO to save.
     * @param matchStatsDTO the matchStatsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated matchStatsDTO,
     * or with status {@code 400 (Bad Request)} if the matchStatsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the matchStatsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the matchStatsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/match-stats/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MatchStatsDTO> partialUpdateMatchStats(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MatchStatsDTO matchStatsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update MatchStats partially : {}, {}", id, matchStatsDTO);
        if (matchStatsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, matchStatsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!matchStatsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MatchStatsDTO> result = matchStatsService.partialUpdate(matchStatsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, matchStatsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /match-stats} : get all the matchStats.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of matchStats in body.
     */
    @GetMapping("/match-stats")
    public ResponseEntity<List<MatchStatsDTO>> getAllMatchStats(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of MatchStats");
        Page<MatchStatsDTO> page = matchStatsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /match-stats/:id} : get the "id" matchStats.
     *
     * @param id the id of the matchStatsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the matchStatsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/match-stats/{id}")
    public ResponseEntity<MatchStatsDTO> getMatchStats(@PathVariable Long id) {
        log.debug("REST request to get MatchStats : {}", id);
        Optional<MatchStatsDTO> matchStatsDTO = matchStatsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(matchStatsDTO);
    }

    /**
     * {@code DELETE  /match-stats/:id} : delete the "id" matchStats.
     *
     * @param id the id of the matchStatsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/match-stats/{id}")
    public ResponseEntity<Void> deleteMatchStats(@PathVariable Long id) {
        log.debug("REST request to delete MatchStats : {}", id);
        matchStatsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
