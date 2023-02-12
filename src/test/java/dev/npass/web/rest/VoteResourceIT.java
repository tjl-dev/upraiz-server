package dev.npass.web.rest;

import static dev.npass.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import dev.npass.IntegrationTest;
import dev.npass.domain.Vote;
import dev.npass.repository.VoteRepository;
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
 * Integration tests for the {@link VoteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VoteResourceIT {

    private static final ZonedDateTime DEFAULT_VOTED_TIMESTAMP = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_VOTED_TIMESTAMP = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Boolean DEFAULT_VERIFIED = false;
    private static final Boolean UPDATED_VERIFIED = true;

    private static final ZonedDateTime DEFAULT_VERIFIED_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_VERIFIED_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_VERIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_VERIFIED_BY = "BBBBBBBBBB";

    private static final Boolean DEFAULT_PAID = false;
    private static final Boolean UPDATED_PAID = true;

    private static final String ENTITY_API_URL = "/api/votes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVoteMockMvc;

    private Vote vote;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vote createEntity(EntityManager em) {
        Vote vote = new Vote()
            .votedTimestamp(DEFAULT_VOTED_TIMESTAMP)
            .verified(DEFAULT_VERIFIED)
            .verifiedTime(DEFAULT_VERIFIED_TIME)
            .verifiedBy(DEFAULT_VERIFIED_BY)
            .paid(DEFAULT_PAID);
        return vote;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vote createUpdatedEntity(EntityManager em) {
        Vote vote = new Vote()
            .votedTimestamp(UPDATED_VOTED_TIMESTAMP)
            .verified(UPDATED_VERIFIED)
            .verifiedTime(UPDATED_VERIFIED_TIME)
            .verifiedBy(UPDATED_VERIFIED_BY)
            .paid(UPDATED_PAID);
        return vote;
    }

    @BeforeEach
    public void initTest() {
        vote = createEntity(em);
    }

    @Test
    @Transactional
    void createVote() throws Exception {
        int databaseSizeBeforeCreate = voteRepository.findAll().size();
        // Create the Vote
        restVoteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vote)))
            .andExpect(status().isCreated());

        // Validate the Vote in the database
        List<Vote> voteList = voteRepository.findAll();
        assertThat(voteList).hasSize(databaseSizeBeforeCreate + 1);
        Vote testVote = voteList.get(voteList.size() - 1);
        assertThat(testVote.getVotedTimestamp()).isEqualTo(DEFAULT_VOTED_TIMESTAMP);
        assertThat(testVote.getVerified()).isEqualTo(DEFAULT_VERIFIED);
        assertThat(testVote.getVerifiedTime()).isEqualTo(DEFAULT_VERIFIED_TIME);
        assertThat(testVote.getVerifiedBy()).isEqualTo(DEFAULT_VERIFIED_BY);
        assertThat(testVote.getPaid()).isEqualTo(DEFAULT_PAID);
    }

    @Test
    @Transactional
    void createVoteWithExistingId() throws Exception {
        // Create the Vote with an existing ID
        vote.setId(1L);

        int databaseSizeBeforeCreate = voteRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVoteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vote)))
            .andExpect(status().isBadRequest());

        // Validate the Vote in the database
        List<Vote> voteList = voteRepository.findAll();
        assertThat(voteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkVotedTimestampIsRequired() throws Exception {
        int databaseSizeBeforeTest = voteRepository.findAll().size();
        // set the field null
        vote.setVotedTimestamp(null);

        // Create the Vote, which fails.

        restVoteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vote)))
            .andExpect(status().isBadRequest());

        List<Vote> voteList = voteRepository.findAll();
        assertThat(voteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkVerifiedIsRequired() throws Exception {
        int databaseSizeBeforeTest = voteRepository.findAll().size();
        // set the field null
        vote.setVerified(null);

        // Create the Vote, which fails.

        restVoteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vote)))
            .andExpect(status().isBadRequest());

        List<Vote> voteList = voteRepository.findAll();
        assertThat(voteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkVerifiedTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = voteRepository.findAll().size();
        // set the field null
        vote.setVerifiedTime(null);

        // Create the Vote, which fails.

        restVoteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vote)))
            .andExpect(status().isBadRequest());

        List<Vote> voteList = voteRepository.findAll();
        assertThat(voteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkVerifiedByIsRequired() throws Exception {
        int databaseSizeBeforeTest = voteRepository.findAll().size();
        // set the field null
        vote.setVerifiedBy(null);

        // Create the Vote, which fails.

        restVoteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vote)))
            .andExpect(status().isBadRequest());

        List<Vote> voteList = voteRepository.findAll();
        assertThat(voteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPaidIsRequired() throws Exception {
        int databaseSizeBeforeTest = voteRepository.findAll().size();
        // set the field null
        vote.setPaid(null);

        // Create the Vote, which fails.

        restVoteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vote)))
            .andExpect(status().isBadRequest());

        List<Vote> voteList = voteRepository.findAll();
        assertThat(voteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllVotes() throws Exception {
        // Initialize the database
        voteRepository.saveAndFlush(vote);

        // Get all the voteList
        restVoteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vote.getId().intValue())))
            .andExpect(jsonPath("$.[*].votedTimestamp").value(hasItem(sameInstant(DEFAULT_VOTED_TIMESTAMP))))
            .andExpect(jsonPath("$.[*].verified").value(hasItem(DEFAULT_VERIFIED.booleanValue())))
            .andExpect(jsonPath("$.[*].verifiedTime").value(hasItem(sameInstant(DEFAULT_VERIFIED_TIME))))
            .andExpect(jsonPath("$.[*].verifiedBy").value(hasItem(DEFAULT_VERIFIED_BY)))
            .andExpect(jsonPath("$.[*].paid").value(hasItem(DEFAULT_PAID.booleanValue())));
    }

    @Test
    @Transactional
    void getVote() throws Exception {
        // Initialize the database
        voteRepository.saveAndFlush(vote);

        // Get the vote
        restVoteMockMvc
            .perform(get(ENTITY_API_URL_ID, vote.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vote.getId().intValue()))
            .andExpect(jsonPath("$.votedTimestamp").value(sameInstant(DEFAULT_VOTED_TIMESTAMP)))
            .andExpect(jsonPath("$.verified").value(DEFAULT_VERIFIED.booleanValue()))
            .andExpect(jsonPath("$.verifiedTime").value(sameInstant(DEFAULT_VERIFIED_TIME)))
            .andExpect(jsonPath("$.verifiedBy").value(DEFAULT_VERIFIED_BY))
            .andExpect(jsonPath("$.paid").value(DEFAULT_PAID.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingVote() throws Exception {
        // Get the vote
        restVoteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingVote() throws Exception {
        // Initialize the database
        voteRepository.saveAndFlush(vote);

        int databaseSizeBeforeUpdate = voteRepository.findAll().size();

        // Update the vote
        Vote updatedVote = voteRepository.findById(vote.getId()).get();
        // Disconnect from session so that the updates on updatedVote are not directly saved in db
        em.detach(updatedVote);
        updatedVote
            .votedTimestamp(UPDATED_VOTED_TIMESTAMP)
            .verified(UPDATED_VERIFIED)
            .verifiedTime(UPDATED_VERIFIED_TIME)
            .verifiedBy(UPDATED_VERIFIED_BY)
            .paid(UPDATED_PAID);

        restVoteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedVote.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedVote))
            )
            .andExpect(status().isOk());

        // Validate the Vote in the database
        List<Vote> voteList = voteRepository.findAll();
        assertThat(voteList).hasSize(databaseSizeBeforeUpdate);
        Vote testVote = voteList.get(voteList.size() - 1);
        assertThat(testVote.getVotedTimestamp()).isEqualTo(UPDATED_VOTED_TIMESTAMP);
        assertThat(testVote.getVerified()).isEqualTo(UPDATED_VERIFIED);
        assertThat(testVote.getVerifiedTime()).isEqualTo(UPDATED_VERIFIED_TIME);
        assertThat(testVote.getVerifiedBy()).isEqualTo(UPDATED_VERIFIED_BY);
        assertThat(testVote.getPaid()).isEqualTo(UPDATED_PAID);
    }

    @Test
    @Transactional
    void putNonExistingVote() throws Exception {
        int databaseSizeBeforeUpdate = voteRepository.findAll().size();
        vote.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVoteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vote.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vote))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vote in the database
        List<Vote> voteList = voteRepository.findAll();
        assertThat(voteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVote() throws Exception {
        int databaseSizeBeforeUpdate = voteRepository.findAll().size();
        vote.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVoteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vote))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vote in the database
        List<Vote> voteList = voteRepository.findAll();
        assertThat(voteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVote() throws Exception {
        int databaseSizeBeforeUpdate = voteRepository.findAll().size();
        vote.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVoteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vote)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vote in the database
        List<Vote> voteList = voteRepository.findAll();
        assertThat(voteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVoteWithPatch() throws Exception {
        // Initialize the database
        voteRepository.saveAndFlush(vote);

        int databaseSizeBeforeUpdate = voteRepository.findAll().size();

        // Update the vote using partial update
        Vote partialUpdatedVote = new Vote();
        partialUpdatedVote.setId(vote.getId());

        partialUpdatedVote.votedTimestamp(UPDATED_VOTED_TIMESTAMP).verified(UPDATED_VERIFIED).verifiedTime(UPDATED_VERIFIED_TIME);

        restVoteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVote.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVote))
            )
            .andExpect(status().isOk());

        // Validate the Vote in the database
        List<Vote> voteList = voteRepository.findAll();
        assertThat(voteList).hasSize(databaseSizeBeforeUpdate);
        Vote testVote = voteList.get(voteList.size() - 1);
        assertThat(testVote.getVotedTimestamp()).isEqualTo(UPDATED_VOTED_TIMESTAMP);
        assertThat(testVote.getVerified()).isEqualTo(UPDATED_VERIFIED);
        assertThat(testVote.getVerifiedTime()).isEqualTo(UPDATED_VERIFIED_TIME);
        assertThat(testVote.getVerifiedBy()).isEqualTo(DEFAULT_VERIFIED_BY);
        assertThat(testVote.getPaid()).isEqualTo(DEFAULT_PAID);
    }

    @Test
    @Transactional
    void fullUpdateVoteWithPatch() throws Exception {
        // Initialize the database
        voteRepository.saveAndFlush(vote);

        int databaseSizeBeforeUpdate = voteRepository.findAll().size();

        // Update the vote using partial update
        Vote partialUpdatedVote = new Vote();
        partialUpdatedVote.setId(vote.getId());

        partialUpdatedVote
            .votedTimestamp(UPDATED_VOTED_TIMESTAMP)
            .verified(UPDATED_VERIFIED)
            .verifiedTime(UPDATED_VERIFIED_TIME)
            .verifiedBy(UPDATED_VERIFIED_BY)
            .paid(UPDATED_PAID);

        restVoteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVote.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVote))
            )
            .andExpect(status().isOk());

        // Validate the Vote in the database
        List<Vote> voteList = voteRepository.findAll();
        assertThat(voteList).hasSize(databaseSizeBeforeUpdate);
        Vote testVote = voteList.get(voteList.size() - 1);
        assertThat(testVote.getVotedTimestamp()).isEqualTo(UPDATED_VOTED_TIMESTAMP);
        assertThat(testVote.getVerified()).isEqualTo(UPDATED_VERIFIED);
        assertThat(testVote.getVerifiedTime()).isEqualTo(UPDATED_VERIFIED_TIME);
        assertThat(testVote.getVerifiedBy()).isEqualTo(UPDATED_VERIFIED_BY);
        assertThat(testVote.getPaid()).isEqualTo(UPDATED_PAID);
    }

    @Test
    @Transactional
    void patchNonExistingVote() throws Exception {
        int databaseSizeBeforeUpdate = voteRepository.findAll().size();
        vote.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVoteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, vote.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vote))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vote in the database
        List<Vote> voteList = voteRepository.findAll();
        assertThat(voteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVote() throws Exception {
        int databaseSizeBeforeUpdate = voteRepository.findAll().size();
        vote.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVoteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vote))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vote in the database
        List<Vote> voteList = voteRepository.findAll();
        assertThat(voteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVote() throws Exception {
        int databaseSizeBeforeUpdate = voteRepository.findAll().size();
        vote.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVoteMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(vote)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vote in the database
        List<Vote> voteList = voteRepository.findAll();
        assertThat(voteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVote() throws Exception {
        // Initialize the database
        voteRepository.saveAndFlush(vote);

        int databaseSizeBeforeDelete = voteRepository.findAll().size();

        // Delete the vote
        restVoteMockMvc
            .perform(delete(ENTITY_API_URL_ID, vote.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Vote> voteList = voteRepository.findAll();
        assertThat(voteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
