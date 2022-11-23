package grafismo.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import grafismo.IntegrationTest;
import grafismo.domain.Person;
import grafismo.domain.Referee;
import grafismo.repository.RefereeRepository;
import grafismo.service.RefereeService;
import grafismo.service.dto.RefereeDTO;
import grafismo.service.mapper.RefereeMapper;
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
 * Integration tests for the {@link RefereeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class RefereeResourceIT {

    private static final String ENTITY_API_URL = "/api/referees";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RefereeRepository refereeRepository;

    @Mock
    private RefereeRepository refereeRepositoryMock;

    @Autowired
    private RefereeMapper refereeMapper;

    @Mock
    private RefereeService refereeServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRefereeMockMvc;

    private Referee referee;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Referee createEntity(EntityManager em) {
        Referee referee = new Referee();
        // Add required entity
        Person person;
        if (TestUtil.findAll(em, Person.class).isEmpty()) {
            person = PersonResourceIT.createEntity(em);
            em.persist(person);
            em.flush();
        } else {
            person = TestUtil.findAll(em, Person.class).get(0);
        }
        referee.setPerson(person);
        return referee;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Referee createUpdatedEntity(EntityManager em) {
        Referee referee = new Referee();
        // Add required entity
        Person person;
        if (TestUtil.findAll(em, Person.class).isEmpty()) {
            person = PersonResourceIT.createUpdatedEntity(em);
            em.persist(person);
            em.flush();
        } else {
            person = TestUtil.findAll(em, Person.class).get(0);
        }
        referee.setPerson(person);
        return referee;
    }

    @BeforeEach
    public void initTest() {
        referee = createEntity(em);
    }

    @Test
    @Transactional
    void createReferee() throws Exception {
        int databaseSizeBeforeCreate = refereeRepository.findAll().size();
        // Create the Referee
        RefereeDTO refereeDTO = refereeMapper.toDto(referee);
        restRefereeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(refereeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Referee in the database
        List<Referee> refereeList = refereeRepository.findAll();
        assertThat(refereeList).hasSize(databaseSizeBeforeCreate + 1);
        Referee testReferee = refereeList.get(refereeList.size() - 1);
    }

    @Test
    @Transactional
    void createRefereeWithExistingId() throws Exception {
        // Create the Referee with an existing ID
        referee.setId(1L);
        RefereeDTO refereeDTO = refereeMapper.toDto(referee);

        int databaseSizeBeforeCreate = refereeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRefereeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(refereeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Referee in the database
        List<Referee> refereeList = refereeRepository.findAll();
        assertThat(refereeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllReferees() throws Exception {
        // Initialize the database
        refereeRepository.saveAndFlush(referee);

        // Get all the refereeList
        restRefereeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(referee.getId().intValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllRefereesWithEagerRelationshipsIsEnabled() throws Exception {
        when(refereeServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restRefereeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(refereeServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllRefereesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(refereeServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restRefereeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(refereeServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getReferee() throws Exception {
        // Initialize the database
        refereeRepository.saveAndFlush(referee);

        // Get the referee
        restRefereeMockMvc
            .perform(get(ENTITY_API_URL_ID, referee.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(referee.getId().intValue()));
    }

    @Test
    @Transactional
    void getNonExistingReferee() throws Exception {
        // Get the referee
        restRefereeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewReferee() throws Exception {
        // Initialize the database
        refereeRepository.saveAndFlush(referee);

        int databaseSizeBeforeUpdate = refereeRepository.findAll().size();

        // Update the referee
        Referee updatedReferee = refereeRepository.findById(referee.getId()).get();
        // Disconnect from session so that the updates on updatedReferee are not directly saved in db
        em.detach(updatedReferee);
        RefereeDTO refereeDTO = refereeMapper.toDto(updatedReferee);

        restRefereeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, refereeDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(refereeDTO))
            )
            .andExpect(status().isOk());

        // Validate the Referee in the database
        List<Referee> refereeList = refereeRepository.findAll();
        assertThat(refereeList).hasSize(databaseSizeBeforeUpdate);
        Referee testReferee = refereeList.get(refereeList.size() - 1);
    }

    @Test
    @Transactional
    void putNonExistingReferee() throws Exception {
        int databaseSizeBeforeUpdate = refereeRepository.findAll().size();
        referee.setId(count.incrementAndGet());

        // Create the Referee
        RefereeDTO refereeDTO = refereeMapper.toDto(referee);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRefereeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, refereeDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(refereeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Referee in the database
        List<Referee> refereeList = refereeRepository.findAll();
        assertThat(refereeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReferee() throws Exception {
        int databaseSizeBeforeUpdate = refereeRepository.findAll().size();
        referee.setId(count.incrementAndGet());

        // Create the Referee
        RefereeDTO refereeDTO = refereeMapper.toDto(referee);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRefereeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(refereeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Referee in the database
        List<Referee> refereeList = refereeRepository.findAll();
        assertThat(refereeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReferee() throws Exception {
        int databaseSizeBeforeUpdate = refereeRepository.findAll().size();
        referee.setId(count.incrementAndGet());

        // Create the Referee
        RefereeDTO refereeDTO = refereeMapper.toDto(referee);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRefereeMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(refereeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Referee in the database
        List<Referee> refereeList = refereeRepository.findAll();
        assertThat(refereeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRefereeWithPatch() throws Exception {
        // Initialize the database
        refereeRepository.saveAndFlush(referee);

        int databaseSizeBeforeUpdate = refereeRepository.findAll().size();

        // Update the referee using partial update
        Referee partialUpdatedReferee = new Referee();
        partialUpdatedReferee.setId(referee.getId());

        restRefereeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReferee.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReferee))
            )
            .andExpect(status().isOk());

        // Validate the Referee in the database
        List<Referee> refereeList = refereeRepository.findAll();
        assertThat(refereeList).hasSize(databaseSizeBeforeUpdate);
        Referee testReferee = refereeList.get(refereeList.size() - 1);
    }

    @Test
    @Transactional
    void fullUpdateRefereeWithPatch() throws Exception {
        // Initialize the database
        refereeRepository.saveAndFlush(referee);

        int databaseSizeBeforeUpdate = refereeRepository.findAll().size();

        // Update the referee using partial update
        Referee partialUpdatedReferee = new Referee();
        partialUpdatedReferee.setId(referee.getId());

        restRefereeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReferee.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReferee))
            )
            .andExpect(status().isOk());

        // Validate the Referee in the database
        List<Referee> refereeList = refereeRepository.findAll();
        assertThat(refereeList).hasSize(databaseSizeBeforeUpdate);
        Referee testReferee = refereeList.get(refereeList.size() - 1);
    }

    @Test
    @Transactional
    void patchNonExistingReferee() throws Exception {
        int databaseSizeBeforeUpdate = refereeRepository.findAll().size();
        referee.setId(count.incrementAndGet());

        // Create the Referee
        RefereeDTO refereeDTO = refereeMapper.toDto(referee);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRefereeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, refereeDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(refereeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Referee in the database
        List<Referee> refereeList = refereeRepository.findAll();
        assertThat(refereeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReferee() throws Exception {
        int databaseSizeBeforeUpdate = refereeRepository.findAll().size();
        referee.setId(count.incrementAndGet());

        // Create the Referee
        RefereeDTO refereeDTO = refereeMapper.toDto(referee);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRefereeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(refereeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Referee in the database
        List<Referee> refereeList = refereeRepository.findAll();
        assertThat(refereeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReferee() throws Exception {
        int databaseSizeBeforeUpdate = refereeRepository.findAll().size();
        referee.setId(count.incrementAndGet());

        // Create the Referee
        RefereeDTO refereeDTO = refereeMapper.toDto(referee);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRefereeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(refereeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Referee in the database
        List<Referee> refereeList = refereeRepository.findAll();
        assertThat(refereeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReferee() throws Exception {
        // Initialize the database
        refereeRepository.saveAndFlush(referee);

        int databaseSizeBeforeDelete = refereeRepository.findAll().size();

        // Delete the referee
        restRefereeMockMvc
            .perform(delete(ENTITY_API_URL_ID, referee.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Referee> refereeList = refereeRepository.findAll();
        assertThat(refereeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
