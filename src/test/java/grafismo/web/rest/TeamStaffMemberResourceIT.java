package grafismo.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import grafismo.IntegrationTest;
import grafismo.domain.Person;
import grafismo.domain.TeamStaffMember;
import grafismo.domain.enumeration.StaffMemberRole;
import grafismo.repository.TeamStaffMemberRepository;
import grafismo.service.TeamStaffMemberService;
import grafismo.service.dto.TeamStaffMemberDTO;
import grafismo.service.mapper.TeamStaffMemberMapper;
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
 * Integration tests for the {@link TeamStaffMemberResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class TeamStaffMemberResourceIT {

    private static final StaffMemberRole DEFAULT_ROLE = StaffMemberRole.DT;
    private static final StaffMemberRole UPDATED_ROLE = StaffMemberRole.DT2;

    private static final String ENTITY_API_URL = "/api/team-staff-members";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TeamStaffMemberRepository teamStaffMemberRepository;

    @Mock
    private TeamStaffMemberRepository teamStaffMemberRepositoryMock;

    @Autowired
    private TeamStaffMemberMapper teamStaffMemberMapper;

    @Mock
    private TeamStaffMemberService teamStaffMemberServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTeamStaffMemberMockMvc;

    private TeamStaffMember teamStaffMember;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TeamStaffMember createEntity(EntityManager em) {
        TeamStaffMember teamStaffMember = new TeamStaffMember().role(DEFAULT_ROLE);
        // Add required entity
        Person person;
        if (TestUtil.findAll(em, Person.class).isEmpty()) {
            person = PersonResourceIT.createEntity(em);
            em.persist(person);
            em.flush();
        } else {
            person = TestUtil.findAll(em, Person.class).get(0);
        }
        teamStaffMember.setPerson(person);
        return teamStaffMember;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TeamStaffMember createUpdatedEntity(EntityManager em) {
        TeamStaffMember teamStaffMember = new TeamStaffMember().role(UPDATED_ROLE);
        // Add required entity
        Person person;
        if (TestUtil.findAll(em, Person.class).isEmpty()) {
            person = PersonResourceIT.createUpdatedEntity(em);
            em.persist(person);
            em.flush();
        } else {
            person = TestUtil.findAll(em, Person.class).get(0);
        }
        teamStaffMember.setPerson(person);
        return teamStaffMember;
    }

    @BeforeEach
    public void initTest() {
        teamStaffMember = createEntity(em);
    }

    @Test
    @Transactional
    void createTeamStaffMember() throws Exception {
        int databaseSizeBeforeCreate = teamStaffMemberRepository.findAll().size();
        // Create the TeamStaffMember
        TeamStaffMemberDTO teamStaffMemberDTO = teamStaffMemberMapper.toDto(teamStaffMember);
        restTeamStaffMemberMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(teamStaffMemberDTO))
            )
            .andExpect(status().isCreated());

        // Validate the TeamStaffMember in the database
        List<TeamStaffMember> teamStaffMemberList = teamStaffMemberRepository.findAll();
        assertThat(teamStaffMemberList).hasSize(databaseSizeBeforeCreate + 1);
        TeamStaffMember testTeamStaffMember = teamStaffMemberList.get(teamStaffMemberList.size() - 1);
        assertThat(testTeamStaffMember.getRole()).isEqualTo(DEFAULT_ROLE);
    }

    @Test
    @Transactional
    void createTeamStaffMemberWithExistingId() throws Exception {
        // Create the TeamStaffMember with an existing ID
        teamStaffMember.setId(1L);
        TeamStaffMemberDTO teamStaffMemberDTO = teamStaffMemberMapper.toDto(teamStaffMember);

        int databaseSizeBeforeCreate = teamStaffMemberRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTeamStaffMemberMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(teamStaffMemberDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TeamStaffMember in the database
        List<TeamStaffMember> teamStaffMemberList = teamStaffMemberRepository.findAll();
        assertThat(teamStaffMemberList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTeamStaffMembers() throws Exception {
        // Initialize the database
        teamStaffMemberRepository.saveAndFlush(teamStaffMember);

        // Get all the teamStaffMemberList
        restTeamStaffMemberMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(teamStaffMember.getId().intValue())))
            .andExpect(jsonPath("$.[*].role").value(hasItem(DEFAULT_ROLE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTeamStaffMembersWithEagerRelationshipsIsEnabled() throws Exception {
        when(teamStaffMemberServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTeamStaffMemberMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(teamStaffMemberServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTeamStaffMembersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(teamStaffMemberServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTeamStaffMemberMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(teamStaffMemberServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getTeamStaffMember() throws Exception {
        // Initialize the database
        teamStaffMemberRepository.saveAndFlush(teamStaffMember);

        // Get the teamStaffMember
        restTeamStaffMemberMockMvc
            .perform(get(ENTITY_API_URL_ID, teamStaffMember.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(teamStaffMember.getId().intValue()))
            .andExpect(jsonPath("$.role").value(DEFAULT_ROLE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingTeamStaffMember() throws Exception {
        // Get the teamStaffMember
        restTeamStaffMemberMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTeamStaffMember() throws Exception {
        // Initialize the database
        teamStaffMemberRepository.saveAndFlush(teamStaffMember);

        int databaseSizeBeforeUpdate = teamStaffMemberRepository.findAll().size();

        // Update the teamStaffMember
        TeamStaffMember updatedTeamStaffMember = teamStaffMemberRepository.findById(teamStaffMember.getId()).get();
        // Disconnect from session so that the updates on updatedTeamStaffMember are not directly saved in db
        em.detach(updatedTeamStaffMember);
        updatedTeamStaffMember.role(UPDATED_ROLE);
        TeamStaffMemberDTO teamStaffMemberDTO = teamStaffMemberMapper.toDto(updatedTeamStaffMember);

        restTeamStaffMemberMockMvc
            .perform(
                put(ENTITY_API_URL_ID, teamStaffMemberDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(teamStaffMemberDTO))
            )
            .andExpect(status().isOk());

        // Validate the TeamStaffMember in the database
        List<TeamStaffMember> teamStaffMemberList = teamStaffMemberRepository.findAll();
        assertThat(teamStaffMemberList).hasSize(databaseSizeBeforeUpdate);
        TeamStaffMember testTeamStaffMember = teamStaffMemberList.get(teamStaffMemberList.size() - 1);
        assertThat(testTeamStaffMember.getRole()).isEqualTo(UPDATED_ROLE);
    }

    @Test
    @Transactional
    void putNonExistingTeamStaffMember() throws Exception {
        int databaseSizeBeforeUpdate = teamStaffMemberRepository.findAll().size();
        teamStaffMember.setId(count.incrementAndGet());

        // Create the TeamStaffMember
        TeamStaffMemberDTO teamStaffMemberDTO = teamStaffMemberMapper.toDto(teamStaffMember);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTeamStaffMemberMockMvc
            .perform(
                put(ENTITY_API_URL_ID, teamStaffMemberDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(teamStaffMemberDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TeamStaffMember in the database
        List<TeamStaffMember> teamStaffMemberList = teamStaffMemberRepository.findAll();
        assertThat(teamStaffMemberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTeamStaffMember() throws Exception {
        int databaseSizeBeforeUpdate = teamStaffMemberRepository.findAll().size();
        teamStaffMember.setId(count.incrementAndGet());

        // Create the TeamStaffMember
        TeamStaffMemberDTO teamStaffMemberDTO = teamStaffMemberMapper.toDto(teamStaffMember);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTeamStaffMemberMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(teamStaffMemberDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TeamStaffMember in the database
        List<TeamStaffMember> teamStaffMemberList = teamStaffMemberRepository.findAll();
        assertThat(teamStaffMemberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTeamStaffMember() throws Exception {
        int databaseSizeBeforeUpdate = teamStaffMemberRepository.findAll().size();
        teamStaffMember.setId(count.incrementAndGet());

        // Create the TeamStaffMember
        TeamStaffMemberDTO teamStaffMemberDTO = teamStaffMemberMapper.toDto(teamStaffMember);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTeamStaffMemberMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(teamStaffMemberDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TeamStaffMember in the database
        List<TeamStaffMember> teamStaffMemberList = teamStaffMemberRepository.findAll();
        assertThat(teamStaffMemberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTeamStaffMemberWithPatch() throws Exception {
        // Initialize the database
        teamStaffMemberRepository.saveAndFlush(teamStaffMember);

        int databaseSizeBeforeUpdate = teamStaffMemberRepository.findAll().size();

        // Update the teamStaffMember using partial update
        TeamStaffMember partialUpdatedTeamStaffMember = new TeamStaffMember();
        partialUpdatedTeamStaffMember.setId(teamStaffMember.getId());

        restTeamStaffMemberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTeamStaffMember.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTeamStaffMember))
            )
            .andExpect(status().isOk());

        // Validate the TeamStaffMember in the database
        List<TeamStaffMember> teamStaffMemberList = teamStaffMemberRepository.findAll();
        assertThat(teamStaffMemberList).hasSize(databaseSizeBeforeUpdate);
        TeamStaffMember testTeamStaffMember = teamStaffMemberList.get(teamStaffMemberList.size() - 1);
        assertThat(testTeamStaffMember.getRole()).isEqualTo(DEFAULT_ROLE);
    }

    @Test
    @Transactional
    void fullUpdateTeamStaffMemberWithPatch() throws Exception {
        // Initialize the database
        teamStaffMemberRepository.saveAndFlush(teamStaffMember);

        int databaseSizeBeforeUpdate = teamStaffMemberRepository.findAll().size();

        // Update the teamStaffMember using partial update
        TeamStaffMember partialUpdatedTeamStaffMember = new TeamStaffMember();
        partialUpdatedTeamStaffMember.setId(teamStaffMember.getId());

        partialUpdatedTeamStaffMember.role(UPDATED_ROLE);

        restTeamStaffMemberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTeamStaffMember.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTeamStaffMember))
            )
            .andExpect(status().isOk());

        // Validate the TeamStaffMember in the database
        List<TeamStaffMember> teamStaffMemberList = teamStaffMemberRepository.findAll();
        assertThat(teamStaffMemberList).hasSize(databaseSizeBeforeUpdate);
        TeamStaffMember testTeamStaffMember = teamStaffMemberList.get(teamStaffMemberList.size() - 1);
        assertThat(testTeamStaffMember.getRole()).isEqualTo(UPDATED_ROLE);
    }

    @Test
    @Transactional
    void patchNonExistingTeamStaffMember() throws Exception {
        int databaseSizeBeforeUpdate = teamStaffMemberRepository.findAll().size();
        teamStaffMember.setId(count.incrementAndGet());

        // Create the TeamStaffMember
        TeamStaffMemberDTO teamStaffMemberDTO = teamStaffMemberMapper.toDto(teamStaffMember);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTeamStaffMemberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, teamStaffMemberDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(teamStaffMemberDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TeamStaffMember in the database
        List<TeamStaffMember> teamStaffMemberList = teamStaffMemberRepository.findAll();
        assertThat(teamStaffMemberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTeamStaffMember() throws Exception {
        int databaseSizeBeforeUpdate = teamStaffMemberRepository.findAll().size();
        teamStaffMember.setId(count.incrementAndGet());

        // Create the TeamStaffMember
        TeamStaffMemberDTO teamStaffMemberDTO = teamStaffMemberMapper.toDto(teamStaffMember);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTeamStaffMemberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(teamStaffMemberDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TeamStaffMember in the database
        List<TeamStaffMember> teamStaffMemberList = teamStaffMemberRepository.findAll();
        assertThat(teamStaffMemberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTeamStaffMember() throws Exception {
        int databaseSizeBeforeUpdate = teamStaffMemberRepository.findAll().size();
        teamStaffMember.setId(count.incrementAndGet());

        // Create the TeamStaffMember
        TeamStaffMemberDTO teamStaffMemberDTO = teamStaffMemberMapper.toDto(teamStaffMember);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTeamStaffMemberMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(teamStaffMemberDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TeamStaffMember in the database
        List<TeamStaffMember> teamStaffMemberList = teamStaffMemberRepository.findAll();
        assertThat(teamStaffMemberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTeamStaffMember() throws Exception {
        // Initialize the database
        teamStaffMemberRepository.saveAndFlush(teamStaffMember);

        int databaseSizeBeforeDelete = teamStaffMemberRepository.findAll().size();

        // Delete the teamStaffMember
        restTeamStaffMemberMockMvc
            .perform(delete(ENTITY_API_URL_ID, teamStaffMember.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TeamStaffMember> teamStaffMemberList = teamStaffMemberRepository.findAll();
        assertThat(teamStaffMemberList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
