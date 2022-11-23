package grafismo.service;

import grafismo.domain.GraphicElementPos;
import grafismo.repository.GraphicElementPosRepository;
import grafismo.service.dto.GraphicElementPosDTO;
import grafismo.service.mapper.GraphicElementPosMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link GraphicElementPos}.
 */
@Service
@Transactional
public class GraphicElementPosService {

    private final Logger log = LoggerFactory.getLogger(GraphicElementPosService.class);

    private final GraphicElementPosRepository graphicElementPosRepository;

    private final GraphicElementPosMapper graphicElementPosMapper;

    public GraphicElementPosService(
        GraphicElementPosRepository graphicElementPosRepository,
        GraphicElementPosMapper graphicElementPosMapper
    ) {
        this.graphicElementPosRepository = graphicElementPosRepository;
        this.graphicElementPosMapper = graphicElementPosMapper;
    }

    /**
     * Save a graphicElementPos.
     *
     * @param graphicElementPosDTO the entity to save.
     * @return the persisted entity.
     */
    public GraphicElementPosDTO save(GraphicElementPosDTO graphicElementPosDTO) {
        log.debug("Request to save GraphicElementPos : {}", graphicElementPosDTO);
        GraphicElementPos graphicElementPos = graphicElementPosMapper.toEntity(graphicElementPosDTO);
        graphicElementPos = graphicElementPosRepository.save(graphicElementPos);
        return graphicElementPosMapper.toDto(graphicElementPos);
    }

    /**
     * Update a graphicElementPos.
     *
     * @param graphicElementPosDTO the entity to save.
     * @return the persisted entity.
     */
    public GraphicElementPosDTO update(GraphicElementPosDTO graphicElementPosDTO) {
        log.debug("Request to save GraphicElementPos : {}", graphicElementPosDTO);
        GraphicElementPos graphicElementPos = graphicElementPosMapper.toEntity(graphicElementPosDTO);
        graphicElementPos = graphicElementPosRepository.save(graphicElementPos);
        return graphicElementPosMapper.toDto(graphicElementPos);
    }

    /**
     * Partially update a graphicElementPos.
     *
     * @param graphicElementPosDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<GraphicElementPosDTO> partialUpdate(GraphicElementPosDTO graphicElementPosDTO) {
        log.debug("Request to partially update GraphicElementPos : {}", graphicElementPosDTO);

        return graphicElementPosRepository
            .findById(graphicElementPosDTO.getId())
            .map(existingGraphicElementPos -> {
                graphicElementPosMapper.partialUpdate(existingGraphicElementPos, graphicElementPosDTO);

                return existingGraphicElementPos;
            })
            .map(graphicElementPosRepository::save)
            .map(graphicElementPosMapper::toDto);
    }

    /**
     * Get all the graphicElementPos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<GraphicElementPosDTO> findAll(Pageable pageable) {
        log.debug("Request to get all GraphicElementPos");
        return graphicElementPosRepository.findAll(pageable).map(graphicElementPosMapper::toDto);
    }

    /**
     * Get all the graphicElementPos with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<GraphicElementPosDTO> findAllWithEagerRelationships(Pageable pageable) {
        return graphicElementPosRepository.findAllWithEagerRelationships(pageable).map(graphicElementPosMapper::toDto);
    }

    /**
     * Get one graphicElementPos by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<GraphicElementPosDTO> findOne(Long id) {
        log.debug("Request to get GraphicElementPos : {}", id);
        return graphicElementPosRepository.findOneWithEagerRelationships(id).map(graphicElementPosMapper::toDto);
    }

    /**
     * Delete the graphicElementPos by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete GraphicElementPos : {}", id);
        graphicElementPosRepository.deleteById(id);
    }
}
