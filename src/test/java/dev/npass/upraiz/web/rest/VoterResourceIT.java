package dev.npass.upraiz.web.rest;

import static dev.npass.upraiz.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import dev.npass.upraiz.IntegrationTest;
import dev.npass.upraiz.domain.Voter;
import dev.npass.upraiz.repository.VoterRepository;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
 * Integration tests for the {@link VoterResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VoterResourceIT {

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/voters";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VoterRepository voterRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVoterMockMvc;

    private Voter voter;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Voter createEntity(EntityManager em) {
        Voter voter = new Voter()
            .email(DEFAULT_EMAIL)
            .name(DEFAULT_NAME)
            .created(DEFAULT_CREATED)
            .active(DEFAULT_ACTIVE)
            .comment(DEFAULT_COMMENT);
        return voter;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Voter createUpdatedEntity(EntityManager em) {
        Voter voter = new Voter()
            .email(UPDATED_EMAIL)
            .name(UPDATED_NAME)
            .created(UPDATED_CREATED)
            .active(UPDATED_ACTIVE)
            .comment(UPDATED_COMMENT);
        return voter;
    }

    @BeforeEach
    public void initTest() {
        voter = createEntity(em);
    }

    @Test
    @Transactional
    void createVoter() throws Exception {
        int databaseSizeBeforeCreate = voterRepository.findAll().size();
        // Create the Voter
        restVoterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(voter)))
            .andExpect(status().isCreated());

        // Validate the Voter in the database
        List<Voter> voterList = voterRepository.findAll();
        assertThat(voterList).hasSize(databaseSizeBeforeCreate + 1);
        Voter testVoter = voterList.get(voterList.size() - 1);
        assertThat(testVoter.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testVoter.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testVoter.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testVoter.getActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testVoter.getComment()).isEqualTo(DEFAULT_COMMENT);
    }

    @Test
    @Transactional
    void createVoterWithExistingId() throws Exception {
        // Create the Voter with an existing ID
        voter.setId(1L);

        int databaseSizeBeforeCreate = voterRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVoterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(voter)))
            .andExpect(status().isBadRequest());

        // Validate the Voter in the database
        List<Voter> voterList = voterRepository.findAll();
        assertThat(voterList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = voterRepository.findAll().size();
        // set the field null
        voter.setEmail(null);

        // Create the Voter, which fails.

        restVoterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(voter)))
            .andExpect(status().isBadRequest());

        List<Voter> voterList = voterRepository.findAll();
        assertThat(voterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = voterRepository.findAll().size();
        // set the field null
        voter.setName(null);

        // Create the Voter, which fails.

        restVoterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(voter)))
            .andExpect(status().isBadRequest());

        List<Voter> voterList = voterRepository.findAll();
        assertThat(voterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllVoters() throws Exception {
        // Initialize the database
        voterRepository.saveAndFlush(voter);

        // Get all the voterList
        restVoterMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(voter.getId().intValue())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].created").value(hasItem(sameInstant(DEFAULT_CREATED))))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT)));
    }

    @Test
    @Transactional
    void getVoter() throws Exception {
        // Initialize the database
        voterRepository.saveAndFlush(voter);

        // Get the voter
        restVoterMockMvc
            .perform(get(ENTITY_API_URL_ID, voter.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(voter.getId().intValue()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.created").value(sameInstant(DEFAULT_CREATED)))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT));
    }

    @Test
    @Transactional
    void getNonExistingVoter() throws Exception {
        // Get the voter
        restVoterMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingVoter() throws Exception {
        // Initialize the database
        voterRepository.saveAndFlush(voter);

        int databaseSizeBeforeUpdate = voterRepository.findAll().size();

        // Update the voter
        Voter updatedVoter = voterRepository.findById(voter.getId()).get();
        // Disconnect from session so that the updates on updatedVoter are not directly saved in db
        em.detach(updatedVoter);
        updatedVoter.email(UPDATED_EMAIL).name(UPDATED_NAME).created(UPDATED_CREATED).active(UPDATED_ACTIVE).comment(UPDATED_COMMENT);

        restVoterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedVoter.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedVoter))
            )
            .andExpect(status().isOk());

        // Validate the Voter in the database
        List<Voter> voterList = voterRepository.findAll();
        assertThat(voterList).hasSize(databaseSizeBeforeUpdate);
        Voter testVoter = voterList.get(voterList.size() - 1);
        assertThat(testVoter.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testVoter.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testVoter.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testVoter.getActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testVoter.getComment()).isEqualTo(UPDATED_COMMENT);
    }

    @Test
    @Transactional
    void putNonExistingVoter() throws Exception {
        int databaseSizeBeforeUpdate = voterRepository.findAll().size();
        voter.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVoterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, voter.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(voter))
            )
            .andExpect(status().isBadRequest());

        // Validate the Voter in the database
        List<Voter> voterList = voterRepository.findAll();
        assertThat(voterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVoter() throws Exception {
        int databaseSizeBeforeUpdate = voterRepository.findAll().size();
        voter.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVoterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(voter))
            )
            .andExpect(status().isBadRequest());

        // Validate the Voter in the database
        List<Voter> voterList = voterRepository.findAll();
        assertThat(voterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVoter() throws Exception {
        int databaseSizeBeforeUpdate = voterRepository.findAll().size();
        voter.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVoterMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(voter)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Voter in the database
        List<Voter> voterList = voterRepository.findAll();
        assertThat(voterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVoterWithPatch() throws Exception {
        // Initialize the database
        voterRepository.saveAndFlush(voter);

        int databaseSizeBeforeUpdate = voterRepository.findAll().size();

        // Update the voter using partial update
        Voter partialUpdatedVoter = new Voter();
        partialUpdatedVoter.setId(voter.getId());

        partialUpdatedVoter.email(UPDATED_EMAIL);

        restVoterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVoter.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVoter))
            )
            .andExpect(status().isOk());

        // Validate the Voter in the database
        List<Voter> voterList = voterRepository.findAll();
        assertThat(voterList).hasSize(databaseSizeBeforeUpdate);
        Voter testVoter = voterList.get(voterList.size() - 1);
        assertThat(testVoter.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testVoter.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testVoter.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testVoter.getActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testVoter.getComment()).isEqualTo(DEFAULT_COMMENT);
    }

    @Test
    @Transactional
    void fullUpdateVoterWithPatch() throws Exception {
        // Initialize the database
        voterRepository.saveAndFlush(voter);

        int databaseSizeBeforeUpdate = voterRepository.findAll().size();

        // Update the voter using partial update
        Voter partialUpdatedVoter = new Voter();
        partialUpdatedVoter.setId(voter.getId());

        partialUpdatedVoter
            .email(UPDATED_EMAIL)
            .name(UPDATED_NAME)
            .created(UPDATED_CREATED)
            .active(UPDATED_ACTIVE)
            .comment(UPDATED_COMMENT);

        restVoterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVoter.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVoter))
            )
            .andExpect(status().isOk());

        // Validate the Voter in the database
        List<Voter> voterList = voterRepository.findAll();
        assertThat(voterList).hasSize(databaseSizeBeforeUpdate);
        Voter testVoter = voterList.get(voterList.size() - 1);
        assertThat(testVoter.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testVoter.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testVoter.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testVoter.getActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testVoter.getComment()).isEqualTo(UPDATED_COMMENT);
    }

    @Test
    @Transactional
    void patchNonExistingVoter() throws Exception {
        int databaseSizeBeforeUpdate = voterRepository.findAll().size();
        voter.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVoterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, voter.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(voter))
            )
            .andExpect(status().isBadRequest());

        // Validate the Voter in the database
        List<Voter> voterList = voterRepository.findAll();
        assertThat(voterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVoter() throws Exception {
        int databaseSizeBeforeUpdate = voterRepository.findAll().size();
        voter.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVoterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(voter))
            )
            .andExpect(status().isBadRequest());

        // Validate the Voter in the database
        List<Voter> voterList = voterRepository.findAll();
        assertThat(voterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVoter() throws Exception {
        int databaseSizeBeforeUpdate = voterRepository.findAll().size();
        voter.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVoterMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(voter)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Voter in the database
        List<Voter> voterList = voterRepository.findAll();
        assertThat(voterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVoter() throws Exception {
        // Initialize the database
        voterRepository.saveAndFlush(voter);

        int databaseSizeBeforeDelete = voterRepository.findAll().size();

        // Delete the voter
        restVoterMockMvc
            .perform(delete(ENTITY_API_URL_ID, voter.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Voter> voterList = voterRepository.findAll();
        assertThat(voterList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
