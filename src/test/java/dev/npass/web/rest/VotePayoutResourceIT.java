package dev.npass.web.rest;

import static dev.npass.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import dev.npass.IntegrationTest;
import dev.npass.domain.VotePayout;
import dev.npass.domain.enumeration.VoteCcy;
import dev.npass.repository.VotePayoutRepository;
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
 * Integration tests for the {@link VotePayoutResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VotePayoutResourceIT {

    private static final ZonedDateTime DEFAULT_PAID_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_PAID_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Double DEFAULT_PAID_AMOUNT = 1D;
    private static final Double UPDATED_PAID_AMOUNT = 2D;

    private static final VoteCcy DEFAULT_PAID_CCY = VoteCcy.XNO;
    private static final VoteCcy UPDATED_PAID_CCY = VoteCcy.XNO;

    private static final String ENTITY_API_URL = "/api/vote-payouts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VotePayoutRepository votePayoutRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVotePayoutMockMvc;

    private VotePayout votePayout;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VotePayout createEntity(EntityManager em) {
        VotePayout votePayout = new VotePayout().paidTime(DEFAULT_PAID_TIME).paidAmount(DEFAULT_PAID_AMOUNT).paidCcy(DEFAULT_PAID_CCY);
        return votePayout;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VotePayout createUpdatedEntity(EntityManager em) {
        VotePayout votePayout = new VotePayout().paidTime(UPDATED_PAID_TIME).paidAmount(UPDATED_PAID_AMOUNT).paidCcy(UPDATED_PAID_CCY);
        return votePayout;
    }

    @BeforeEach
    public void initTest() {
        votePayout = createEntity(em);
    }

    @Test
    @Transactional
    void createVotePayout() throws Exception {
        int databaseSizeBeforeCreate = votePayoutRepository.findAll().size();
        // Create the VotePayout
        restVotePayoutMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(votePayout)))
            .andExpect(status().isCreated());

        // Validate the VotePayout in the database
        List<VotePayout> votePayoutList = votePayoutRepository.findAll();
        assertThat(votePayoutList).hasSize(databaseSizeBeforeCreate + 1);
        VotePayout testVotePayout = votePayoutList.get(votePayoutList.size() - 1);
        assertThat(testVotePayout.getPaidTime()).isEqualTo(DEFAULT_PAID_TIME);
        assertThat(testVotePayout.getPaidAmount()).isEqualTo(DEFAULT_PAID_AMOUNT);
        assertThat(testVotePayout.getPaidCcy()).isEqualTo(DEFAULT_PAID_CCY);
    }

    @Test
    @Transactional
    void createVotePayoutWithExistingId() throws Exception {
        // Create the VotePayout with an existing ID
        votePayout.setId(1L);

        int databaseSizeBeforeCreate = votePayoutRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVotePayoutMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(votePayout)))
            .andExpect(status().isBadRequest());

        // Validate the VotePayout in the database
        List<VotePayout> votePayoutList = votePayoutRepository.findAll();
        assertThat(votePayoutList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPaidTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = votePayoutRepository.findAll().size();
        // set the field null
        votePayout.setPaidTime(null);

        // Create the VotePayout, which fails.

        restVotePayoutMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(votePayout)))
            .andExpect(status().isBadRequest());

        List<VotePayout> votePayoutList = votePayoutRepository.findAll();
        assertThat(votePayoutList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPaidAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = votePayoutRepository.findAll().size();
        // set the field null
        votePayout.setPaidAmount(null);

        // Create the VotePayout, which fails.

        restVotePayoutMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(votePayout)))
            .andExpect(status().isBadRequest());

        List<VotePayout> votePayoutList = votePayoutRepository.findAll();
        assertThat(votePayoutList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPaidCcyIsRequired() throws Exception {
        int databaseSizeBeforeTest = votePayoutRepository.findAll().size();
        // set the field null
        votePayout.setPaidCcy(null);

        // Create the VotePayout, which fails.

        restVotePayoutMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(votePayout)))
            .andExpect(status().isBadRequest());

        List<VotePayout> votePayoutList = votePayoutRepository.findAll();
        assertThat(votePayoutList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllVotePayouts() throws Exception {
        // Initialize the database
        votePayoutRepository.saveAndFlush(votePayout);

        // Get all the votePayoutList
        restVotePayoutMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(votePayout.getId().intValue())))
            .andExpect(jsonPath("$.[*].paidTime").value(hasItem(sameInstant(DEFAULT_PAID_TIME))))
            .andExpect(jsonPath("$.[*].paidAmount").value(hasItem(DEFAULT_PAID_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].paidCcy").value(hasItem(DEFAULT_PAID_CCY.toString())));
    }

    @Test
    @Transactional
    void getVotePayout() throws Exception {
        // Initialize the database
        votePayoutRepository.saveAndFlush(votePayout);

        // Get the votePayout
        restVotePayoutMockMvc
            .perform(get(ENTITY_API_URL_ID, votePayout.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(votePayout.getId().intValue()))
            .andExpect(jsonPath("$.paidTime").value(sameInstant(DEFAULT_PAID_TIME)))
            .andExpect(jsonPath("$.paidAmount").value(DEFAULT_PAID_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.paidCcy").value(DEFAULT_PAID_CCY.toString()));
    }

    @Test
    @Transactional
    void getNonExistingVotePayout() throws Exception {
        // Get the votePayout
        restVotePayoutMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingVotePayout() throws Exception {
        // Initialize the database
        votePayoutRepository.saveAndFlush(votePayout);

        int databaseSizeBeforeUpdate = votePayoutRepository.findAll().size();

        // Update the votePayout
        VotePayout updatedVotePayout = votePayoutRepository.findById(votePayout.getId()).get();
        // Disconnect from session so that the updates on updatedVotePayout are not directly saved in db
        em.detach(updatedVotePayout);
        updatedVotePayout.paidTime(UPDATED_PAID_TIME).paidAmount(UPDATED_PAID_AMOUNT).paidCcy(UPDATED_PAID_CCY);

        restVotePayoutMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedVotePayout.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedVotePayout))
            )
            .andExpect(status().isOk());

        // Validate the VotePayout in the database
        List<VotePayout> votePayoutList = votePayoutRepository.findAll();
        assertThat(votePayoutList).hasSize(databaseSizeBeforeUpdate);
        VotePayout testVotePayout = votePayoutList.get(votePayoutList.size() - 1);
        assertThat(testVotePayout.getPaidTime()).isEqualTo(UPDATED_PAID_TIME);
        assertThat(testVotePayout.getPaidAmount()).isEqualTo(UPDATED_PAID_AMOUNT);
        assertThat(testVotePayout.getPaidCcy()).isEqualTo(UPDATED_PAID_CCY);
    }

    @Test
    @Transactional
    void putNonExistingVotePayout() throws Exception {
        int databaseSizeBeforeUpdate = votePayoutRepository.findAll().size();
        votePayout.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVotePayoutMockMvc
            .perform(
                put(ENTITY_API_URL_ID, votePayout.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(votePayout))
            )
            .andExpect(status().isBadRequest());

        // Validate the VotePayout in the database
        List<VotePayout> votePayoutList = votePayoutRepository.findAll();
        assertThat(votePayoutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVotePayout() throws Exception {
        int databaseSizeBeforeUpdate = votePayoutRepository.findAll().size();
        votePayout.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVotePayoutMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(votePayout))
            )
            .andExpect(status().isBadRequest());

        // Validate the VotePayout in the database
        List<VotePayout> votePayoutList = votePayoutRepository.findAll();
        assertThat(votePayoutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVotePayout() throws Exception {
        int databaseSizeBeforeUpdate = votePayoutRepository.findAll().size();
        votePayout.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVotePayoutMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(votePayout)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the VotePayout in the database
        List<VotePayout> votePayoutList = votePayoutRepository.findAll();
        assertThat(votePayoutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVotePayoutWithPatch() throws Exception {
        // Initialize the database
        votePayoutRepository.saveAndFlush(votePayout);

        int databaseSizeBeforeUpdate = votePayoutRepository.findAll().size();

        // Update the votePayout using partial update
        VotePayout partialUpdatedVotePayout = new VotePayout();
        partialUpdatedVotePayout.setId(votePayout.getId());

        restVotePayoutMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVotePayout.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVotePayout))
            )
            .andExpect(status().isOk());

        // Validate the VotePayout in the database
        List<VotePayout> votePayoutList = votePayoutRepository.findAll();
        assertThat(votePayoutList).hasSize(databaseSizeBeforeUpdate);
        VotePayout testVotePayout = votePayoutList.get(votePayoutList.size() - 1);
        assertThat(testVotePayout.getPaidTime()).isEqualTo(DEFAULT_PAID_TIME);
        assertThat(testVotePayout.getPaidAmount()).isEqualTo(DEFAULT_PAID_AMOUNT);
        assertThat(testVotePayout.getPaidCcy()).isEqualTo(DEFAULT_PAID_CCY);
    }

    @Test
    @Transactional
    void fullUpdateVotePayoutWithPatch() throws Exception {
        // Initialize the database
        votePayoutRepository.saveAndFlush(votePayout);

        int databaseSizeBeforeUpdate = votePayoutRepository.findAll().size();

        // Update the votePayout using partial update
        VotePayout partialUpdatedVotePayout = new VotePayout();
        partialUpdatedVotePayout.setId(votePayout.getId());

        partialUpdatedVotePayout.paidTime(UPDATED_PAID_TIME).paidAmount(UPDATED_PAID_AMOUNT).paidCcy(UPDATED_PAID_CCY);

        restVotePayoutMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVotePayout.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVotePayout))
            )
            .andExpect(status().isOk());

        // Validate the VotePayout in the database
        List<VotePayout> votePayoutList = votePayoutRepository.findAll();
        assertThat(votePayoutList).hasSize(databaseSizeBeforeUpdate);
        VotePayout testVotePayout = votePayoutList.get(votePayoutList.size() - 1);
        assertThat(testVotePayout.getPaidTime()).isEqualTo(UPDATED_PAID_TIME);
        assertThat(testVotePayout.getPaidAmount()).isEqualTo(UPDATED_PAID_AMOUNT);
        assertThat(testVotePayout.getPaidCcy()).isEqualTo(UPDATED_PAID_CCY);
    }

    @Test
    @Transactional
    void patchNonExistingVotePayout() throws Exception {
        int databaseSizeBeforeUpdate = votePayoutRepository.findAll().size();
        votePayout.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVotePayoutMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, votePayout.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(votePayout))
            )
            .andExpect(status().isBadRequest());

        // Validate the VotePayout in the database
        List<VotePayout> votePayoutList = votePayoutRepository.findAll();
        assertThat(votePayoutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVotePayout() throws Exception {
        int databaseSizeBeforeUpdate = votePayoutRepository.findAll().size();
        votePayout.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVotePayoutMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(votePayout))
            )
            .andExpect(status().isBadRequest());

        // Validate the VotePayout in the database
        List<VotePayout> votePayoutList = votePayoutRepository.findAll();
        assertThat(votePayoutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVotePayout() throws Exception {
        int databaseSizeBeforeUpdate = votePayoutRepository.findAll().size();
        votePayout.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVotePayoutMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(votePayout))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the VotePayout in the database
        List<VotePayout> votePayoutList = votePayoutRepository.findAll();
        assertThat(votePayoutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVotePayout() throws Exception {
        // Initialize the database
        votePayoutRepository.saveAndFlush(votePayout);

        int databaseSizeBeforeDelete = votePayoutRepository.findAll().size();

        // Delete the votePayout
        restVotePayoutMockMvc
            .perform(delete(ENTITY_API_URL_ID, votePayout.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<VotePayout> votePayoutList = votePayoutRepository.findAll();
        assertThat(votePayoutList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
