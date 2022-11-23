package grafismo.web.rest;

import grafismo.repository.InjuryRepository;
import grafismo.service.InjuryService;
import grafismo.service.dto.InjuryDTO;
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
 * REST controller for managing {@link grafismo.domain.Injury}.
 */
@RestController
@RequestMapping("/api")
public class InjuryResource {

    private final Logger log = LoggerFactory.getLogger(InjuryResource.class);

    private static final String ENTITY_NAME = "injury";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InjuryService injuryService;

    private final InjuryRepository injuryRepository;

    public InjuryResource(InjuryService injuryService, InjuryRepository injuryRepository) {
        this.injuryService = injuryService;
        this.injuryRepository = injuryRepository;
    }

    /**
     * {@code POST  /injuries} : Create a new injury.
     *
     * @param injuryDTO the injuryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new injuryDTO, or with status {@code 400 (Bad Request)} if the injury has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/injuries")
    public ResponseEntity<InjuryDTO> createInjury(@Valid @RequestBody InjuryDTO injuryDTO) throws URISyntaxException {
        log.debug("REST request to save Injury : {}", injuryDTO);
        if (injuryDTO.getId() != null) {
            throw new BadRequestAlertException("A new injury cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InjuryDTO result = injuryService.save(injuryDTO);
        return ResponseEntity
            .created(new URI("/api/injuries/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /injuries/:id} : Updates an existing injury.
     *
     * @param id the id of the injuryDTO to save.
     * @param injuryDTO the injuryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated injuryDTO,
     * or with status {@code 400 (Bad Request)} if the injuryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the injuryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/injuries/{id}")
    public ResponseEntity<InjuryDTO> updateInjury(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody InjuryDTO injuryDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Injury : {}, {}", id, injuryDTO);
        if (injuryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, injuryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!injuryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        InjuryDTO result = injuryService.update(injuryDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, injuryDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /injuries/:id} : Partial updates given fields of an existing injury, field will ignore if it is null
     *
     * @param id the id of the injuryDTO to save.
     * @param injuryDTO the injuryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated injuryDTO,
     * or with status {@code 400 (Bad Request)} if the injuryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the injuryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the injuryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/injuries/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InjuryDTO> partialUpdateInjury(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody InjuryDTO injuryDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Injury partially : {}, {}", id, injuryDTO);
        if (injuryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, injuryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!injuryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InjuryDTO> result = injuryService.partialUpdate(injuryDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, injuryDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /injuries} : get all the injuries.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of injuries in body.
     */
    @GetMapping("/injuries")
    public ResponseEntity<List<InjuryDTO>> getAllInjuries(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Injuries");
        Page<InjuryDTO> page = injuryService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /injuries/:id} : get the "id" injury.
     *
     * @param id the id of the injuryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the injuryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/injuries/{id}")
    public ResponseEntity<InjuryDTO> getInjury(@PathVariable Long id) {
        log.debug("REST request to get Injury : {}", id);
        Optional<InjuryDTO> injuryDTO = injuryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(injuryDTO);
    }

    /**
     * {@code DELETE  /injuries/:id} : delete the "id" injury.
     *
     * @param id the id of the injuryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/injuries/{id}")
    public ResponseEntity<Void> deleteInjury(@PathVariable Long id) {
        log.debug("REST request to delete Injury : {}", id);
        injuryService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
