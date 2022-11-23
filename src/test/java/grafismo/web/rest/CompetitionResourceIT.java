package grafismo.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import grafismo.IntegrationTest;
import grafismo.domain.Competition;
import grafismo.domain.enumeration.CompetitionType;
import grafismo.repository.CompetitionRepository;
import grafismo.service.CompetitionService;
import grafismo.service.dto.CompetitionDTO;
import grafismo.service.mapper.CompetitionMapper;
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
 * Integration tests for the {@link CompetitionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CompetitionResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_GRAPHICS_NAME = "AAAAAAAAAA";
    private static final String UPDATED_GRAPHICS_NAME = "BBBBBBBBBB";

    private static final CompetitionType DEFAULT_TYPE = CompetitionType.LEAGUE;
    private static final CompetitionType UPDATED_TYPE = CompetitionType.CUP;

    private static final String DEFAULT_COLOUR = "";
    private static final String UPDATED_COLOUR = "B";

    private static final Integer DEFAULT_SUSPENSION_YC_MATCHES = 0;
    private static final Integer UPDATED_SUSPENSION_YC_MATCHES = 1;

    private static final String ENTITY_API_URL = "/api/competitions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CompetitionRepository competitionRepository;

    @Mock
    private CompetitionRepository competitionRepositoryMock;

    @Autowired
    private CompetitionMapper competitionMapper;

    @Mock
    private CompetitionService competitionServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCompetitionMockMvc;

    private Competition competition;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Competition createEntity(EntityManager em) {
        Competition competition = new Competition()
            .name(DEFAULT_NAME)
            .graphicsName(DEFAULT_GRAPHICS_NAME)
            .type(DEFAULT_TYPE)
            .colour(DEFAULT_COLOUR)
            .suspensionYcMatches(DEFAULT_SUSPENSION_YC_MATCHES);
        return competition;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Competition createUpdatedEntity(EntityManager em) {
        Competition competition = new Competition()
            .name(UPDATED_NAME)
            .graphicsName(UPDATED_GRAPHICS_NAME)
            .type(UPDATED_TYPE)
            .colour(UPDATED_COLOUR)
            .suspensionYcMatches(UPDATED_SUSPENSION_YC_MATCHES);
        return competition;
    }

    @BeforeEach
    public void initTest() {
        competition = createEntity(em);
    }

    @Test
    @Transactional
    void createCompetition() throws Exception {
        int databaseSizeBeforeCreate = competitionRepository.findAll().size();
        // Create the Competition
        CompetitionDTO competitionDTO = competitionMapper.toDto(competition);
        restCompetitionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(competitionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Competition in the database
        List<Competition> competitionList = competitionRepository.findAll();
        assertThat(competitionList).hasSize(databaseSizeBeforeCreate + 1);
        Competition testCompetition = competitionList.get(competitionList.size() - 1);
        assertThat(testCompetition.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCompetition.getGraphicsName()).isEqualTo(DEFAULT_GRAPHICS_NAME);
        assertThat(testCompetition.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testCompetition.getColour()).isEqualTo(DEFAULT_COLOUR);
        assertThat(testCompetition.getSuspensionYcMatches()).isEqualTo(DEFAULT_SUSPENSION_YC_MATCHES);
    }

    @Test
    @Transactional
    void createCompetitionWithExistingId() throws Exception {
        // Create the Competition with an existing ID
        competition.setId(1L);
        CompetitionDTO competitionDTO = competitionMapper.toDto(competition);

        int databaseSizeBeforeCreate = competitionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCompetitionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(competitionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Competition in the database
        List<Competition> competitionList = competitionRepository.findAll();
        assertThat(competitionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = competitionRepository.findAll().size();
        // set the field null
        competition.setName(null);

        // Create the Competition, which fails.
        CompetitionDTO competitionDTO = competitionMapper.toDto(competition);

        restCompetitionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(competitionDTO))
            )
            .andExpect(status().isBadRequest());

        List<Competition> competitionList = competitionRepository.findAll();
        assertThat(competitionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkGraphicsNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = competitionRepository.findAll().size();
        // set the field null
        competition.setGraphicsName(null);

        // Create the Competition, which fails.
        CompetitionDTO competitionDTO = competitionMapper.toDto(competition);

        restCompetitionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(competitionDTO))
            )
            .andExpect(status().isBadRequest());

        List<Competition> competitionList = competitionRepository.findAll();
        assertThat(competitionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = competitionRepository.findAll().size();
        // set the field null
        competition.setType(null);

        // Create the Competition, which fails.
        CompetitionDTO competitionDTO = competitionMapper.toDto(competition);

        restCompetitionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(competitionDTO))
            )
            .andExpect(status().isBadRequest());

        List<Competition> competitionList = competitionRepository.findAll();
        assertThat(competitionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCompetitions() throws Exception {
        // Initialize the database
        competitionRepository.saveAndFlush(competition);

        // Get all the competitionList
        restCompetitionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(competition.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].graphicsName").value(hasItem(DEFAULT_GRAPHICS_NAME)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].colour").value(hasItem(DEFAULT_COLOUR)))
            .andExpect(jsonPath("$.[*].suspensionYcMatches").value(hasItem(DEFAULT_SUSPENSION_YC_MATCHES)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCompetitionsWithEagerRelationshipsIsEnabled() throws Exception {
        when(competitionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCompetitionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(competitionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCompetitionsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(competitionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCompetitionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(competitionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getCompetition() throws Exception {
        // Initialize the database
        competitionRepository.saveAndFlush(competition);

        // Get the competition
        restCompetitionMockMvc
            .perform(get(ENTITY_API_URL_ID, competition.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(competition.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.graphicsName").value(DEFAULT_GRAPHICS_NAME))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.colour").value(DEFAULT_COLOUR))
            .andExpect(jsonPath("$.suspensionYcMatches").value(DEFAULT_SUSPENSION_YC_MATCHES));
    }

    @Test
    @Transactional
    void getNonExistingCompetition() throws Exception {
        // Get the competition
        restCompetitionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCompetition() throws Exception {
        // Initialize the database
        competitionRepository.saveAndFlush(competition);

        int databaseSizeBeforeUpdate = competitionRepository.findAll().size();

        // Update the competition
        Competition updatedCompetition = competitionRepository.findById(competition.getId()).get();
        // Disconnect from session so that the updates on updatedCompetition are not directly saved in db
        em.detach(updatedCompetition);
        updatedCompetition
            .name(UPDATED_NAME)
            .graphicsName(UPDATED_GRAPHICS_NAME)
            .type(UPDATED_TYPE)
            .colour(UPDATED_COLOUR)
            .suspensionYcMatches(UPDATED_SUSPENSION_YC_MATCHES);
        CompetitionDTO competitionDTO = competitionMapper.toDto(updatedCompetition);

        restCompetitionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, competitionDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(competitionDTO))
            )
            .andExpect(status().isOk());

        // Validate the Competition in the database
        List<Competition> competitionList = competitionRepository.findAll();
        assertThat(competitionList).hasSize(databaseSizeBeforeUpdate);
        Competition testCompetition = competitionList.get(competitionList.size() - 1);
        assertThat(testCompetition.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCompetition.getGraphicsName()).isEqualTo(UPDATED_GRAPHICS_NAME);
        assertThat(testCompetition.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testCompetition.getColour()).isEqualTo(UPDATED_COLOUR);
        assertThat(testCompetition.getSuspensionYcMatches()).isEqualTo(UPDATED_SUSPENSION_YC_MATCHES);
    }

    @Test
    @Transactional
    void putNonExistingCompetition() throws Exception {
        int databaseSizeBeforeUpdate = competitionRepository.findAll().size();
        competition.setId(count.incrementAndGet());

        // Create the Competition
        CompetitionDTO competitionDTO = competitionMapper.toDto(competition);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompetitionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, competitionDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(competitionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Competition in the database
        List<Competition> competitionList = competitionRepository.findAll();
        assertThat(competitionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCompetition() throws Exception {
        int databaseSizeBeforeUpdate = competitionRepository.findAll().size();
        competition.setId(count.incrementAndGet());

        // Create the Competition
        CompetitionDTO competitionDTO = competitionMapper.toDto(competition);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompetitionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(competitionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Competition in the database
        List<Competition> competitionList = competitionRepository.findAll();
        assertThat(competitionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCompetition() throws Exception {
        int databaseSizeBeforeUpdate = competitionRepository.findAll().size();
        competition.setId(count.incrementAndGet());

        // Create the Competition
        CompetitionDTO competitionDTO = competitionMapper.toDto(competition);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompetitionMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(competitionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Competition in the database
        List<Competition> competitionList = competitionRepository.findAll();
        assertThat(competitionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCompetitionWithPatch() throws Exception {
        // Initialize the database
        competitionRepository.saveAndFlush(competition);

        int databaseSizeBeforeUpdate = competitionRepository.findAll().size();

        // Update the competition using partial update
        Competition partialUpdatedCompetition = new Competition();
        partialUpdatedCompetition.setId(competition.getId());

        partialUpdatedCompetition.name(UPDATED_NAME).type(UPDATED_TYPE).suspensionYcMatches(UPDATED_SUSPENSION_YC_MATCHES);

        restCompetitionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompetition.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCompetition))
            )
            .andExpect(status().isOk());

        // Validate the Competition in the database
        List<Competition> competitionList = competitionRepository.findAll();
        assertThat(competitionList).hasSize(databaseSizeBeforeUpdate);
        Competition testCompetition = competitionList.get(competitionList.size() - 1);
        assertThat(testCompetition.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCompetition.getGraphicsName()).isEqualTo(DEFAULT_GRAPHICS_NAME);
        assertThat(testCompetition.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testCompetition.getColour()).isEqualTo(DEFAULT_COLOUR);
        assertThat(testCompetition.getSuspensionYcMatches()).isEqualTo(UPDATED_SUSPENSION_YC_MATCHES);
    }

    @Test
    @Transactional
    void fullUpdateCompetitionWithPatch() throws Exception {
        // Initialize the database
        competitionRepository.saveAndFlush(competition);

        int databaseSizeBeforeUpdate = competitionRepository.findAll().size();

        // Update the competition using partial update
        Competition partialUpdatedCompetition = new Competition();
        partialUpdatedCompetition.setId(competition.getId());

        partialUpdatedCompetition
            .name(UPDATED_NAME)
            .graphicsName(UPDATED_GRAPHICS_NAME)
            .type(UPDATED_TYPE)
            .colour(UPDATED_COLOUR)
            .suspensionYcMatches(UPDATED_SUSPENSION_YC_MATCHES);

        restCompetitionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompetition.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCompetition))
            )
            .andExpect(status().isOk());

        // Validate the Competition in the database
        List<Competition> competitionList = competitionRepository.findAll();
        assertThat(competitionList).hasSize(databaseSizeBeforeUpdate);
        Competition testCompetition = competitionList.get(competitionList.size() - 1);
        assertThat(testCompetition.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCompetition.getGraphicsName()).isEqualTo(UPDATED_GRAPHICS_NAME);
        assertThat(testCompetition.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testCompetition.getColour()).isEqualTo(UPDATED_COLOUR);
        assertThat(testCompetition.getSuspensionYcMatches()).isEqualTo(UPDATED_SUSPENSION_YC_MATCHES);
    }

    @Test
    @Transactional
    void patchNonExistingCompetition() throws Exception {
        int databaseSizeBeforeUpdate = competitionRepository.findAll().size();
        competition.setId(count.incrementAndGet());

        // Create the Competition
        CompetitionDTO competitionDTO = competitionMapper.toDto(competition);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompetitionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, competitionDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(competitionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Competition in the database
        List<Competition> competitionList = competitionRepository.findAll();
        assertThat(competitionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCompetition() throws Exception {
        int databaseSizeBeforeUpdate = competitionRepository.findAll().size();
        competition.setId(count.incrementAndGet());

        // Create the Competition
        CompetitionDTO competitionDTO = competitionMapper.toDto(competition);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompetitionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(competitionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Competition in the database
        List<Competition> competitionList = competitionRepository.findAll();
        assertThat(competitionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCompetition() throws Exception {
        int databaseSizeBeforeUpdate = competitionRepository.findAll().size();
        competition.setId(count.incrementAndGet());

        // Create the Competition
        CompetitionDTO competitionDTO = competitionMapper.toDto(competition);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompetitionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(competitionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Competition in the database
        List<Competition> competitionList = competitionRepository.findAll();
        assertThat(competitionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCompetition() throws Exception {
        // Initialize the database
        competitionRepository.saveAndFlush(competition);

        int databaseSizeBeforeDelete = competitionRepository.findAll().size();

        // Delete the competition
        restCompetitionMockMvc
            .perform(delete(ENTITY_API_URL_ID, competition.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Competition> competitionList = competitionRepository.findAll();
        assertThat(competitionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
