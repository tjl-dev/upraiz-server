package dev.npass.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import dev.npass.IntegrationTest;
import dev.npass.domain.VoterPreferences;
import dev.npass.domain.enumeration.VoteCcy;
import dev.npass.repository.VoterPreferencesRepository;
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
 * Integration tests for the {@link VoterPreferencesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VoterPreferencesResourceIT {

    private static final VoteCcy DEFAULT_RECEIVE_CCY = VoteCcy.XNO;
    private static final VoteCcy UPDATED_RECEIVE_CCY = VoteCcy.XNO;

    private static final String ENTITY_API_URL = "/api/voter-preferences";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VoterPreferencesRepository voterPreferencesRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVoterPreferencesMockMvc;

    private VoterPreferences voterPreferences;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VoterPreferences createEntity(EntityManager em) {
        VoterPreferences voterPreferences = new VoterPreferences().receiveCcy(DEFAULT_RECEIVE_CCY);
        return voterPreferences;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VoterPreferences createUpdatedEntity(EntityManager em) {
        VoterPreferences voterPreferences = new VoterPreferences().receiveCcy(UPDATED_RECEIVE_CCY);
        return voterPreferences;
    }

    @BeforeEach
    public void initTest() {
        voterPreferences = createEntity(em);
    }

    @Test
    @Transactional
    void createVoterPreferences() throws Exception {
        int databaseSizeBeforeCreate = voterPreferencesRepository.findAll().size();
        // Create the VoterPreferences
        restVoterPreferencesMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(voterPreferences))
            )
            .andExpect(status().isCreated());

        // Validate the VoterPreferences in the database
        List<VoterPreferences> voterPreferencesList = voterPreferencesRepository.findAll();
        assertThat(voterPreferencesList).hasSize(databaseSizeBeforeCreate + 1);
        VoterPreferences testVoterPreferences = voterPreferencesList.get(voterPreferencesList.size() - 1);
        assertThat(testVoterPreferences.getReceiveCcy()).isEqualTo(DEFAULT_RECEIVE_CCY);
    }

    @Test
    @Transactional
    void createVoterPreferencesWithExistingId() throws Exception {
        // Create the VoterPreferences with an existing ID
        voterPreferences.setId(1L);

        int databaseSizeBeforeCreate = voterPreferencesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVoterPreferencesMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(voterPreferences))
            )
            .andExpect(status().isBadRequest());

        // Validate the VoterPreferences in the database
        List<VoterPreferences> voterPreferencesList = voterPreferencesRepository.findAll();
        assertThat(voterPreferencesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllVoterPreferences() throws Exception {
        // Initialize the database
        voterPreferencesRepository.saveAndFlush(voterPreferences);

        // Get all the voterPreferencesList
        restVoterPreferencesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(voterPreferences.getId().intValue())))
            .andExpect(jsonPath("$.[*].receiveCcy").value(hasItem(DEFAULT_RECEIVE_CCY.toString())));
    }

    @Test
    @Transactional
    void getVoterPreferences() throws Exception {
        // Initialize the database
        voterPreferencesRepository.saveAndFlush(voterPreferences);

        // Get the voterPreferences
        restVoterPreferencesMockMvc
            .perform(get(ENTITY_API_URL_ID, voterPreferences.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(voterPreferences.getId().intValue()))
            .andExpect(jsonPath("$.receiveCcy").value(DEFAULT_RECEIVE_CCY.toString()));
    }

    @Test
    @Transactional
    void getNonExistingVoterPreferences() throws Exception {
        // Get the voterPreferences
        restVoterPreferencesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingVoterPreferences() throws Exception {
        // Initialize the database
        voterPreferencesRepository.saveAndFlush(voterPreferences);

        int databaseSizeBeforeUpdate = voterPreferencesRepository.findAll().size();

        // Update the voterPreferences
        VoterPreferences updatedVoterPreferences = voterPreferencesRepository.findById(voterPreferences.getId()).get();
        // Disconnect from session so that the updates on updatedVoterPreferences are not directly saved in db
        em.detach(updatedVoterPreferences);
        updatedVoterPreferences.receiveCcy(UPDATED_RECEIVE_CCY);

        restVoterPreferencesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedVoterPreferences.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedVoterPreferences))
            )
            .andExpect(status().isOk());

        // Validate the VoterPreferences in the database
        List<VoterPreferences> voterPreferencesList = voterPreferencesRepository.findAll();
        assertThat(voterPreferencesList).hasSize(databaseSizeBeforeUpdate);
        VoterPreferences testVoterPreferences = voterPreferencesList.get(voterPreferencesList.size() - 1);
        assertThat(testVoterPreferences.getReceiveCcy()).isEqualTo(UPDATED_RECEIVE_CCY);
    }

    @Test
    @Transactional
    void putNonExistingVoterPreferences() throws Exception {
        int databaseSizeBeforeUpdate = voterPreferencesRepository.findAll().size();
        voterPreferences.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVoterPreferencesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, voterPreferences.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(voterPreferences))
            )
            .andExpect(status().isBadRequest());

        // Validate the VoterPreferences in the database
        List<VoterPreferences> voterPreferencesList = voterPreferencesRepository.findAll();
        assertThat(voterPreferencesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVoterPreferences() throws Exception {
        int databaseSizeBeforeUpdate = voterPreferencesRepository.findAll().size();
        voterPreferences.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVoterPreferencesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(voterPreferences))
            )
            .andExpect(status().isBadRequest());

        // Validate the VoterPreferences in the database
        List<VoterPreferences> voterPreferencesList = voterPreferencesRepository.findAll();
        assertThat(voterPreferencesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVoterPreferences() throws Exception {
        int databaseSizeBeforeUpdate = voterPreferencesRepository.findAll().size();
        voterPreferences.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVoterPreferencesMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(voterPreferences))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the VoterPreferences in the database
        List<VoterPreferences> voterPreferencesList = voterPreferencesRepository.findAll();
        assertThat(voterPreferencesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVoterPreferencesWithPatch() throws Exception {
        // Initialize the database
        voterPreferencesRepository.saveAndFlush(voterPreferences);

        int databaseSizeBeforeUpdate = voterPreferencesRepository.findAll().size();

        // Update the voterPreferences using partial update
        VoterPreferences partialUpdatedVoterPreferences = new VoterPreferences();
        partialUpdatedVoterPreferences.setId(voterPreferences.getId());

        restVoterPreferencesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVoterPreferences.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVoterPreferences))
            )
            .andExpect(status().isOk());

        // Validate the VoterPreferences in the database
        List<VoterPreferences> voterPreferencesList = voterPreferencesRepository.findAll();
        assertThat(voterPreferencesList).hasSize(databaseSizeBeforeUpdate);
        VoterPreferences testVoterPreferences = voterPreferencesList.get(voterPreferencesList.size() - 1);
        assertThat(testVoterPreferences.getReceiveCcy()).isEqualTo(DEFAULT_RECEIVE_CCY);
    }

    @Test
    @Transactional
    void fullUpdateVoterPreferencesWithPatch() throws Exception {
        // Initialize the database
        voterPreferencesRepository.saveAndFlush(voterPreferences);

        int databaseSizeBeforeUpdate = voterPreferencesRepository.findAll().size();

        // Update the voterPreferences using partial update
        VoterPreferences partialUpdatedVoterPreferences = new VoterPreferences();
        partialUpdatedVoterPreferences.setId(voterPreferences.getId());

        partialUpdatedVoterPreferences.receiveCcy(UPDATED_RECEIVE_CCY);

        restVoterPreferencesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVoterPreferences.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVoterPreferences))
            )
            .andExpect(status().isOk());

        // Validate the VoterPreferences in the database
        List<VoterPreferences> voterPreferencesList = voterPreferencesRepository.findAll();
        assertThat(voterPreferencesList).hasSize(databaseSizeBeforeUpdate);
        VoterPreferences testVoterPreferences = voterPreferencesList.get(voterPreferencesList.size() - 1);
        assertThat(testVoterPreferences.getReceiveCcy()).isEqualTo(UPDATED_RECEIVE_CCY);
    }

    @Test
    @Transactional
    void patchNonExistingVoterPreferences() throws Exception {
        int databaseSizeBeforeUpdate = voterPreferencesRepository.findAll().size();
        voterPreferences.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVoterPreferencesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, voterPreferences.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(voterPreferences))
            )
            .andExpect(status().isBadRequest());

        // Validate the VoterPreferences in the database
        List<VoterPreferences> voterPreferencesList = voterPreferencesRepository.findAll();
        assertThat(voterPreferencesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVoterPreferences() throws Exception {
        int databaseSizeBeforeUpdate = voterPreferencesRepository.findAll().size();
        voterPreferences.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVoterPreferencesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(voterPreferences))
            )
            .andExpect(status().isBadRequest());

        // Validate the VoterPreferences in the database
        List<VoterPreferences> voterPreferencesList = voterPreferencesRepository.findAll();
        assertThat(voterPreferencesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVoterPreferences() throws Exception {
        int databaseSizeBeforeUpdate = voterPreferencesRepository.findAll().size();
        voterPreferences.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVoterPreferencesMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(voterPreferences))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the VoterPreferences in the database
        List<VoterPreferences> voterPreferencesList = voterPreferencesRepository.findAll();
        assertThat(voterPreferencesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVoterPreferences() throws Exception {
        // Initialize the database
        voterPreferencesRepository.saveAndFlush(voterPreferences);

        int databaseSizeBeforeDelete = voterPreferencesRepository.findAll().size();

        // Delete the voterPreferences
        restVoterPreferencesMockMvc
            .perform(delete(ENTITY_API_URL_ID, voterPreferences.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<VoterPreferences> voterPreferencesList = voterPreferencesRepository.findAll();
        assertThat(voterPreferencesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
