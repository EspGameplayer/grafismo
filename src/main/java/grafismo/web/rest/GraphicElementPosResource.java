package grafismo.web.rest;

import grafismo.repository.GraphicElementPosRepository;
import grafismo.service.GraphicElementPosService;
import grafismo.service.dto.GraphicElementPosDTO;
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
 * REST controller for managing {@link grafismo.domain.GraphicElementPos}.
 */
@RestController
@RequestMapping("/api")
public class GraphicElementPosResource {

    private final Logger log = LoggerFactory.getLogger(GraphicElementPosResource.class);

    private static final String ENTITY_NAME = "graphicElementPos";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GraphicElementPosService graphicElementPosService;

    private final GraphicElementPosRepository graphicElementPosRepository;

    public GraphicElementPosResource(
        GraphicElementPosService graphicElementPosService,
        GraphicElementPosRepository graphicElementPosRepository
    ) {
        this.graphicElementPosService = graphicElementPosService;
        this.graphicElementPosRepository = graphicElementPosRepository;
    }

    /**
     * {@code POST  /graphic-element-pos} : Create a new graphicElementPos.
     *
     * @param graphicElementPosDTO the graphicElementPosDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new graphicElementPosDTO, or with status {@code 400 (Bad Request)} if the graphicElementPos has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/graphic-element-pos")
    public ResponseEntity<GraphicElementPosDTO> createGraphicElementPos(@Valid @RequestBody GraphicElementPosDTO graphicElementPosDTO)
        throws URISyntaxException {
        log.debug("REST request to save GraphicElementPos : {}", graphicElementPosDTO);
        if (graphicElementPosDTO.getId() != null) {
            throw new BadRequestAlertException("A new graphicElementPos cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GraphicElementPosDTO result = graphicElementPosService.save(graphicElementPosDTO);
        return ResponseEntity
            .created(new URI("/api/graphic-element-pos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /graphic-element-pos/:id} : Updates an existing graphicElementPos.
     *
     * @param id the id of the graphicElementPosDTO to save.
     * @param graphicElementPosDTO the graphicElementPosDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated graphicElementPosDTO,
     * or with status {@code 400 (Bad Request)} if the graphicElementPosDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the graphicElementPosDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/graphic-element-pos/{id}")
    public ResponseEntity<GraphicElementPosDTO> updateGraphicElementPos(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody GraphicElementPosDTO graphicElementPosDTO
    ) throws URISyntaxException {
        log.debug("REST request to update GraphicElementPos : {}, {}", id, graphicElementPosDTO);
        if (graphicElementPosDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, graphicElementPosDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!graphicElementPosRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        GraphicElementPosDTO result = graphicElementPosService.update(graphicElementPosDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, graphicElementPosDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /graphic-element-pos/:id} : Partial updates given fields of an existing graphicElementPos, field will ignore if it is null
     *
     * @param id the id of the graphicElementPosDTO to save.
     * @param graphicElementPosDTO the graphicElementPosDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated graphicElementPosDTO,
     * or with status {@code 400 (Bad Request)} if the graphicElementPosDTO is not valid,
     * or with status {@code 404 (Not Found)} if the graphicElementPosDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the graphicElementPosDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/graphic-element-pos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<GraphicElementPosDTO> partialUpdateGraphicElementPos(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody GraphicElementPosDTO graphicElementPosDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update GraphicElementPos partially : {}, {}", id, graphicElementPosDTO);
        if (graphicElementPosDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, graphicElementPosDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!graphicElementPosRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<GraphicElementPosDTO> result = graphicElementPosService.partialUpdate(graphicElementPosDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, graphicElementPosDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /graphic-element-pos} : get all the graphicElementPos.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of graphicElementPos in body.
     */
    @GetMapping("/graphic-element-pos")
    public ResponseEntity<List<GraphicElementPosDTO>> getAllGraphicElementPos(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of GraphicElementPos");
        Page<GraphicElementPosDTO> page;
        if (eagerload) {
            page = graphicElementPosService.findAllWithEagerRelationships(pageable);
        } else {
            page = graphicElementPosService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /graphic-element-pos/:id} : get the "id" graphicElementPos.
     *
     * @param id the id of the graphicElementPosDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the graphicElementPosDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/graphic-element-pos/{id}")
    public ResponseEntity<GraphicElementPosDTO> getGraphicElementPos(@PathVariable Long id) {
        log.debug("REST request to get GraphicElementPos : {}", id);
        Optional<GraphicElementPosDTO> graphicElementPosDTO = graphicElementPosService.findOne(id);
        return ResponseUtil.wrapOrNotFound(graphicElementPosDTO);
    }

    /**
     * {@code DELETE  /graphic-element-pos/:id} : delete the "id" graphicElementPos.
     *
     * @param id the id of the graphicElementPosDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/graphic-element-pos/{id}")
    public ResponseEntity<Void> deleteGraphicElementPos(@PathVariable Long id) {
        log.debug("REST request to delete GraphicElementPos : {}", id);
        graphicElementPosService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
