package grafismo.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import grafismo.IntegrationTest;
import grafismo.domain.Shirt;
import grafismo.repository.ShirtRepository;
import grafismo.service.ShirtService;
import grafismo.service.dto.ShirtDTO;
import grafismo.service.mapper.ShirtMapper;
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
 * Integration tests for the {@link ShirtResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ShirtResourceIT {

    private static final String DEFAULT_COLOUR_1 = "9a4b14";
    private static final String UPDATED_COLOUR_1 = "";

    private static final String DEFAULT_COLOUR_2 = "";
    private static final String UPDATED_COLOUR_2 = "e07e00";

    private static final Integer DEFAULT_TYPE = 1;
    private static final Integer UPDATED_TYPE = 2;

    private static final String ENTITY_API_URL = "/api/shirts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ShirtRepository shirtRepository;

    @Mock
    private ShirtRepository shirtRepositoryMock;

    @Autowired
    private ShirtMapper shirtMapper;

    @Mock
    private ShirtService shirtServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restShirtMockMvc;

    private Shirt shirt;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Shirt createEntity(EntityManager em) {
        Shirt shirt = new Shirt().colour1(DEFAULT_COLOUR_1).colour2(DEFAULT_COLOUR_2).type(DEFAULT_TYPE);
        return shirt;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Shirt createUpdatedEntity(EntityManager em) {
        Shirt shirt = new Shirt().colour1(UPDATED_COLOUR_1).colour2(UPDATED_COLOUR_2).type(UPDATED_TYPE);
        return shirt;
    }

    @BeforeEach
    public void initTest() {
        shirt = createEntity(em);
    }

    @Test
    @Transactional
    void createShirt() throws Exception {
        int databaseSizeBeforeCreate = shirtRepository.findAll().size();
        // Create the Shirt
        ShirtDTO shirtDTO = shirtMapper.toDto(shirt);
        restShirtMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(shirtDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Shirt in the database
        List<Shirt> shirtList = shirtRepository.findAll();
        assertThat(shirtList).hasSize(databaseSizeBeforeCreate + 1);
        Shirt testShirt = shirtList.get(shirtList.size() - 1);
        assertThat(testShirt.getColour1()).isEqualTo(DEFAULT_COLOUR_1);
        assertThat(testShirt.getColour2()).isEqualTo(DEFAULT_COLOUR_2);
        assertThat(testShirt.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    void createShirtWithExistingId() throws Exception {
        // Create the Shirt with an existing ID
        shirt.setId(1L);
        ShirtDTO shirtDTO = shirtMapper.toDto(shirt);

        int databaseSizeBeforeCreate = shirtRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restShirtMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(shirtDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Shirt in the database
        List<Shirt> shirtList = shirtRepository.findAll();
        assertThat(shirtList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllShirts() throws Exception {
        // Initialize the database
        shirtRepository.saveAndFlush(shirt);

        // Get all the shirtList
        restShirtMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shirt.getId().intValue())))
            .andExpect(jsonPath("$.[*].colour1").value(hasItem(DEFAULT_COLOUR_1)))
            .andExpect(jsonPath("$.[*].colour2").value(hasItem(DEFAULT_COLOUR_2)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllShirtsWithEagerRelationshipsIsEnabled() throws Exception {
        when(shirtServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restShirtMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(shirtServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllShirtsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(shirtServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restShirtMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(shirtServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getShirt() throws Exception {
        // Initialize the database
        shirtRepository.saveAndFlush(shirt);

        // Get the shirt
        restShirtMockMvc
            .perform(get(ENTITY_API_URL_ID, shirt.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(shirt.getId().intValue()))
            .andExpect(jsonPath("$.colour1").value(DEFAULT_COLOUR_1))
            .andExpect(jsonPath("$.colour2").value(DEFAULT_COLOUR_2))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE));
    }

    @Test
    @Transactional
    void getNonExistingShirt() throws Exception {
        // Get the shirt
        restShirtMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewShirt() throws Exception {
        // Initialize the database
        shirtRepository.saveAndFlush(shirt);

        int databaseSizeBeforeUpdate = shirtRepository.findAll().size();

        // Update the shirt
        Shirt updatedShirt = shirtRepository.findById(shirt.getId()).get();
        // Disconnect from session so that the updates on updatedShirt are not directly saved in db
        em.detach(updatedShirt);
        updatedShirt.colour1(UPDATED_COLOUR_1).colour2(UPDATED_COLOUR_2).type(UPDATED_TYPE);
        ShirtDTO shirtDTO = shirtMapper.toDto(updatedShirt);

        restShirtMockMvc
            .perform(
                put(ENTITY_API_URL_ID, shirtDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(shirtDTO))
            )
            .andExpect(status().isOk());

        // Validate the Shirt in the database
        List<Shirt> shirtList = shirtRepository.findAll();
        assertThat(shirtList).hasSize(databaseSizeBeforeUpdate);
        Shirt testShirt = shirtList.get(shirtList.size() - 1);
        assertThat(testShirt.getColour1()).isEqualTo(UPDATED_COLOUR_1);
        assertThat(testShirt.getColour2()).isEqualTo(UPDATED_COLOUR_2);
        assertThat(testShirt.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingShirt() throws Exception {
        int databaseSizeBeforeUpdate = shirtRepository.findAll().size();
        shirt.setId(count.incrementAndGet());

        // Create the Shirt
        ShirtDTO shirtDTO = shirtMapper.toDto(shirt);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShirtMockMvc
            .perform(
                put(ENTITY_API_URL_ID, shirtDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(shirtDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Shirt in the database
        List<Shirt> shirtList = shirtRepository.findAll();
        assertThat(shirtList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchShirt() throws Exception {
        int databaseSizeBeforeUpdate = shirtRepository.findAll().size();
        shirt.setId(count.incrementAndGet());

        // Create the Shirt
        ShirtDTO shirtDTO = shirtMapper.toDto(shirt);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShirtMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(shirtDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Shirt in the database
        List<Shirt> shirtList = shirtRepository.findAll();
        assertThat(shirtList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamShirt() throws Exception {
        int databaseSizeBeforeUpdate = shirtRepository.findAll().size();
        shirt.setId(count.incrementAndGet());

        // Create the Shirt
        ShirtDTO shirtDTO = shirtMapper.toDto(shirt);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShirtMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(shirtDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Shirt in the database
        List<Shirt> shirtList = shirtRepository.findAll();
        assertThat(shirtList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateShirtWithPatch() throws Exception {
        // Initialize the database
        shirtRepository.saveAndFlush(shirt);

        int databaseSizeBeforeUpdate = shirtRepository.findAll().size();

        // Update the shirt using partial update
        Shirt partialUpdatedShirt = new Shirt();
        partialUpdatedShirt.setId(shirt.getId());

        partialUpdatedShirt.type(UPDATED_TYPE);

        restShirtMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShirt.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedShirt))
            )
            .andExpect(status().isOk());

        // Validate the Shirt in the database
        List<Shirt> shirtList = shirtRepository.findAll();
        assertThat(shirtList).hasSize(databaseSizeBeforeUpdate);
        Shirt testShirt = shirtList.get(shirtList.size() - 1);
        assertThat(testShirt.getColour1()).isEqualTo(DEFAULT_COLOUR_1);
        assertThat(testShirt.getColour2()).isEqualTo(DEFAULT_COLOUR_2);
        assertThat(testShirt.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateShirtWithPatch() throws Exception {
        // Initialize the database
        shirtRepository.saveAndFlush(shirt);

        int databaseSizeBeforeUpdate = shirtRepository.findAll().size();

        // Update the shirt using partial update
        Shirt partialUpdatedShirt = new Shirt();
        partialUpdatedShirt.setId(shirt.getId());

        partialUpdatedShirt.colour1(UPDATED_COLOUR_1).colour2(UPDATED_COLOUR_2).type(UPDATED_TYPE);

        restShirtMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShirt.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedShirt))
            )
            .andExpect(status().isOk());

        // Validate the Shirt in the database
        List<Shirt> shirtList = shirtRepository.findAll();
        assertThat(shirtList).hasSize(databaseSizeBeforeUpdate);
        Shirt testShirt = shirtList.get(shirtList.size() - 1);
        assertThat(testShirt.getColour1()).isEqualTo(UPDATED_COLOUR_1);
        assertThat(testShirt.getColour2()).isEqualTo(UPDATED_COLOUR_2);
        assertThat(testShirt.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingShirt() throws Exception {
        int databaseSizeBeforeUpdate = shirtRepository.findAll().size();
        shirt.setId(count.incrementAndGet());

        // Create the Shirt
        ShirtDTO shirtDTO = shirtMapper.toDto(shirt);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShirtMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, shirtDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(shirtDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Shirt in the database
        List<Shirt> shirtList = shirtRepository.findAll();
        assertThat(shirtList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchShirt() throws Exception {
        int databaseSizeBeforeUpdate = shirtRepository.findAll().size();
        shirt.setId(count.incrementAndGet());

        // Create the Shirt
        ShirtDTO shirtDTO = shirtMapper.toDto(shirt);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShirtMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(shirtDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Shirt in the database
        List<Shirt> shirtList = shirtRepository.findAll();
        assertThat(shirtList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamShirt() throws Exception {
        int databaseSizeBeforeUpdate = shirtRepository.findAll().size();
        shirt.setId(count.incrementAndGet());

        // Create the Shirt
        ShirtDTO shirtDTO = shirtMapper.toDto(shirt);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShirtMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(shirtDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Shirt in the database
        List<Shirt> shirtList = shirtRepository.findAll();
        assertThat(shirtList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteShirt() throws Exception {
        // Initialize the database
        shirtRepository.saveAndFlush(shirt);

        int databaseSizeBeforeDelete = shirtRepository.findAll().size();

        // Delete the shirt
        restShirtMockMvc
            .perform(delete(ENTITY_API_URL_ID, shirt.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Shirt> shirtList = shirtRepository.findAll();
        assertThat(shirtList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
