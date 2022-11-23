package grafismo.web.rest;

import grafismo.repository.ActionKeyRepository;
import grafismo.service.ActionKeyService;
import grafismo.service.dto.ActionKeyDTO;
import grafismo.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
 * REST controller for managing {@link grafismo.domain.ActionKey}.
 */
@RestController
@RequestMapping("/api")
public class ActionKeyResource {

    private final Logger log = LoggerFactory.getLogger(ActionKeyResource.class);

    private static final String ENTITY_NAME = "actionKey";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ActionKeyService actionKeyService;

    private final ActionKeyRepository actionKeyRepository;

    public ActionKeyResource(ActionKeyService actionKeyService, ActionKeyRepository actionKeyRepository) {
        this.actionKeyService = actionKeyService;
        this.actionKeyRepository = actionKeyRepository;
    }

    /**
     * {@code POST  /action-keys} : Create a new actionKey.
     *
     * @param actionKeyDTO the actionKeyDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new actionKeyDTO, or with status {@code 400 (Bad Request)} if the actionKey has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/action-keys")
    public ResponseEntity<ActionKeyDTO> createActionKey(@RequestBody ActionKeyDTO actionKeyDTO) throws URISyntaxException {
        log.debug("REST request to save ActionKey : {}", actionKeyDTO);
        if (actionKeyDTO.getId() != null) {
            throw new BadRequestAlertException("A new actionKey cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ActionKeyDTO result = actionKeyService.save(actionKeyDTO);
        return ResponseEntity
            .created(new URI("/api/action-keys/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /action-keys/:id} : Updates an existing actionKey.
     *
     * @param id the id of the actionKeyDTO to save.
     * @param actionKeyDTO the actionKeyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated actionKeyDTO,
     * or with status {@code 400 (Bad Request)} if the actionKeyDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the actionKeyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/action-keys/{id}")
    public ResponseEntity<ActionKeyDTO> updateActionKey(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ActionKeyDTO actionKeyDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ActionKey : {}, {}", id, actionKeyDTO);
        if (actionKeyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, actionKeyDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!actionKeyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ActionKeyDTO result = actionKeyService.update(actionKeyDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, actionKeyDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /action-keys/:id} : Partial updates given fields of an existing actionKey, field will ignore if it is null
     *
     * @param id the id of the actionKeyDTO to save.
     * @param actionKeyDTO the actionKeyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated actionKeyDTO,
     * or with status {@code 400 (Bad Request)} if the actionKeyDTO is not valid,
     * or with status {@code 404 (Not Found)} if the actionKeyDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the actionKeyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/action-keys/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ActionKeyDTO> partialUpdateActionKey(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ActionKeyDTO actionKeyDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ActionKey partially : {}, {}", id, actionKeyDTO);
        if (actionKeyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, actionKeyDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!actionKeyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ActionKeyDTO> result = actionKeyService.partialUpdate(actionKeyDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, actionKeyDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /action-keys} : get all the actionKeys.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of actionKeys in body.
     */
    @GetMapping("/action-keys")
    public ResponseEntity<List<ActionKeyDTO>> getAllActionKeys(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of ActionKeys");
        Page<ActionKeyDTO> page = actionKeyService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /action-keys/:id} : get the "id" actionKey.
     *
     * @param id the id of the actionKeyDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the actionKeyDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/action-keys/{id}")
    public ResponseEntity<ActionKeyDTO> getActionKey(@PathVariable Long id) {
        log.debug("REST request to get ActionKey : {}", id);
        Optional<ActionKeyDTO> actionKeyDTO = actionKeyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(actionKeyDTO);
    }

    /**
     * {@code DELETE  /action-keys/:id} : delete the "id" actionKey.
     *
     * @param id the id of the actionKeyDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/action-keys/{id}")
    public ResponseEntity<Void> deleteActionKey(@PathVariable Long id) {
        log.debug("REST request to delete ActionKey : {}", id);
        actionKeyService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
