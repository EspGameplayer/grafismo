package grafismo.web.rest;

import grafismo.repository.MatchdayRepository;
import grafismo.service.MatchdayService;
import grafismo.service.dto.MatchdayDTO;
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
 * REST controller for managing {@link grafismo.domain.Matchday}.
 */
@RestController
@RequestMapping("/api")
public class MatchdayResource {

    private final Logger log = LoggerFactory.getLogger(MatchdayResource.class);

    private static final String ENTITY_NAME = "matchday";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MatchdayService matchdayService;

    private final MatchdayRepository matchdayRepository;

    public MatchdayResource(MatchdayService matchdayService, MatchdayRepository matchdayRepository) {
        this.matchdayService = matchdayService;
        this.matchdayRepository = matchdayRepository;
    }

    /**
     * {@code POST  /matchdays} : Create a new matchday.
     *
     * @param matchdayDTO the matchdayDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new matchdayDTO, or with status {@code 400 (Bad Request)} if the matchday has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/matchdays")
    public ResponseEntity<MatchdayDTO> createMatchday(@Valid @RequestBody MatchdayDTO matchdayDTO) throws URISyntaxException {
        log.debug("REST request to save Matchday : {}", matchdayDTO);
        if (matchdayDTO.getId() != null) {
            throw new BadRequestAlertException("A new matchday cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MatchdayDTO result = matchdayService.save(matchdayDTO);
        return ResponseEntity
            .created(new URI("/api/matchdays/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /matchdays/:id} : Updates an existing matchday.
     *
     * @param id the id of the matchdayDTO to save.
     * @param matchdayDTO the matchdayDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated matchdayDTO,
     * or with status {@code 400 (Bad Request)} if the matchdayDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the matchdayDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/matchdays/{id}")
    public ResponseEntity<MatchdayDTO> updateMatchday(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MatchdayDTO matchdayDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Matchday : {}, {}", id, matchdayDTO);
        if (matchdayDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, matchdayDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!matchdayRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MatchdayDTO result = matchdayService.update(matchdayDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, matchdayDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /matchdays/:id} : Partial updates given fields of an existing matchday, field will ignore if it is null
     *
     * @param id the id of the matchdayDTO to save.
     * @param matchdayDTO the matchdayDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated matchdayDTO,
     * or with status {@code 400 (Bad Request)} if the matchdayDTO is not valid,
     * or with status {@code 404 (Not Found)} if the matchdayDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the matchdayDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/matchdays/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MatchdayDTO> partialUpdateMatchday(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MatchdayDTO matchdayDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Matchday partially : {}, {}", id, matchdayDTO);
        if (matchdayDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, matchdayDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!matchdayRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MatchdayDTO> result = matchdayService.partialUpdate(matchdayDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, matchdayDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /matchdays} : get all the matchdays.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of matchdays in body.
     */
    @GetMapping("/matchdays")
    public ResponseEntity<List<MatchdayDTO>> getAllMatchdays(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of Matchdays");
        Page<MatchdayDTO> page;
        if (eagerload) {
            page = matchdayService.findAllWithEagerRelationships(pageable);
        } else {
            page = matchdayService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /matchdays/:id} : get the "id" matchday.
     *
     * @param id the id of the matchdayDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the matchdayDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/matchdays/{id}")
    public ResponseEntity<MatchdayDTO> getMatchday(@PathVariable Long id) {
        log.debug("REST request to get Matchday : {}", id);
        Optional<MatchdayDTO> matchdayDTO = matchdayService.findOne(id);
        return ResponseUtil.wrapOrNotFound(matchdayDTO);
    }

    /**
     * {@code DELETE  /matchdays/:id} : delete the "id" matchday.
     *
     * @param id the id of the matchdayDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/matchdays/{id}")
    public ResponseEntity<Void> deleteMatchday(@PathVariable Long id) {
        log.debug("REST request to delete Matchday : {}", id);
        matchdayService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
