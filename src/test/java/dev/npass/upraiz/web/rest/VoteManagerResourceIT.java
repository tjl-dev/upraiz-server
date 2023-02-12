package dev.npass.upraiz.web.rest;

import static dev.npass.upraiz.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import dev.npass.upraiz.IntegrationTest;
import dev.npass.upraiz.domain.VoteManager;
import dev.npass.upraiz.repository.VoteManagerRepository;
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
 * Integration tests for the {@link VoteManagerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VoteManagerResourceIT {

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

    private static final String ENTITY_API_URL = "/api/vote-managers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VoteManagerRepository voteManagerRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVoteManagerMockMvc;

    private VoteManager voteManager;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VoteManager createEntity(EntityManager em) {
        VoteManager voteManager = new VoteManager()
            .email(DEFAULT_EMAIL)
            .name(DEFAULT_NAME)
            .created(DEFAULT_CREATED)
            .active(DEFAULT_ACTIVE)
            .comment(DEFAULT_COMMENT);
        return voteManager;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VoteManager createUpdatedEntity(EntityManager em) {
        VoteManager voteManager = new VoteManager()
            .email(UPDATED_EMAIL)
            .name(UPDATED_NAME)
            .created(UPDATED_CREATED)
            .active(UPDATED_ACTIVE)
            .comment(UPDATED_COMMENT);
        return voteManager;
    }

    @BeforeEach
    public void initTest() {
        voteManager = createEntity(em);
    }

    @Test
    @Transactional
    void createVoteManager() throws Exception {
        int databaseSizeBeforeCreate = voteManagerRepository.findAll().size();
        // Create the VoteManager
        restVoteManagerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(voteManager)))
            .andExpect(status().isCreated());

        // Validate the VoteManager in the database
        List<VoteManager> voteManagerList = voteManagerRepository.findAll();
        assertThat(voteManagerList).hasSize(databaseSizeBeforeCreate + 1);
        VoteManager testVoteManager = voteManagerList.get(voteManagerList.size() - 1);
        assertThat(testVoteManager.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testVoteManager.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testVoteManager.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testVoteManager.getActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testVoteManager.getComment()).isEqualTo(DEFAULT_COMMENT);
    }

    @Test
    @Transactional
    void createVoteManagerWithExistingId() throws Exception {
        // Create the VoteManager with an existing ID
        voteManager.setId(1L);

        int databaseSizeBeforeCreate = voteManagerRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVoteManagerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(voteManager)))
            .andExpect(status().isBadRequest());

        // Validate the VoteManager in the database
        List<VoteManager> voteManagerList = voteManagerRepository.findAll();
        assertThat(voteManagerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = voteManagerRepository.findAll().size();
        // set the field null
        voteManager.setEmail(null);

        // Create the VoteManager, which fails.

        restVoteManagerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(voteManager)))
            .andExpect(status().isBadRequest());

        List<VoteManager> voteManagerList = voteManagerRepository.findAll();
        assertThat(voteManagerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = voteManagerRepository.findAll().size();
        // set the field null
        voteManager.setName(null);

        // Create the VoteManager, which fails.

        restVoteManagerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(voteManager)))
            .andExpect(status().isBadRequest());

        List<VoteManager> voteManagerList = voteManagerRepository.findAll();
        assertThat(voteManagerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllVoteManagers() throws Exception {
        // Initialize the database
        voteManagerRepository.saveAndFlush(voteManager);

        // Get all the voteManagerList
        restVoteManagerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(voteManager.getId().intValue())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].created").value(hasItem(sameInstant(DEFAULT_CREATED))))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT)));
    }

    @Test
    @Transactional
    void getVoteManager() throws Exception {
        // Initialize the database
        voteManagerRepository.saveAndFlush(voteManager);

        // Get the voteManager
        restVoteManagerMockMvc
            .perform(get(ENTITY_API_URL_ID, voteManager.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(voteManager.getId().intValue()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.created").value(sameInstant(DEFAULT_CREATED)))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT));
    }

    @Test
    @Transactional
    void getNonExistingVoteManager() throws Exception {
        // Get the voteManager
        restVoteManagerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingVoteManager() throws Exception {
        // Initialize the database
        voteManagerRepository.saveAndFlush(voteManager);

        int databaseSizeBeforeUpdate = voteManagerRepository.findAll().size();

        // Update the voteManager
        VoteManager updatedVoteManager = voteManagerRepository.findById(voteManager.getId()).get();
        // Disconnect from session so that the updates on updatedVoteManager are not directly saved in db
        em.detach(updatedVoteManager);
        updatedVoteManager.email(UPDATED_EMAIL).name(UPDATED_NAME).created(UPDATED_CREATED).active(UPDATED_ACTIVE).comment(UPDATED_COMMENT);

        restVoteManagerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedVoteManager.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedVoteManager))
            )
            .andExpect(status().isOk());

        // Validate the VoteManager in the database
        List<VoteManager> voteManagerList = voteManagerRepository.findAll();
        assertThat(voteManagerList).hasSize(databaseSizeBeforeUpdate);
        VoteManager testVoteManager = voteManagerList.get(voteManagerList.size() - 1);
        assertThat(testVoteManager.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testVoteManager.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testVoteManager.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testVoteManager.getActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testVoteManager.getComment()).isEqualTo(UPDATED_COMMENT);
    }

    @Test
    @Transactional
    void putNonExistingVoteManager() throws Exception {
        int databaseSizeBeforeUpdate = voteManagerRepository.findAll().size();
        voteManager.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVoteManagerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, voteManager.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(voteManager))
            )
            .andExpect(status().isBadRequest());

        // Validate the VoteManager in the database
        List<VoteManager> voteManagerList = voteManagerRepository.findAll();
        assertThat(voteManagerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVoteManager() throws Exception {
        int databaseSizeBeforeUpdate = voteManagerRepository.findAll().size();
        voteManager.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVoteManagerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(voteManager))
            )
            .andExpect(status().isBadRequest());

        // Validate the VoteManager in the database
        List<VoteManager> voteManagerList = voteManagerRepository.findAll();
        assertThat(voteManagerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVoteManager() throws Exception {
        int databaseSizeBeforeUpdate = voteManagerRepository.findAll().size();
        voteManager.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVoteManagerMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(voteManager)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the VoteManager in the database
        List<VoteManager> voteManagerList = voteManagerRepository.findAll();
        assertThat(voteManagerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVoteManagerWithPatch() throws Exception {
        // Initialize the database
        voteManagerRepository.saveAndFlush(voteManager);

        int databaseSizeBeforeUpdate = voteManagerRepository.findAll().size();

        // Update the voteManager using partial update
        VoteManager partialUpdatedVoteManager = new VoteManager();
        partialUpdatedVoteManager.setId(voteManager.getId());

        partialUpdatedVoteManager.created(UPDATED_CREATED).active(UPDATED_ACTIVE);

        restVoteManagerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVoteManager.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVoteManager))
            )
            .andExpect(status().isOk());

        // Validate the VoteManager in the database
        List<VoteManager> voteManagerList = voteManagerRepository.findAll();
        assertThat(voteManagerList).hasSize(databaseSizeBeforeUpdate);
        VoteManager testVoteManager = voteManagerList.get(voteManagerList.size() - 1);
        assertThat(testVoteManager.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testVoteManager.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testVoteManager.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testVoteManager.getActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testVoteManager.getComment()).isEqualTo(DEFAULT_COMMENT);
    }

    @Test
    @Transactional
    void fullUpdateVoteManagerWithPatch() throws Exception {
        // Initialize the database
        voteManagerRepository.saveAndFlush(voteManager);

        int databaseSizeBeforeUpdate = voteManagerRepository.findAll().size();

        // Update the voteManager using partial update
        VoteManager partialUpdatedVoteManager = new VoteManager();
        partialUpdatedVoteManager.setId(voteManager.getId());

        partialUpdatedVoteManager
            .email(UPDATED_EMAIL)
            .name(UPDATED_NAME)
            .created(UPDATED_CREATED)
            .active(UPDATED_ACTIVE)
            .comment(UPDATED_COMMENT);

        restVoteManagerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVoteManager.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVoteManager))
            )
            .andExpect(status().isOk());

        // Validate the VoteManager in the database
        List<VoteManager> voteManagerList = voteManagerRepository.findAll();
        assertThat(voteManagerList).hasSize(databaseSizeBeforeUpdate);
        VoteManager testVoteManager = voteManagerList.get(voteManagerList.size() - 1);
        assertThat(testVoteManager.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testVoteManager.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testVoteManager.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testVoteManager.getActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testVoteManager.getComment()).isEqualTo(UPDATED_COMMENT);
    }

    @Test
    @Transactional
    void patchNonExistingVoteManager() throws Exception {
        int databaseSizeBeforeUpdate = voteManagerRepository.findAll().size();
        voteManager.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVoteManagerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, voteManager.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(voteManager))
            )
            .andExpect(status().isBadRequest());

        // Validate the VoteManager in the database
        List<VoteManager> voteManagerList = voteManagerRepository.findAll();
        assertThat(voteManagerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVoteManager() throws Exception {
        int databaseSizeBeforeUpdate = voteManagerRepository.findAll().size();
        voteManager.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVoteManagerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(voteManager))
            )
            .andExpect(status().isBadRequest());

        // Validate the VoteManager in the database
        List<VoteManager> voteManagerList = voteManagerRepository.findAll();
        assertThat(voteManagerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVoteManager() throws Exception {
        int databaseSizeBeforeUpdate = voteManagerRepository.findAll().size();
        voteManager.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVoteManagerMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(voteManager))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the VoteManager in the database
        List<VoteManager> voteManagerList = voteManagerRepository.findAll();
        assertThat(voteManagerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVoteManager() throws Exception {
        // Initialize the database
        voteManagerRepository.saveAndFlush(voteManager);

        int databaseSizeBeforeDelete = voteManagerRepository.findAll().size();

        // Delete the voteManager
        restVoteManagerMockMvc
            .perform(delete(ENTITY_API_URL_ID, voteManager.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<VoteManager> voteManagerList = voteManagerRepository.findAll();
        assertThat(voteManagerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
