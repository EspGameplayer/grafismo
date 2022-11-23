package grafismo.web.rest;

import grafismo.repository.StadiumRepository;
import grafismo.service.StadiumService;
import grafismo.service.dto.StadiumDTO;
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
 * REST controller for managing {@link grafismo.domain.Stadium}.
 */
@RestController
@RequestMapping("/api")
public class StadiumResource {

    private final Logger log = LoggerFactory.getLogger(StadiumResource.class);

    private static final String ENTITY_NAME = "stadium";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StadiumService stadiumService;

    private final StadiumRepository stadiumRepository;

    public StadiumResource(StadiumService stadiumService, StadiumRepository stadiumRepository) {
        this.stadiumService = stadiumService;
        this.stadiumRepository = stadiumRepository;
    }

    /**
     * {@code POST  /stadiums} : Create a new stadium.
     *
     * @param stadiumDTO the stadiumDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new stadiumDTO, or with status {@code 400 (Bad Request)} if the stadium has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/stadiums")
    public ResponseEntity<StadiumDTO> createStadium(@Valid @RequestBody StadiumDTO stadiumDTO) throws URISyntaxException {
        log.debug("REST request to save Stadium : {}", stadiumDTO);
        if (stadiumDTO.getId() != null) {
            throw new BadRequestAlertException("A new stadium cannot already have an ID", ENTITY_NAME, "idexists");
        }
        StadiumDTO result = stadiumService.save(stadiumDTO);
        return ResponseEntity
            .created(new URI("/api/stadiums/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /stadiums/:id} : Updates an existing stadium.
     *
     * @param id the id of the stadiumDTO to save.
     * @param stadiumDTO the stadiumDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated stadiumDTO,
     * or with status {@code 400 (Bad Request)} if the stadiumDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the stadiumDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/stadiums/{id}")
    public ResponseEntity<StadiumDTO> updateStadium(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody StadiumDTO stadiumDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Stadium : {}, {}", id, stadiumDTO);
        if (stadiumDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, stadiumDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!stadiumRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        StadiumDTO result = stadiumService.update(stadiumDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, stadiumDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /stadiums/:id} : Partial updates given fields of an existing stadium, field will ignore if it is null
     *
     * @param id the id of the stadiumDTO to save.
     * @param stadiumDTO the stadiumDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated stadiumDTO,
     * or with status {@code 400 (Bad Request)} if the stadiumDTO is not valid,
     * or with status {@code 404 (Not Found)} if the stadiumDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the stadiumDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/stadiums/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<StadiumDTO> partialUpdateStadium(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody StadiumDTO stadiumDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Stadium partially : {}, {}", id, stadiumDTO);
        if (stadiumDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, stadiumDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!stadiumRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<StadiumDTO> result = stadiumService.partialUpdate(stadiumDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, stadiumDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /stadiums} : get all the stadiums.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of stadiums in body.
     */
    @GetMapping("/stadiums")
    public ResponseEntity<List<StadiumDTO>> getAllStadiums(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Stadiums");
        Page<StadiumDTO> page = stadiumService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /stadiums/:id} : get the "id" stadium.
     *
     * @param id the id of the stadiumDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the stadiumDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/stadiums/{id}")
    public ResponseEntity<StadiumDTO> getStadium(@PathVariable Long id) {
        log.debug("REST request to get Stadium : {}", id);
        Optional<StadiumDTO> stadiumDTO = stadiumService.findOne(id);
        return ResponseUtil.wrapOrNotFound(stadiumDTO);
    }

    /**
     * {@code DELETE  /stadiums/:id} : delete the "id" stadium.
     *
     * @param id the id of the stadiumDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/stadiums/{id}")
    public ResponseEntity<Void> deleteStadium(@PathVariable Long id) {
        log.debug("REST request to delete Stadium : {}", id);
        stadiumService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
