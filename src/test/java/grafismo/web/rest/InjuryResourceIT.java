package grafismo.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import grafismo.IntegrationTest;
import grafismo.domain.Injury;
import grafismo.domain.Player;
import grafismo.repository.InjuryRepository;
import grafismo.service.dto.InjuryDTO;
import grafismo.service.mapper.InjuryMapper;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link InjuryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InjuryResourceIT {

    private static final Instant DEFAULT_MOMENT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_MOMENT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final LocalDate DEFAULT_EST_RETURN_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EST_RETURN_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_REASON = "AAAAAAAAAA";
    private static final String UPDATED_REASON = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/injuries";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private InjuryRepository injuryRepository;

    @Autowired
    private InjuryMapper injuryMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInjuryMockMvc;

    private Injury injury;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Injury createEntity(EntityManager em) {
        Injury injury = new Injury().moment(DEFAULT_MOMENT).estReturnDate(DEFAULT_EST_RETURN_DATE).reason(DEFAULT_REASON);
        // Add required entity
        Player player;
        if (TestUtil.findAll(em, Player.class).isEmpty()) {
            player = PlayerResourceIT.createEntity(em);
            em.persist(player);
            em.flush();
        } else {
            player = TestUtil.findAll(em, Player.class).get(0);
        }
        injury.setPlayer(player);
        return injury;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Injury createUpdatedEntity(EntityManager em) {
        Injury injury = new Injury().moment(UPDATED_MOMENT).estReturnDate(UPDATED_EST_RETURN_DATE).reason(UPDATED_REASON);
        // Add required entity
        Player player;
        if (TestUtil.findAll(em, Player.class).isEmpty()) {
            player = PlayerResourceIT.createUpdatedEntity(em);
            em.persist(player);
            em.flush();
        } else {
            player = TestUtil.findAll(em, Player.class).get(0);
        }
        injury.setPlayer(player);
        return injury;
    }

    @BeforeEach
    public void initTest() {
        injury = createEntity(em);
    }

    @Test
    @Transactional
    void createInjury() throws Exception {
        int databaseSizeBeforeCreate = injuryRepository.findAll().size();
        // Create the Injury
        InjuryDTO injuryDTO = injuryMapper.toDto(injury);
        restInjuryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(injuryDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Injury in the database
        List<Injury> injuryList = injuryRepository.findAll();
        assertThat(injuryList).hasSize(databaseSizeBeforeCreate + 1);
        Injury testInjury = injuryList.get(injuryList.size() - 1);
        assertThat(testInjury.getMoment()).isEqualTo(DEFAULT_MOMENT);
        assertThat(testInjury.getEstReturnDate()).isEqualTo(DEFAULT_EST_RETURN_DATE);
        assertThat(testInjury.getReason()).isEqualTo(DEFAULT_REASON);
    }

    @Test
    @Transactional
    void createInjuryWithExistingId() throws Exception {
        // Create the Injury with an existing ID
        injury.setId(1L);
        InjuryDTO injuryDTO = injuryMapper.toDto(injury);

        int databaseSizeBeforeCreate = injuryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInjuryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(injuryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Injury in the database
        List<Injury> injuryList = injuryRepository.findAll();
        assertThat(injuryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllInjuries() throws Exception {
        // Initialize the database
        injuryRepository.saveAndFlush(injury);

        // Get all the injuryList
        restInjuryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(injury.getId().intValue())))
            .andExpect(jsonPath("$.[*].moment").value(hasItem(DEFAULT_MOMENT.toString())))
            .andExpect(jsonPath("$.[*].estReturnDate").value(hasItem(DEFAULT_EST_RETURN_DATE.toString())))
            .andExpect(jsonPath("$.[*].reason").value(hasItem(DEFAULT_REASON)));
    }

    @Test
    @Transactional
    void getInjury() throws Exception {
        // Initialize the database
        injuryRepository.saveAndFlush(injury);

        // Get the injury
        restInjuryMockMvc
            .perform(get(ENTITY_API_URL_ID, injury.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(injury.getId().intValue()))
            .andExpect(jsonPath("$.moment").value(DEFAULT_MOMENT.toString()))
            .andExpect(jsonPath("$.estReturnDate").value(DEFAULT_EST_RETURN_DATE.toString()))
            .andExpect(jsonPath("$.reason").value(DEFAULT_REASON));
    }

    @Test
    @Transactional
    void getNonExistingInjury() throws Exception {
        // Get the injury
        restInjuryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewInjury() throws Exception {
        // Initialize the database
        injuryRepository.saveAndFlush(injury);

        int databaseSizeBeforeUpdate = injuryRepository.findAll().size();

        // Update the injury
        Injury updatedInjury = injuryRepository.findById(injury.getId()).get();
        // Disconnect from session so that the updates on updatedInjury are not directly saved in db
        em.detach(updatedInjury);
        updatedInjury.moment(UPDATED_MOMENT).estReturnDate(UPDATED_EST_RETURN_DATE).reason(UPDATED_REASON);
        InjuryDTO injuryDTO = injuryMapper.toDto(updatedInjury);

        restInjuryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, injuryDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(injuryDTO))
            )
            .andExpect(status().isOk());

        // Validate the Injury in the database
        List<Injury> injuryList = injuryRepository.findAll();
        assertThat(injuryList).hasSize(databaseSizeBeforeUpdate);
        Injury testInjury = injuryList.get(injuryList.size() - 1);
        assertThat(testInjury.getMoment()).isEqualTo(UPDATED_MOMENT);
        assertThat(testInjury.getEstReturnDate()).isEqualTo(UPDATED_EST_RETURN_DATE);
        assertThat(testInjury.getReason()).isEqualTo(UPDATED_REASON);
    }

    @Test
    @Transactional
    void putNonExistingInjury() throws Exception {
        int databaseSizeBeforeUpdate = injuryRepository.findAll().size();
        injury.setId(count.incrementAndGet());

        // Create the Injury
        InjuryDTO injuryDTO = injuryMapper.toDto(injury);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInjuryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, injuryDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(injuryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Injury in the database
        List<Injury> injuryList = injuryRepository.findAll();
        assertThat(injuryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInjury() throws Exception {
        int databaseSizeBeforeUpdate = injuryRepository.findAll().size();
        injury.setId(count.incrementAndGet());

        // Create the Injury
        InjuryDTO injuryDTO = injuryMapper.toDto(injury);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInjuryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(injuryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Injury in the database
        List<Injury> injuryList = injuryRepository.findAll();
        assertThat(injuryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInjury() throws Exception {
        int databaseSizeBeforeUpdate = injuryRepository.findAll().size();
        injury.setId(count.incrementAndGet());

        // Create the Injury
        InjuryDTO injuryDTO = injuryMapper.toDto(injury);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInjuryMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(injuryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Injury in the database
        List<Injury> injuryList = injuryRepository.findAll();
        assertThat(injuryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInjuryWithPatch() throws Exception {
        // Initialize the database
        injuryRepository.saveAndFlush(injury);

        int databaseSizeBeforeUpdate = injuryRepository.findAll().size();

        // Update the injury using partial update
        Injury partialUpdatedInjury = new Injury();
        partialUpdatedInjury.setId(injury.getId());

        partialUpdatedInjury.estReturnDate(UPDATED_EST_RETURN_DATE).reason(UPDATED_REASON);

        restInjuryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInjury.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInjury))
            )
            .andExpect(status().isOk());

        // Validate the Injury in the database
        List<Injury> injuryList = injuryRepository.findAll();
        assertThat(injuryList).hasSize(databaseSizeBeforeUpdate);
        Injury testInjury = injuryList.get(injuryList.size() - 1);
        assertThat(testInjury.getMoment()).isEqualTo(DEFAULT_MOMENT);
        assertThat(testInjury.getEstReturnDate()).isEqualTo(UPDATED_EST_RETURN_DATE);
        assertThat(testInjury.getReason()).isEqualTo(UPDATED_REASON);
    }

    @Test
    @Transactional
    void fullUpdateInjuryWithPatch() throws Exception {
        // Initialize the database
        injuryRepository.saveAndFlush(injury);

        int databaseSizeBeforeUpdate = injuryRepository.findAll().size();

        // Update the injury using partial update
        Injury partialUpdatedInjury = new Injury();
        partialUpdatedInjury.setId(injury.getId());

        partialUpdatedInjury.moment(UPDATED_MOMENT).estReturnDate(UPDATED_EST_RETURN_DATE).reason(UPDATED_REASON);

        restInjuryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInjury.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInjury))
            )
            .andExpect(status().isOk());

        // Validate the Injury in the database
        List<Injury> injuryList = injuryRepository.findAll();
        assertThat(injuryList).hasSize(databaseSizeBeforeUpdate);
        Injury testInjury = injuryList.get(injuryList.size() - 1);
        assertThat(testInjury.getMoment()).isEqualTo(UPDATED_MOMENT);
        assertThat(testInjury.getEstReturnDate()).isEqualTo(UPDATED_EST_RETURN_DATE);
        assertThat(testInjury.getReason()).isEqualTo(UPDATED_REASON);
    }

    @Test
    @Transactional
    void patchNonExistingInjury() throws Exception {
        int databaseSizeBeforeUpdate = injuryRepository.findAll().size();
        injury.setId(count.incrementAndGet());

        // Create the Injury
        InjuryDTO injuryDTO = injuryMapper.toDto(injury);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInjuryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, injuryDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(injuryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Injury in the database
        List<Injury> injuryList = injuryRepository.findAll();
        assertThat(injuryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInjury() throws Exception {
        int databaseSizeBeforeUpdate = injuryRepository.findAll().size();
        injury.setId(count.incrementAndGet());

        // Create the Injury
        InjuryDTO injuryDTO = injuryMapper.toDto(injury);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInjuryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(injuryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Injury in the database
        List<Injury> injuryList = injuryRepository.findAll();
        assertThat(injuryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInjury() throws Exception {
        int databaseSizeBeforeUpdate = injuryRepository.findAll().size();
        injury.setId(count.incrementAndGet());

        // Create the Injury
        InjuryDTO injuryDTO = injuryMapper.toDto(injury);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInjuryMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(injuryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Injury in the database
        List<Injury> injuryList = injuryRepository.findAll();
        assertThat(injuryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInjury() throws Exception {
        // Initialize the database
        injuryRepository.saveAndFlush(injury);

        int databaseSizeBeforeDelete = injuryRepository.findAll().size();

        // Delete the injury
        restInjuryMockMvc
            .perform(delete(ENTITY_API_URL_ID, injury.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Injury> injuryList = injuryRepository.findAll();
        assertThat(injuryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
