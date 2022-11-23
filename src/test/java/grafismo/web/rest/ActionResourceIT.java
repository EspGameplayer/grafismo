package grafismo.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import grafismo.IntegrationTest;
import grafismo.domain.Action;
import grafismo.domain.enumeration.ActionType;
import grafismo.repository.ActionRepository;
import grafismo.service.ActionService;
import grafismo.service.dto.ActionDTO;
import grafismo.service.mapper.ActionMapper;
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
 * Integration tests for the {@link ActionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ActionResourceIT {

    private static final Integer DEFAULT_MINUTE = 0;
    private static final Integer UPDATED_MINUTE = 1;

    private static final Integer DEFAULT_SECOND = 0;
    private static final Integer UPDATED_SECOND = 1;

    private static final Integer DEFAULT_PERIOD = 1;
    private static final Integer UPDATED_PERIOD = 2;

    private static final ActionType DEFAULT_TYPE = ActionType.GOAL;
    private static final ActionType UPDATED_TYPE = ActionType.YC;

    private static final Integer DEFAULT_STATUS = 0;
    private static final Integer UPDATED_STATUS = 1;

    private static final String ENTITY_API_URL = "/api/actions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ActionRepository actionRepository;

    @Mock
    private ActionRepository actionRepositoryMock;

    @Autowired
    private ActionMapper actionMapper;

    @Mock
    private ActionService actionServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restActionMockMvc;

    private Action action;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Action createEntity(EntityManager em) {
        Action action = new Action()
            .minute(DEFAULT_MINUTE)
            .second(DEFAULT_SECOND)
            .period(DEFAULT_PERIOD)
            .type(DEFAULT_TYPE)
            .status(DEFAULT_STATUS);
        return action;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Action createUpdatedEntity(EntityManager em) {
        Action action = new Action()
            .minute(UPDATED_MINUTE)
            .second(UPDATED_SECOND)
            .period(UPDATED_PERIOD)
            .type(UPDATED_TYPE)
            .status(UPDATED_STATUS);
        return action;
    }

    @BeforeEach
    public void initTest() {
        action = createEntity(em);
    }

    @Test
    @Transactional
    void createAction() throws Exception {
        int databaseSizeBeforeCreate = actionRepository.findAll().size();
        // Create the Action
        ActionDTO actionDTO = actionMapper.toDto(action);
        restActionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(actionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Action in the database
        List<Action> actionList = actionRepository.findAll();
        assertThat(actionList).hasSize(databaseSizeBeforeCreate + 1);
        Action testAction = actionList.get(actionList.size() - 1);
        assertThat(testAction.getMinute()).isEqualTo(DEFAULT_MINUTE);
        assertThat(testAction.getSecond()).isEqualTo(DEFAULT_SECOND);
        assertThat(testAction.getPeriod()).isEqualTo(DEFAULT_PERIOD);
        assertThat(testAction.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testAction.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void createActionWithExistingId() throws Exception {
        // Create the Action with an existing ID
        action.setId(1L);
        ActionDTO actionDTO = actionMapper.toDto(action);

        int databaseSizeBeforeCreate = actionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restActionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(actionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Action in the database
        List<Action> actionList = actionRepository.findAll();
        assertThat(actionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = actionRepository.findAll().size();
        // set the field null
        action.setType(null);

        // Create the Action, which fails.
        ActionDTO actionDTO = actionMapper.toDto(action);

        restActionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(actionDTO))
            )
            .andExpect(status().isBadRequest());

        List<Action> actionList = actionRepository.findAll();
        assertThat(actionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllActions() throws Exception {
        // Initialize the database
        actionRepository.saveAndFlush(action);

        // Get all the actionList
        restActionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(action.getId().intValue())))
            .andExpect(jsonPath("$.[*].minute").value(hasItem(DEFAULT_MINUTE)))
            .andExpect(jsonPath("$.[*].second").value(hasItem(DEFAULT_SECOND)))
            .andExpect(jsonPath("$.[*].period").value(hasItem(DEFAULT_PERIOD)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllActionsWithEagerRelationshipsIsEnabled() throws Exception {
        when(actionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restActionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(actionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllActionsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(actionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restActionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(actionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getAction() throws Exception {
        // Initialize the database
        actionRepository.saveAndFlush(action);

        // Get the action
        restActionMockMvc
            .perform(get(ENTITY_API_URL_ID, action.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(action.getId().intValue()))
            .andExpect(jsonPath("$.minute").value(DEFAULT_MINUTE))
            .andExpect(jsonPath("$.second").value(DEFAULT_SECOND))
            .andExpect(jsonPath("$.period").value(DEFAULT_PERIOD))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS));
    }

    @Test
    @Transactional
    void getNonExistingAction() throws Exception {
        // Get the action
        restActionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAction() throws Exception {
        // Initialize the database
        actionRepository.saveAndFlush(action);

        int databaseSizeBeforeUpdate = actionRepository.findAll().size();

        // Update the action
        Action updatedAction = actionRepository.findById(action.getId()).get();
        // Disconnect from session so that the updates on updatedAction are not directly saved in db
        em.detach(updatedAction);
        updatedAction.minute(UPDATED_MINUTE).second(UPDATED_SECOND).period(UPDATED_PERIOD).type(UPDATED_TYPE).status(UPDATED_STATUS);
        ActionDTO actionDTO = actionMapper.toDto(updatedAction);

        restActionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, actionDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(actionDTO))
            )
            .andExpect(status().isOk());

        // Validate the Action in the database
        List<Action> actionList = actionRepository.findAll();
        assertThat(actionList).hasSize(databaseSizeBeforeUpdate);
        Action testAction = actionList.get(actionList.size() - 1);
        assertThat(testAction.getMinute()).isEqualTo(UPDATED_MINUTE);
        assertThat(testAction.getSecond()).isEqualTo(UPDATED_SECOND);
        assertThat(testAction.getPeriod()).isEqualTo(UPDATED_PERIOD);
        assertThat(testAction.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testAction.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingAction() throws Exception {
        int databaseSizeBeforeUpdate = actionRepository.findAll().size();
        action.setId(count.incrementAndGet());

        // Create the Action
        ActionDTO actionDTO = actionMapper.toDto(action);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restActionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, actionDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(actionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Action in the database
        List<Action> actionList = actionRepository.findAll();
        assertThat(actionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAction() throws Exception {
        int databaseSizeBeforeUpdate = actionRepository.findAll().size();
        action.setId(count.incrementAndGet());

        // Create the Action
        ActionDTO actionDTO = actionMapper.toDto(action);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(actionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Action in the database
        List<Action> actionList = actionRepository.findAll();
        assertThat(actionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAction() throws Exception {
        int databaseSizeBeforeUpdate = actionRepository.findAll().size();
        action.setId(count.incrementAndGet());

        // Create the Action
        ActionDTO actionDTO = actionMapper.toDto(action);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActionMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(actionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Action in the database
        List<Action> actionList = actionRepository.findAll();
        assertThat(actionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateActionWithPatch() throws Exception {
        // Initialize the database
        actionRepository.saveAndFlush(action);

        int databaseSizeBeforeUpdate = actionRepository.findAll().size();

        // Update the action using partial update
        Action partialUpdatedAction = new Action();
        partialUpdatedAction.setId(action.getId());

        partialUpdatedAction.minute(UPDATED_MINUTE).period(UPDATED_PERIOD);

        restActionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAction.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAction))
            )
            .andExpect(status().isOk());

        // Validate the Action in the database
        List<Action> actionList = actionRepository.findAll();
        assertThat(actionList).hasSize(databaseSizeBeforeUpdate);
        Action testAction = actionList.get(actionList.size() - 1);
        assertThat(testAction.getMinute()).isEqualTo(UPDATED_MINUTE);
        assertThat(testAction.getSecond()).isEqualTo(DEFAULT_SECOND);
        assertThat(testAction.getPeriod()).isEqualTo(UPDATED_PERIOD);
        assertThat(testAction.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testAction.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateActionWithPatch() throws Exception {
        // Initialize the database
        actionRepository.saveAndFlush(action);

        int databaseSizeBeforeUpdate = actionRepository.findAll().size();

        // Update the action using partial update
        Action partialUpdatedAction = new Action();
        partialUpdatedAction.setId(action.getId());

        partialUpdatedAction.minute(UPDATED_MINUTE).second(UPDATED_SECOND).period(UPDATED_PERIOD).type(UPDATED_TYPE).status(UPDATED_STATUS);

        restActionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAction.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAction))
            )
            .andExpect(status().isOk());

        // Validate the Action in the database
        List<Action> actionList = actionRepository.findAll();
        assertThat(actionList).hasSize(databaseSizeBeforeUpdate);
        Action testAction = actionList.get(actionList.size() - 1);
        assertThat(testAction.getMinute()).isEqualTo(UPDATED_MINUTE);
        assertThat(testAction.getSecond()).isEqualTo(UPDATED_SECOND);
        assertThat(testAction.getPeriod()).isEqualTo(UPDATED_PERIOD);
        assertThat(testAction.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testAction.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingAction() throws Exception {
        int databaseSizeBeforeUpdate = actionRepository.findAll().size();
        action.setId(count.incrementAndGet());

        // Create the Action
        ActionDTO actionDTO = actionMapper.toDto(action);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restActionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, actionDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(actionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Action in the database
        List<Action> actionList = actionRepository.findAll();
        assertThat(actionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAction() throws Exception {
        int databaseSizeBeforeUpdate = actionRepository.findAll().size();
        action.setId(count.incrementAndGet());

        // Create the Action
        ActionDTO actionDTO = actionMapper.toDto(action);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(actionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Action in the database
        List<Action> actionList = actionRepository.findAll();
        assertThat(actionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAction() throws Exception {
        int databaseSizeBeforeUpdate = actionRepository.findAll().size();
        action.setId(count.incrementAndGet());

        // Create the Action
        ActionDTO actionDTO = actionMapper.toDto(action);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(actionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Action in the database
        List<Action> actionList = actionRepository.findAll();
        assertThat(actionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAction() throws Exception {
        // Initialize the database
        actionRepository.saveAndFlush(action);

        int databaseSizeBeforeDelete = actionRepository.findAll().size();

        // Delete the action
        restActionMockMvc
            .perform(delete(ENTITY_API_URL_ID, action.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Action> actionList = actionRepository.findAll();
        assertThat(actionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
