package dev.npass.upraiz.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import dev.npass.upraiz.IntegrationTest;
import dev.npass.upraiz.domain.VoteManagerPreferences;
import dev.npass.upraiz.domain.enumeration.VoteCcy;
import dev.npass.upraiz.repository.VoteManagerPreferencesRepository;
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
 * Integration tests for the {@link VoteManagerPreferencesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VoteManagerPreferencesResourceIT {

    private static final VoteCcy DEFAULT_PAY_CCY = VoteCcy.XNO;
    private static final VoteCcy UPDATED_PAY_CCY = VoteCcy.XNO;

    private static final String ENTITY_API_URL = "/api/vote-manager-preferences";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VoteManagerPreferencesRepository voteManagerPreferencesRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVoteManagerPreferencesMockMvc;

    private VoteManagerPreferences voteManagerPreferences;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VoteManagerPreferences createEntity(EntityManager em) {
        VoteManagerPreferences voteManagerPreferences = new VoteManagerPreferences().payCcy(DEFAULT_PAY_CCY);
        return voteManagerPreferences;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VoteManagerPreferences createUpdatedEntity(EntityManager em) {
        VoteManagerPreferences voteManagerPreferences = new VoteManagerPreferences().payCcy(UPDATED_PAY_CCY);
        return voteManagerPreferences;
    }

    @BeforeEach
    public void initTest() {
        voteManagerPreferences = createEntity(em);
    }

    @Test
    @Transactional
    void createVoteManagerPreferences() throws Exception {
        int databaseSizeBeforeCreate = voteManagerPreferencesRepository.findAll().size();
        // Create the VoteManagerPreferences
        restVoteManagerPreferencesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(voteManagerPreferences))
            )
            .andExpect(status().isCreated());

        // Validate the VoteManagerPreferences in the database
        List<VoteManagerPreferences> voteManagerPreferencesList = voteManagerPreferencesRepository.findAll();
        assertThat(voteManagerPreferencesList).hasSize(databaseSizeBeforeCreate + 1);
        VoteManagerPreferences testVoteManagerPreferences = voteManagerPreferencesList.get(voteManagerPreferencesList.size() - 1);
        assertThat(testVoteManagerPreferences.getPayCcy()).isEqualTo(DEFAULT_PAY_CCY);
    }

    @Test
    @Transactional
    void createVoteManagerPreferencesWithExistingId() throws Exception {
        // Create the VoteManagerPreferences with an existing ID
        voteManagerPreferences.setId(1L);

        int databaseSizeBeforeCreate = voteManagerPreferencesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVoteManagerPreferencesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(voteManagerPreferences))
            )
            .andExpect(status().isBadRequest());

        // Validate the VoteManagerPreferences in the database
        List<VoteManagerPreferences> voteManagerPreferencesList = voteManagerPreferencesRepository.findAll();
        assertThat(voteManagerPreferencesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllVoteManagerPreferences() throws Exception {
        // Initialize the database
        voteManagerPreferencesRepository.saveAndFlush(voteManagerPreferences);

        // Get all the voteManagerPreferencesList
        restVoteManagerPreferencesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(voteManagerPreferences.getId().intValue())))
            .andExpect(jsonPath("$.[*].payCcy").value(hasItem(DEFAULT_PAY_CCY.toString())));
    }

    @Test
    @Transactional
    void getVoteManagerPreferences() throws Exception {
        // Initialize the database
        voteManagerPreferencesRepository.saveAndFlush(voteManagerPreferences);

        // Get the voteManagerPreferences
        restVoteManagerPreferencesMockMvc
            .perform(get(ENTITY_API_URL_ID, voteManagerPreferences.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(voteManagerPreferences.getId().intValue()))
            .andExpect(jsonPath("$.payCcy").value(DEFAULT_PAY_CCY.toString()));
    }

    @Test
    @Transactional
    void getNonExistingVoteManagerPreferences() throws Exception {
        // Get the voteManagerPreferences
        restVoteManagerPreferencesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingVoteManagerPreferences() throws Exception {
        // Initialize the database
        voteManagerPreferencesRepository.saveAndFlush(voteManagerPreferences);

        int databaseSizeBeforeUpdate = voteManagerPreferencesRepository.findAll().size();

        // Update the voteManagerPreferences
        VoteManagerPreferences updatedVoteManagerPreferences = voteManagerPreferencesRepository
            .findById(voteManagerPreferences.getId())
            .get();
        // Disconnect from session so that the updates on updatedVoteManagerPreferences are not directly saved in db
        em.detach(updatedVoteManagerPreferences);
        updatedVoteManagerPreferences.payCcy(UPDATED_PAY_CCY);

        restVoteManagerPreferencesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedVoteManagerPreferences.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedVoteManagerPreferences))
            )
            .andExpect(status().isOk());

        // Validate the VoteManagerPreferences in the database
        List<VoteManagerPreferences> voteManagerPreferencesList = voteManagerPreferencesRepository.findAll();
        assertThat(voteManagerPreferencesList).hasSize(databaseSizeBeforeUpdate);
        VoteManagerPreferences testVoteManagerPreferences = voteManagerPreferencesList.get(voteManagerPreferencesList.size() - 1);
        assertThat(testVoteManagerPreferences.getPayCcy()).isEqualTo(UPDATED_PAY_CCY);
    }

    @Test
    @Transactional
    void putNonExistingVoteManagerPreferences() throws Exception {
        int databaseSizeBeforeUpdate = voteManagerPreferencesRepository.findAll().size();
        voteManagerPreferences.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVoteManagerPreferencesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, voteManagerPreferences.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(voteManagerPreferences))
            )
            .andExpect(status().isBadRequest());

        // Validate the VoteManagerPreferences in the database
        List<VoteManagerPreferences> voteManagerPreferencesList = voteManagerPreferencesRepository.findAll();
        assertThat(voteManagerPreferencesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVoteManagerPreferences() throws Exception {
        int databaseSizeBeforeUpdate = voteManagerPreferencesRepository.findAll().size();
        voteManagerPreferences.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVoteManagerPreferencesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(voteManagerPreferences))
            )
            .andExpect(status().isBadRequest());

        // Validate the VoteManagerPreferences in the database
        List<VoteManagerPreferences> voteManagerPreferencesList = voteManagerPreferencesRepository.findAll();
        assertThat(voteManagerPreferencesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVoteManagerPreferences() throws Exception {
        int databaseSizeBeforeUpdate = voteManagerPreferencesRepository.findAll().size();
        voteManagerPreferences.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVoteManagerPreferencesMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(voteManagerPreferences))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the VoteManagerPreferences in the database
        List<VoteManagerPreferences> voteManagerPreferencesList = voteManagerPreferencesRepository.findAll();
        assertThat(voteManagerPreferencesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVoteManagerPreferencesWithPatch() throws Exception {
        // Initialize the database
        voteManagerPreferencesRepository.saveAndFlush(voteManagerPreferences);

        int databaseSizeBeforeUpdate = voteManagerPreferencesRepository.findAll().size();

        // Update the voteManagerPreferences using partial update
        VoteManagerPreferences partialUpdatedVoteManagerPreferences = new VoteManagerPreferences();
        partialUpdatedVoteManagerPreferences.setId(voteManagerPreferences.getId());

        restVoteManagerPreferencesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVoteManagerPreferences.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVoteManagerPreferences))
            )
            .andExpect(status().isOk());

        // Validate the VoteManagerPreferences in the database
        List<VoteManagerPreferences> voteManagerPreferencesList = voteManagerPreferencesRepository.findAll();
        assertThat(voteManagerPreferencesList).hasSize(databaseSizeBeforeUpdate);
        VoteManagerPreferences testVoteManagerPreferences = voteManagerPreferencesList.get(voteManagerPreferencesList.size() - 1);
        assertThat(testVoteManagerPreferences.getPayCcy()).isEqualTo(DEFAULT_PAY_CCY);
    }

    @Test
    @Transactional
    void fullUpdateVoteManagerPreferencesWithPatch() throws Exception {
        // Initialize the database
        voteManagerPreferencesRepository.saveAndFlush(voteManagerPreferences);

        int databaseSizeBeforeUpdate = voteManagerPreferencesRepository.findAll().size();

        // Update the voteManagerPreferences using partial update
        VoteManagerPreferences partialUpdatedVoteManagerPreferences = new VoteManagerPreferences();
        partialUpdatedVoteManagerPreferences.setId(voteManagerPreferences.getId());

        partialUpdatedVoteManagerPreferences.payCcy(UPDATED_PAY_CCY);

        restVoteManagerPreferencesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVoteManagerPreferences.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVoteManagerPreferences))
            )
            .andExpect(status().isOk());

        // Validate the VoteManagerPreferences in the database
        List<VoteManagerPreferences> voteManagerPreferencesList = voteManagerPreferencesRepository.findAll();
        assertThat(voteManagerPreferencesList).hasSize(databaseSizeBeforeUpdate);
        VoteManagerPreferences testVoteManagerPreferences = voteManagerPreferencesList.get(voteManagerPreferencesList.size() - 1);
        assertThat(testVoteManagerPreferences.getPayCcy()).isEqualTo(UPDATED_PAY_CCY);
    }

    @Test
    @Transactional
    void patchNonExistingVoteManagerPreferences() throws Exception {
        int databaseSizeBeforeUpdate = voteManagerPreferencesRepository.findAll().size();
        voteManagerPreferences.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVoteManagerPreferencesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, voteManagerPreferences.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(voteManagerPreferences))
            )
            .andExpect(status().isBadRequest());

        // Validate the VoteManagerPreferences in the database
        List<VoteManagerPreferences> voteManagerPreferencesList = voteManagerPreferencesRepository.findAll();
        assertThat(voteManagerPreferencesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVoteManagerPreferences() throws Exception {
        int databaseSizeBeforeUpdate = voteManagerPreferencesRepository.findAll().size();
        voteManagerPreferences.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVoteManagerPreferencesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(voteManagerPreferences))
            )
            .andExpect(status().isBadRequest());

        // Validate the VoteManagerPreferences in the database
        List<VoteManagerPreferences> voteManagerPreferencesList = voteManagerPreferencesRepository.findAll();
        assertThat(voteManagerPreferencesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVoteManagerPreferences() throws Exception {
        int databaseSizeBeforeUpdate = voteManagerPreferencesRepository.findAll().size();
        voteManagerPreferences.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVoteManagerPreferencesMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(voteManagerPreferences))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the VoteManagerPreferences in the database
        List<VoteManagerPreferences> voteManagerPreferencesList = voteManagerPreferencesRepository.findAll();
        assertThat(voteManagerPreferencesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVoteManagerPreferences() throws Exception {
        // Initialize the database
        voteManagerPreferencesRepository.saveAndFlush(voteManagerPreferences);

        int databaseSizeBeforeDelete = voteManagerPreferencesRepository.findAll().size();

        // Delete the voteManagerPreferences
        restVoteManagerPreferencesMockMvc
            .perform(delete(ENTITY_API_URL_ID, voteManagerPreferences.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<VoteManagerPreferences> voteManagerPreferencesList = voteManagerPreferencesRepository.findAll();
        assertThat(voteManagerPreferencesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
