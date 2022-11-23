package grafismo.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import grafismo.IntegrationTest;
import grafismo.domain.ActionKey;
import grafismo.repository.ActionKeyRepository;
import grafismo.service.dto.ActionKeyDTO;
import grafismo.service.mapper.ActionKeyMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ActionKeyResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ActionKeyResourceIT {

    private static final String DEFAULT_ACTION = "AAAAAAAAAA";
    private static final String UPDATED_ACTION = "BBBBBBBBBB";

    private static final String DEFAULT_KEYS = "AAAAAAAAAA";
    private static final String UPDATED_KEYS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/action-keys";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ActionKeyRepository actionKeyRepository;

    @Autowired
    private ActionKeyMapper actionKeyMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restActionKeyMockMvc;

    private ActionKey actionKey;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ActionKey createEntity(EntityManager em) {
        ActionKey actionKey = new ActionKey().action(DEFAULT_ACTION).keys(DEFAULT_KEYS);
        return actionKey;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ActionKey createUpdatedEntity(EntityManager em) {
        ActionKey actionKey = new ActionKey().action(UPDATED_ACTION).keys(UPDATED_KEYS);
        return actionKey;
    }

    @BeforeEach
    public void initTest() {
        actionKey = createEntity(em);
    }

    @Test
    @Transactional
    void createActionKey() throws Exception {
        int databaseSizeBeforeCreate = actionKeyRepository.findAll().size();
        // Create the ActionKey
        ActionKeyDTO actionKeyDTO = actionKeyMapper.toDto(actionKey);
        restActionKeyMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(actionKeyDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ActionKey in the database
        List<ActionKey> actionKeyList = actionKeyRepository.findAll();
        assertThat(actionKeyList).hasSize(databaseSizeBeforeCreate + 1);
        ActionKey testActionKey = actionKeyList.get(actionKeyList.size() - 1);
        assertThat(testActionKey.getAction()).isEqualTo(DEFAULT_ACTION);
        assertThat(testActionKey.getKeys()).isEqualTo(DEFAULT_KEYS);
    }

    @Test
    @Transactional
    void createActionKeyWithExistingId() throws Exception {
        // Create the ActionKey with an existing ID
        actionKey.setId(1L);
        ActionKeyDTO actionKeyDTO = actionKeyMapper.toDto(actionKey);

        int databaseSizeBeforeCreate = actionKeyRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restActionKeyMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(actionKeyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ActionKey in the database
        List<ActionKey> actionKeyList = actionKeyRepository.findAll();
        assertThat(actionKeyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllActionKeys() throws Exception {
        // Initialize the database
        actionKeyRepository.saveAndFlush(actionKey);

        // Get all the actionKeyList
        restActionKeyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(actionKey.getId().intValue())))
            .andExpect(jsonPath("$.[*].action").value(hasItem(DEFAULT_ACTION)))
            .andExpect(jsonPath("$.[*].keys").value(hasItem(DEFAULT_KEYS)));
    }

    @Test
    @Transactional
    void getActionKey() throws Exception {
        // Initialize the database
        actionKeyRepository.saveAndFlush(actionKey);

        // Get the actionKey
        restActionKeyMockMvc
            .perform(get(ENTITY_API_URL_ID, actionKey.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(actionKey.getId().intValue()))
            .andExpect(jsonPath("$.action").value(DEFAULT_ACTION))
            .andExpect(jsonPath("$.keys").value(DEFAULT_KEYS));
    }

    @Test
    @Transactional
    void getNonExistingActionKey() throws Exception {
        // Get the actionKey
        restActionKeyMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewActionKey() throws Exception {
        // Initialize the database
        actionKeyRepository.saveAndFlush(actionKey);

        int databaseSizeBeforeUpdate = actionKeyRepository.findAll().size();

        // Update the actionKey
        ActionKey updatedActionKey = actionKeyRepository.findById(actionKey.getId()).get();
        // Disconnect from session so that the updates on updatedActionKey are not directly saved in db
        em.detach(updatedActionKey);
        updatedActionKey.action(UPDATED_ACTION).keys(UPDATED_KEYS);
        ActionKeyDTO actionKeyDTO = actionKeyMapper.toDto(updatedActionKey);

        restActionKeyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, actionKeyDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(actionKeyDTO))
            )
            .andExpect(status().isOk());

        // Validate the ActionKey in the database
        List<ActionKey> actionKeyList = actionKeyRepository.findAll();
        assertThat(actionKeyList).hasSize(databaseSizeBeforeUpdate);
        ActionKey testActionKey = actionKeyList.get(actionKeyList.size() - 1);
        assertThat(testActionKey.getAction()).isEqualTo(UPDATED_ACTION);
        assertThat(testActionKey.getKeys()).isEqualTo(UPDATED_KEYS);
    }

    @Test
    @Transactional
    void putNonExistingActionKey() throws Exception {
        int databaseSizeBeforeUpdate = actionKeyRepository.findAll().size();
        actionKey.setId(count.incrementAndGet());

        // Create the ActionKey
        ActionKeyDTO actionKeyDTO = actionKeyMapper.toDto(actionKey);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restActionKeyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, actionKeyDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(actionKeyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ActionKey in the database
        List<ActionKey> actionKeyList = actionKeyRepository.findAll();
        assertThat(actionKeyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchActionKey() throws Exception {
        int databaseSizeBeforeUpdate = actionKeyRepository.findAll().size();
        actionKey.setId(count.incrementAndGet());

        // Create the ActionKey
        ActionKeyDTO actionKeyDTO = actionKeyMapper.toDto(actionKey);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActionKeyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(actionKeyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ActionKey in the database
        List<ActionKey> actionKeyList = actionKeyRepository.findAll();
        assertThat(actionKeyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamActionKey() throws Exception {
        int databaseSizeBeforeUpdate = actionKeyRepository.findAll().size();
        actionKey.setId(count.incrementAndGet());

        // Create the ActionKey
        ActionKeyDTO actionKeyDTO = actionKeyMapper.toDto(actionKey);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActionKeyMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(actionKeyDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ActionKey in the database
        List<ActionKey> actionKeyList = actionKeyRepository.findAll();
        assertThat(actionKeyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateActionKeyWithPatch() throws Exception {
        // Initialize the database
        actionKeyRepository.saveAndFlush(actionKey);

        int databaseSizeBeforeUpdate = actionKeyRepository.findAll().size();

        // Update the actionKey using partial update
        ActionKey partialUpdatedActionKey = new ActionKey();
        partialUpdatedActionKey.setId(actionKey.getId());

        restActionKeyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedActionKey.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedActionKey))
            )
            .andExpect(status().isOk());

        // Validate the ActionKey in the database
        List<ActionKey> actionKeyList = actionKeyRepository.findAll();
        assertThat(actionKeyList).hasSize(databaseSizeBeforeUpdate);
        ActionKey testActionKey = actionKeyList.get(actionKeyList.size() - 1);
        assertThat(testActionKey.getAction()).isEqualTo(DEFAULT_ACTION);
        assertThat(testActionKey.getKeys()).isEqualTo(DEFAULT_KEYS);
    }

    @Test
    @Transactional
    void fullUpdateActionKeyWithPatch() throws Exception {
        // Initialize the database
        actionKeyRepository.saveAndFlush(actionKey);

        int databaseSizeBeforeUpdate = actionKeyRepository.findAll().size();

        // Update the actionKey using partial update
        ActionKey partialUpdatedActionKey = new ActionKey();
        partialUpdatedActionKey.setId(actionKey.getId());

        partialUpdatedActionKey.action(UPDATED_ACTION).keys(UPDATED_KEYS);

        restActionKeyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedActionKey.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedActionKey))
            )
            .andExpect(status().isOk());

        // Validate the ActionKey in the database
        List<ActionKey> actionKeyList = actionKeyRepository.findAll();
        assertThat(actionKeyList).hasSize(databaseSizeBeforeUpdate);
        ActionKey testActionKey = actionKeyList.get(actionKeyList.size() - 1);
        assertThat(testActionKey.getAction()).isEqualTo(UPDATED_ACTION);
        assertThat(testActionKey.getKeys()).isEqualTo(UPDATED_KEYS);
    }

    @Test
    @Transactional
    void patchNonExistingActionKey() throws Exception {
        int databaseSizeBeforeUpdate = actionKeyRepository.findAll().size();
        actionKey.setId(count.incrementAndGet());

        // Create the ActionKey
        ActionKeyDTO actionKeyDTO = actionKeyMapper.toDto(actionKey);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restActionKeyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, actionKeyDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(actionKeyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ActionKey in the database
        List<ActionKey> actionKeyList = actionKeyRepository.findAll();
        assertThat(actionKeyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchActionKey() throws Exception {
        int databaseSizeBeforeUpdate = actionKeyRepository.findAll().size();
        actionKey.setId(count.incrementAndGet());

        // Create the ActionKey
        ActionKeyDTO actionKeyDTO = actionKeyMapper.toDto(actionKey);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActionKeyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(actionKeyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ActionKey in the database
        List<ActionKey> actionKeyList = actionKeyRepository.findAll();
        assertThat(actionKeyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamActionKey() throws Exception {
        int databaseSizeBeforeUpdate = actionKeyRepository.findAll().size();
        actionKey.setId(count.incrementAndGet());

        // Create the ActionKey
        ActionKeyDTO actionKeyDTO = actionKeyMapper.toDto(actionKey);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActionKeyMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(actionKeyDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ActionKey in the database
        List<ActionKey> actionKeyList = actionKeyRepository.findAll();
        assertThat(actionKeyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteActionKey() throws Exception {
        // Initialize the database
        actionKeyRepository.saveAndFlush(actionKey);

        int databaseSizeBeforeDelete = actionKeyRepository.findAll().size();

        // Delete the actionKey
        restActionKeyMockMvc
            .perform(delete(ENTITY_API_URL_ID, actionKey.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ActionKey> actionKeyList = actionKeyRepository.findAll();
        assertThat(actionKeyList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
