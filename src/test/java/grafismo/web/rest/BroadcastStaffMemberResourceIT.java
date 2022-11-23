package grafismo.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import grafismo.IntegrationTest;
import grafismo.domain.BroadcastStaffMember;
import grafismo.domain.Person;
import grafismo.repository.BroadcastStaffMemberRepository;
import grafismo.service.BroadcastStaffMemberService;
import grafismo.service.dto.BroadcastStaffMemberDTO;
import grafismo.service.mapper.BroadcastStaffMemberMapper;
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
 * Integration tests for the {@link BroadcastStaffMemberResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class BroadcastStaffMemberResourceIT {

    private static final String ENTITY_API_URL = "/api/broadcast-staff-members";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BroadcastStaffMemberRepository broadcastStaffMemberRepository;

    @Mock
    private BroadcastStaffMemberRepository broadcastStaffMemberRepositoryMock;

    @Autowired
    private BroadcastStaffMemberMapper broadcastStaffMemberMapper;

    @Mock
    private BroadcastStaffMemberService broadcastStaffMemberServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBroadcastStaffMemberMockMvc;

    private BroadcastStaffMember broadcastStaffMember;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BroadcastStaffMember createEntity(EntityManager em) {
        BroadcastStaffMember broadcastStaffMember = new BroadcastStaffMember();
        // Add required entity
        Person person;
        if (TestUtil.findAll(em, Person.class).isEmpty()) {
            person = PersonResourceIT.createEntity(em);
            em.persist(person);
            em.flush();
        } else {
            person = TestUtil.findAll(em, Person.class).get(0);
        }
        broadcastStaffMember.setPerson(person);
        return broadcastStaffMember;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BroadcastStaffMember createUpdatedEntity(EntityManager em) {
        BroadcastStaffMember broadcastStaffMember = new BroadcastStaffMember();
        // Add required entity
        Person person;
        if (TestUtil.findAll(em, Person.class).isEmpty()) {
            person = PersonResourceIT.createUpdatedEntity(em);
            em.persist(person);
            em.flush();
        } else {
            person = TestUtil.findAll(em, Person.class).get(0);
        }
        broadcastStaffMember.setPerson(person);
        return broadcastStaffMember;
    }

    @BeforeEach
    public void initTest() {
        broadcastStaffMember = createEntity(em);
    }

    @Test
    @Transactional
    void createBroadcastStaffMember() throws Exception {
        int databaseSizeBeforeCreate = broadcastStaffMemberRepository.findAll().size();
        // Create the BroadcastStaffMember
        BroadcastStaffMemberDTO broadcastStaffMemberDTO = broadcastStaffMemberMapper.toDto(broadcastStaffMember);
        restBroadcastStaffMemberMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(broadcastStaffMemberDTO))
            )
            .andExpect(status().isCreated());

        // Validate the BroadcastStaffMember in the database
        List<BroadcastStaffMember> broadcastStaffMemberList = broadcastStaffMemberRepository.findAll();
        assertThat(broadcastStaffMemberList).hasSize(databaseSizeBeforeCreate + 1);
        BroadcastStaffMember testBroadcastStaffMember = broadcastStaffMemberList.get(broadcastStaffMemberList.size() - 1);
    }

    @Test
    @Transactional
    void createBroadcastStaffMemberWithExistingId() throws Exception {
        // Create the BroadcastStaffMember with an existing ID
        broadcastStaffMember.setId(1L);
        BroadcastStaffMemberDTO broadcastStaffMemberDTO = broadcastStaffMemberMapper.toDto(broadcastStaffMember);

        int databaseSizeBeforeCreate = broadcastStaffMemberRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBroadcastStaffMemberMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(broadcastStaffMemberDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BroadcastStaffMember in the database
        List<BroadcastStaffMember> broadcastStaffMemberList = broadcastStaffMemberRepository.findAll();
        assertThat(broadcastStaffMemberList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllBroadcastStaffMembers() throws Exception {
        // Initialize the database
        broadcastStaffMemberRepository.saveAndFlush(broadcastStaffMember);

        // Get all the broadcastStaffMemberList
        restBroadcastStaffMemberMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(broadcastStaffMember.getId().intValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBroadcastStaffMembersWithEagerRelationshipsIsEnabled() throws Exception {
        when(broadcastStaffMemberServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBroadcastStaffMemberMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(broadcastStaffMemberServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBroadcastStaffMembersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(broadcastStaffMemberServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBroadcastStaffMemberMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(broadcastStaffMemberServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getBroadcastStaffMember() throws Exception {
        // Initialize the database
        broadcastStaffMemberRepository.saveAndFlush(broadcastStaffMember);

        // Get the broadcastStaffMember
        restBroadcastStaffMemberMockMvc
            .perform(get(ENTITY_API_URL_ID, broadcastStaffMember.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(broadcastStaffMember.getId().intValue()));
    }

    @Test
    @Transactional
    void getNonExistingBroadcastStaffMember() throws Exception {
        // Get the broadcastStaffMember
        restBroadcastStaffMemberMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewBroadcastStaffMember() throws Exception {
        // Initialize the database
        broadcastStaffMemberRepository.saveAndFlush(broadcastStaffMember);

        int databaseSizeBeforeUpdate = broadcastStaffMemberRepository.findAll().size();

        // Update the broadcastStaffMember
        BroadcastStaffMember updatedBroadcastStaffMember = broadcastStaffMemberRepository.findById(broadcastStaffMember.getId()).get();
        // Disconnect from session so that the updates on updatedBroadcastStaffMember are not directly saved in db
        em.detach(updatedBroadcastStaffMember);
        BroadcastStaffMemberDTO broadcastStaffMemberDTO = broadcastStaffMemberMapper.toDto(updatedBroadcastStaffMember);

        restBroadcastStaffMemberMockMvc
            .perform(
                put(ENTITY_API_URL_ID, broadcastStaffMemberDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(broadcastStaffMemberDTO))
            )
            .andExpect(status().isOk());

        // Validate the BroadcastStaffMember in the database
        List<BroadcastStaffMember> broadcastStaffMemberList = broadcastStaffMemberRepository.findAll();
        assertThat(broadcastStaffMemberList).hasSize(databaseSizeBeforeUpdate);
        BroadcastStaffMember testBroadcastStaffMember = broadcastStaffMemberList.get(broadcastStaffMemberList.size() - 1);
    }

    @Test
    @Transactional
    void putNonExistingBroadcastStaffMember() throws Exception {
        int databaseSizeBeforeUpdate = broadcastStaffMemberRepository.findAll().size();
        broadcastStaffMember.setId(count.incrementAndGet());

        // Create the BroadcastStaffMember
        BroadcastStaffMemberDTO broadcastStaffMemberDTO = broadcastStaffMemberMapper.toDto(broadcastStaffMember);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBroadcastStaffMemberMockMvc
            .perform(
                put(ENTITY_API_URL_ID, broadcastStaffMemberDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(broadcastStaffMemberDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BroadcastStaffMember in the database
        List<BroadcastStaffMember> broadcastStaffMemberList = broadcastStaffMemberRepository.findAll();
        assertThat(broadcastStaffMemberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBroadcastStaffMember() throws Exception {
        int databaseSizeBeforeUpdate = broadcastStaffMemberRepository.findAll().size();
        broadcastStaffMember.setId(count.incrementAndGet());

        // Create the BroadcastStaffMember
        BroadcastStaffMemberDTO broadcastStaffMemberDTO = broadcastStaffMemberMapper.toDto(broadcastStaffMember);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBroadcastStaffMemberMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(broadcastStaffMemberDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BroadcastStaffMember in the database
        List<BroadcastStaffMember> broadcastStaffMemberList = broadcastStaffMemberRepository.findAll();
        assertThat(broadcastStaffMemberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBroadcastStaffMember() throws Exception {
        int databaseSizeBeforeUpdate = broadcastStaffMemberRepository.findAll().size();
        broadcastStaffMember.setId(count.incrementAndGet());

        // Create the BroadcastStaffMember
        BroadcastStaffMemberDTO broadcastStaffMemberDTO = broadcastStaffMemberMapper.toDto(broadcastStaffMember);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBroadcastStaffMemberMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(broadcastStaffMemberDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BroadcastStaffMember in the database
        List<BroadcastStaffMember> broadcastStaffMemberList = broadcastStaffMemberRepository.findAll();
        assertThat(broadcastStaffMemberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBroadcastStaffMemberWithPatch() throws Exception {
        // Initialize the database
        broadcastStaffMemberRepository.saveAndFlush(broadcastStaffMember);

        int databaseSizeBeforeUpdate = broadcastStaffMemberRepository.findAll().size();

        // Update the broadcastStaffMember using partial update
        BroadcastStaffMember partialUpdatedBroadcastStaffMember = new BroadcastStaffMember();
        partialUpdatedBroadcastStaffMember.setId(broadcastStaffMember.getId());

        restBroadcastStaffMemberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBroadcastStaffMember.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBroadcastStaffMember))
            )
            .andExpect(status().isOk());

        // Validate the BroadcastStaffMember in the database
        List<BroadcastStaffMember> broadcastStaffMemberList = broadcastStaffMemberRepository.findAll();
        assertThat(broadcastStaffMemberList).hasSize(databaseSizeBeforeUpdate);
        BroadcastStaffMember testBroadcastStaffMember = broadcastStaffMemberList.get(broadcastStaffMemberList.size() - 1);
    }

    @Test
    @Transactional
    void fullUpdateBroadcastStaffMemberWithPatch() throws Exception {
        // Initialize the database
        broadcastStaffMemberRepository.saveAndFlush(broadcastStaffMember);

        int databaseSizeBeforeUpdate = broadcastStaffMemberRepository.findAll().size();

        // Update the broadcastStaffMember using partial update
        BroadcastStaffMember partialUpdatedBroadcastStaffMember = new BroadcastStaffMember();
        partialUpdatedBroadcastStaffMember.setId(broadcastStaffMember.getId());

        restBroadcastStaffMemberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBroadcastStaffMember.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBroadcastStaffMember))
            )
            .andExpect(status().isOk());

        // Validate the BroadcastStaffMember in the database
        List<BroadcastStaffMember> broadcastStaffMemberList = broadcastStaffMemberRepository.findAll();
        assertThat(broadcastStaffMemberList).hasSize(databaseSizeBeforeUpdate);
        BroadcastStaffMember testBroadcastStaffMember = broadcastStaffMemberList.get(broadcastStaffMemberList.size() - 1);
    }

    @Test
    @Transactional
    void patchNonExistingBroadcastStaffMember() throws Exception {
        int databaseSizeBeforeUpdate = broadcastStaffMemberRepository.findAll().size();
        broadcastStaffMember.setId(count.incrementAndGet());

        // Create the BroadcastStaffMember
        BroadcastStaffMemberDTO broadcastStaffMemberDTO = broadcastStaffMemberMapper.toDto(broadcastStaffMember);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBroadcastStaffMemberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, broadcastStaffMemberDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(broadcastStaffMemberDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BroadcastStaffMember in the database
        List<BroadcastStaffMember> broadcastStaffMemberList = broadcastStaffMemberRepository.findAll();
        assertThat(broadcastStaffMemberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBroadcastStaffMember() throws Exception {
        int databaseSizeBeforeUpdate = broadcastStaffMemberRepository.findAll().size();
        broadcastStaffMember.setId(count.incrementAndGet());

        // Create the BroadcastStaffMember
        BroadcastStaffMemberDTO broadcastStaffMemberDTO = broadcastStaffMemberMapper.toDto(broadcastStaffMember);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBroadcastStaffMemberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(broadcastStaffMemberDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BroadcastStaffMember in the database
        List<BroadcastStaffMember> broadcastStaffMemberList = broadcastStaffMemberRepository.findAll();
        assertThat(broadcastStaffMemberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBroadcastStaffMember() throws Exception {
        int databaseSizeBeforeUpdate = broadcastStaffMemberRepository.findAll().size();
        broadcastStaffMember.setId(count.incrementAndGet());

        // Create the BroadcastStaffMember
        BroadcastStaffMemberDTO broadcastStaffMemberDTO = broadcastStaffMemberMapper.toDto(broadcastStaffMember);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBroadcastStaffMemberMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(broadcastStaffMemberDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BroadcastStaffMember in the database
        List<BroadcastStaffMember> broadcastStaffMemberList = broadcastStaffMemberRepository.findAll();
        assertThat(broadcastStaffMemberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBroadcastStaffMember() throws Exception {
        // Initialize the database
        broadcastStaffMemberRepository.saveAndFlush(broadcastStaffMember);

        int databaseSizeBeforeDelete = broadcastStaffMemberRepository.findAll().size();

        // Delete the broadcastStaffMember
        restBroadcastStaffMemberMockMvc
            .perform(delete(ENTITY_API_URL_ID, broadcastStaffMember.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BroadcastStaffMember> broadcastStaffMemberList = broadcastStaffMemberRepository.findAll();
        assertThat(broadcastStaffMemberList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
