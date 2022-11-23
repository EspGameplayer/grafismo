package grafismo.web.rest;

import grafismo.repository.SystemConfigurationRepository;
import grafismo.service.SystemConfigurationService;
import grafismo.service.dto.SystemConfigurationDTO;
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
 * REST controller for managing {@link grafismo.domain.SystemConfiguration}.
 */
@RestController
@RequestMapping("/api")
public class SystemConfigurationResource {

    private final Logger log = LoggerFactory.getLogger(SystemConfigurationResource.class);

    private static final String ENTITY_NAME = "systemConfiguration";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SystemConfigurationService systemConfigurationService;

    private final SystemConfigurationRepository systemConfigurationRepository;

    public SystemConfigurationResource(
        SystemConfigurationService systemConfigurationService,
        SystemConfigurationRepository systemConfigurationRepository
    ) {
        this.systemConfigurationService = systemConfigurationService;
        this.systemConfigurationRepository = systemConfigurationRepository;
    }

    /**
     * {@code POST  /system-configurations} : Create a new systemConfiguration.
     *
     * @param systemConfigurationDTO the systemConfigurationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new systemConfigurationDTO, or with status {@code 400 (Bad Request)} if the systemConfiguration has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/system-configurations")
    public ResponseEntity<SystemConfigurationDTO> createSystemConfiguration(@RequestBody SystemConfigurationDTO systemConfigurationDTO)
        throws URISyntaxException {
        log.debug("REST request to save SystemConfiguration : {}", systemConfigurationDTO);
        if (systemConfigurationDTO.getId() != null) {
            throw new BadRequestAlertException("A new systemConfiguration cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SystemConfigurationDTO result = systemConfigurationService.save(systemConfigurationDTO);
        return ResponseEntity
            .created(new URI("/api/system-configurations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /system-configurations/:id} : Updates an existing systemConfiguration.
     *
     * @param id the id of the systemConfigurationDTO to save.
     * @param systemConfigurationDTO the systemConfigurationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated systemConfigurationDTO,
     * or with status {@code 400 (Bad Request)} if the systemConfigurationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the systemConfigurationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/system-configurations/{id}")
    public ResponseEntity<SystemConfigurationDTO> updateSystemConfiguration(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SystemConfigurationDTO systemConfigurationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SystemConfiguration : {}, {}", id, systemConfigurationDTO);
        if (systemConfigurationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, systemConfigurationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!systemConfigurationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SystemConfigurationDTO result = systemConfigurationService.update(systemConfigurationDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, systemConfigurationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /system-configurations/:id} : Partial updates given fields of an existing systemConfiguration, field will ignore if it is null
     *
     * @param id the id of the systemConfigurationDTO to save.
     * @param systemConfigurationDTO the systemConfigurationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated systemConfigurationDTO,
     * or with status {@code 400 (Bad Request)} if the systemConfigurationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the systemConfigurationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the systemConfigurationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/system-configurations/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SystemConfigurationDTO> partialUpdateSystemConfiguration(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SystemConfigurationDTO systemConfigurationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SystemConfiguration partially : {}, {}", id, systemConfigurationDTO);
        if (systemConfigurationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, systemConfigurationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!systemConfigurationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SystemConfigurationDTO> result = systemConfigurationService.partialUpdate(systemConfigurationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, systemConfigurationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /system-configurations} : get all the systemConfigurations.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of systemConfigurations in body.
     */
    @GetMapping("/system-configurations")
    public ResponseEntity<List<SystemConfigurationDTO>> getAllSystemConfigurations(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of SystemConfigurations");
        Page<SystemConfigurationDTO> page;
        if (eagerload) {
            page = systemConfigurationService.findAllWithEagerRelationships(pageable);
        } else {
            page = systemConfigurationService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /system-configurations/:id} : get the "id" systemConfiguration.
     *
     * @param id the id of the systemConfigurationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the systemConfigurationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/system-configurations/{id}")
    public ResponseEntity<SystemConfigurationDTO> getSystemConfiguration(@PathVariable Long id) {
        log.debug("REST request to get SystemConfiguration : {}", id);
        Optional<SystemConfigurationDTO> systemConfigurationDTO = systemConfigurationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(systemConfigurationDTO);
    }

    /**
     * {@code DELETE  /system-configurations/:id} : delete the "id" systemConfiguration.
     *
     * @param id the id of the systemConfigurationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/system-configurations/{id}")
    public ResponseEntity<Void> deleteSystemConfiguration(@PathVariable Long id) {
        log.debug("REST request to delete SystemConfiguration : {}", id);
        systemConfigurationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
