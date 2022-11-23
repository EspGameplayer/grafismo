package grafismo.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import grafismo.IntegrationTest;
import grafismo.domain.GraphicElementPos;
import grafismo.repository.GraphicElementPosRepository;
import grafismo.service.GraphicElementPosService;
import grafismo.service.dto.GraphicElementPosDTO;
import grafismo.service.mapper.GraphicElementPosMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link GraphicElementPosResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class GraphicElementPosResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Double DEFAULT_X = 1D;
    private static final Double UPDATED_X = 2D;

    private static final Double DEFAULT_Y = 1D;
    private static final Double UPDATED_Y = 2D;

    private static final String ENTITY_API_URL = "/api/graphic-element-pos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private GraphicElementPosRepository graphicElementPosRepository;

    @Mock
    private GraphicElementPosRepository graphicElementPosRepositoryMock;

    @Autowired
    private GraphicElementPosMapper graphicElementPosMapper;

    @Mock
    private GraphicElementPosService graphicElementPosServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGraphicElementPosMockMvc;

    private GraphicElementPos graphicElementPos;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GraphicElementPos createEntity(EntityManager em) {
        GraphicElementPos graphicElementPos = new GraphicElementPos().name(DEFAULT_NAME).x(DEFAULT_X).y(DEFAULT_Y);
        return graphicElementPos;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GraphicElementPos createUpdatedEntity(EntityManager em) {
        GraphicElementPos graphicElementPos = new GraphicElementPos().name(UPDATED_NAME).x(UPDATED_X).y(UPDATED_Y);
        return graphicElementPos;
    }

    @BeforeEach
    public void initTest() {
        graphicElementPos = createEntity(em);
    }

    @Test
    @Transactional
    void createGraphicElementPos() throws Exception {
        int databaseSizeBeforeCreate = graphicElementPosRepository.findAll().size();
        // Create the GraphicElementPos
        GraphicElementPosDTO graphicElementPosDTO = graphicElementPosMapper.toDto(graphicElementPos);
        restGraphicElementPosMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(graphicElementPosDTO))
            )
            .andExpect(status().isCreated());

        // Validate the GraphicElementPos in the database
        List<GraphicElementPos> graphicElementPosList = graphicElementPosRepository.findAll();
        assertThat(graphicElementPosList).hasSize(databaseSizeBeforeCreate + 1);
        GraphicElementPos testGraphicElementPos = graphicElementPosList.get(graphicElementPosList.size() - 1);
        assertThat(testGraphicElementPos.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testGraphicElementPos.getX()).isEqualTo(DEFAULT_X);
        assertThat(testGraphicElementPos.getY()).isEqualTo(DEFAULT_Y);
    }

    @Test
    @Transactional
    void createGraphicElementPosWithExistingId() throws Exception {
        // Create the GraphicElementPos with an existing ID
        graphicElementPos.setId(1L);
        GraphicElementPosDTO graphicElementPosDTO = graphicElementPosMapper.toDto(graphicElementPos);

        int databaseSizeBeforeCreate = graphicElementPosRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGraphicElementPosMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(graphicElementPosDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GraphicElementPos in the database
        List<GraphicElementPos> graphicElementPosList = graphicElementPosRepository.findAll();
        assertThat(graphicElementPosList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = graphicElementPosRepository.findAll().size();
        // set the field null
        graphicElementPos.setName(null);

        // Create the GraphicElementPos, which fails.
        GraphicElementPosDTO graphicElementPosDTO = graphicElementPosMapper.toDto(graphicElementPos);

        restGraphicElementPosMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(graphicElementPosDTO))
            )
            .andExpect(status().isBadRequest());

        List<GraphicElementPos> graphicElementPosList = graphicElementPosRepository.findAll();
        assertThat(graphicElementPosList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllGraphicElementPos() throws Exception {
        // Initialize the database
        graphicElementPosRepository.saveAndFlush(graphicElementPos);

        // Get all the graphicElementPosList
        restGraphicElementPosMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(graphicElementPos.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].x").value(hasItem(DEFAULT_X.doubleValue())))
            .andExpect(jsonPath("$.[*].y").value(hasItem(DEFAULT_Y.doubleValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllGraphicElementPosWithEagerRelationshipsIsEnabled() throws Exception {
        when(graphicElementPosServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restGraphicElementPosMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(graphicElementPosServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllGraphicElementPosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(graphicElementPosServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restGraphicElementPosMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(graphicElementPosServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getGraphicElementPos() throws Exception {
        // Initialize the database
        graphicElementPosRepository.saveAndFlush(graphicElementPos);

        // Get the graphicElementPos
        restGraphicElementPosMockMvc
            .perform(get(ENTITY_API_URL_ID, graphicElementPos.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(graphicElementPos.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.x").value(DEFAULT_X.doubleValue()))
            .andExpect(jsonPath("$.y").value(DEFAULT_Y.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingGraphicElementPos() throws Exception {
        // Get the graphicElementPos
        restGraphicElementPosMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewGraphicElementPos() throws Exception {
        // Initialize the database
        graphicElementPosRepository.saveAndFlush(graphicElementPos);

        int databaseSizeBeforeUpdate = graphicElementPosRepository.findAll().size();

        // Update the graphicElementPos
        GraphicElementPos updatedGraphicElementPos = graphicElementPosRepository.findById(graphicElementPos.getId()).get();
        // Disconnect from session so that the updates on updatedGraphicElementPos are not directly saved in db
        em.detach(updatedGraphicElementPos);
        updatedGraphicElementPos.name(UPDATED_NAME).x(UPDATED_X).y(UPDATED_Y);
        GraphicElementPosDTO graphicElementPosDTO = graphicElementPosMapper.toDto(updatedGraphicElementPos);

        restGraphicElementPosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, graphicElementPosDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(graphicElementPosDTO))
            )
            .andExpect(status().isOk());

        // Validate the GraphicElementPos in the database
        List<GraphicElementPos> graphicElementPosList = graphicElementPosRepository.findAll();
        assertThat(graphicElementPosList).hasSize(databaseSizeBeforeUpdate);
        GraphicElementPos testGraphicElementPos = graphicElementPosList.get(graphicElementPosList.size() - 1);
        assertThat(testGraphicElementPos.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testGraphicElementPos.getX()).isEqualTo(UPDATED_X);
        assertThat(testGraphicElementPos.getY()).isEqualTo(UPDATED_Y);
    }

    @Test
    @Transactional
    void putNonExistingGraphicElementPos() throws Exception {
        int databaseSizeBeforeUpdate = graphicElementPosRepository.findAll().size();
        graphicElementPos.setId(count.incrementAndGet());

        // Create the GraphicElementPos
        GraphicElementPosDTO graphicElementPosDTO = graphicElementPosMapper.toDto(graphicElementPos);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGraphicElementPosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, graphicElementPosDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(graphicElementPosDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GraphicElementPos in the database
        List<GraphicElementPos> graphicElementPosList = graphicElementPosRepository.findAll();
        assertThat(graphicElementPosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGraphicElementPos() throws Exception {
        int databaseSizeBeforeUpdate = graphicElementPosRepository.findAll().size();
        graphicElementPos.setId(count.incrementAndGet());

        // Create the GraphicElementPos
        GraphicElementPosDTO graphicElementPosDTO = graphicElementPosMapper.toDto(graphicElementPos);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGraphicElementPosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(graphicElementPosDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GraphicElementPos in the database
        List<GraphicElementPos> graphicElementPosList = graphicElementPosRepository.findAll();
        assertThat(graphicElementPosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGraphicElementPos() throws Exception {
        int databaseSizeBeforeUpdate = graphicElementPosRepository.findAll().size();
        graphicElementPos.setId(count.incrementAndGet());

        // Create the GraphicElementPos
        GraphicElementPosDTO graphicElementPosDTO = graphicElementPosMapper.toDto(graphicElementPos);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGraphicElementPosMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(graphicElementPosDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the GraphicElementPos in the database
        List<GraphicElementPos> graphicElementPosList = graphicElementPosRepository.findAll();
        assertThat(graphicElementPosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGraphicElementPosWithPatch() throws Exception {
        // Initialize the database
        graphicElementPosRepository.saveAndFlush(graphicElementPos);

        int databaseSizeBeforeUpdate = graphicElementPosRepository.findAll().size();

        // Update the graphicElementPos using partial update
        GraphicElementPos partialUpdatedGraphicElementPos = new GraphicElementPos();
        partialUpdatedGraphicElementPos.setId(graphicElementPos.getId());

        restGraphicElementPosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGraphicElementPos.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGraphicElementPos))
            )
            .andExpect(status().isOk());

        // Validate the GraphicElementPos in the database
        List<GraphicElementPos> graphicElementPosList = graphicElementPosRepository.findAll();
        assertThat(graphicElementPosList).hasSize(databaseSizeBeforeUpdate);
        GraphicElementPos testGraphicElementPos = graphicElementPosList.get(graphicElementPosList.size() - 1);
        assertThat(testGraphicElementPos.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testGraphicElementPos.getX()).isEqualTo(DEFAULT_X);
        assertThat(testGraphicElementPos.getY()).isEqualTo(DEFAULT_Y);
    }

    @Test
    @Transactional
    void fullUpdateGraphicElementPosWithPatch() throws Exception {
        // Initialize the database
        graphicElementPosRepository.saveAndFlush(graphicElementPos);

        int databaseSizeBeforeUpdate = graphicElementPosRepository.findAll().size();

        // Update the graphicElementPos using partial update
        GraphicElementPos partialUpdatedGraphicElementPos = new GraphicElementPos();
        partialUpdatedGraphicElementPos.setId(graphicElementPos.getId());

        partialUpdatedGraphicElementPos.name(UPDATED_NAME).x(UPDATED_X).y(UPDATED_Y);

        restGraphicElementPosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGraphicElementPos.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGraphicElementPos))
            )
            .andExpect(status().isOk());

        // Validate the GraphicElementPos in the database
        List<GraphicElementPos> graphicElementPosList = graphicElementPosRepository.findAll();
        assertThat(graphicElementPosList).hasSize(databaseSizeBeforeUpdate);
        GraphicElementPos testGraphicElementPos = graphicElementPosList.get(graphicElementPosList.size() - 1);
        assertThat(testGraphicElementPos.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testGraphicElementPos.getX()).isEqualTo(UPDATED_X);
        assertThat(testGraphicElementPos.getY()).isEqualTo(UPDATED_Y);
    }

    @Test
    @Transactional
    void patchNonExistingGraphicElementPos() throws Exception {
        int databaseSizeBeforeUpdate = graphicElementPosRepository.findAll().size();
        graphicElementPos.setId(count.incrementAndGet());

        // Create the GraphicElementPos
        GraphicElementPosDTO graphicElementPosDTO = graphicElementPosMapper.toDto(graphicElementPos);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGraphicElementPosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, graphicElementPosDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(graphicElementPosDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GraphicElementPos in the database
        List<GraphicElementPos> graphicElementPosList = graphicElementPosRepository.findAll();
        assertThat(graphicElementPosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGraphicElementPos() throws Exception {
        int databaseSizeBeforeUpdate = graphicElementPosRepository.findAll().size();
        graphicElementPos.setId(count.incrementAndGet());

        // Create the GraphicElementPos
        GraphicElementPosDTO graphicElementPosDTO = graphicElementPosMapper.toDto(graphicElementPos);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGraphicElementPosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(graphicElementPosDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GraphicElementPos in the database
        List<GraphicElementPos> graphicElementPosList = graphicElementPosRepository.findAll();
        assertThat(graphicElementPosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGraphicElementPos() throws Exception {
        int databaseSizeBeforeUpdate = graphicElementPosRepository.findAll().size();
        graphicElementPos.setId(count.incrementAndGet());

        // Create the GraphicElementPos
        GraphicElementPosDTO graphicElementPosDTO = graphicElementPosMapper.toDto(graphicElementPos);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGraphicElementPosMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(graphicElementPosDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the GraphicElementPos in the database
        List<GraphicElementPos> graphicElementPosList = graphicElementPosRepository.findAll();
        assertThat(graphicElementPosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGraphicElementPos() throws Exception {
        // Initialize the database
        graphicElementPosRepository.saveAndFlush(graphicElementPos);

        int databaseSizeBeforeDelete = graphicElementPosRepository.findAll().size();

        // Delete the graphicElementPos
        restGraphicElementPosMockMvc
            .perform(delete(ENTITY_API_URL_ID, graphicElementPos.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<GraphicElementPos> graphicElementPosList = graphicElementPosRepository.findAll();
        assertThat(graphicElementPosList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
