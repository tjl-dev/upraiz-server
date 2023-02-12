package dev.npass.web.rest;

import static dev.npass.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import dev.npass.IntegrationTest;
import dev.npass.domain.VoteTarget;
import dev.npass.domain.enumeration.VoteCcy;
import dev.npass.domain.enumeration.VoteTargetType;
import dev.npass.repository.VoteTargetRepository;
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
 * Integration tests for the {@link VoteTargetResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VoteTargetResourceIT {

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final VoteTargetType DEFAULT_VOTETYPE = VoteTargetType.POST;
    private static final VoteTargetType UPDATED_VOTETYPE = VoteTargetType.COMMENT;

    private static final Double DEFAULT_PAYOUT = 1D;
    private static final Double UPDATED_PAYOUT = 2D;

    private static final VoteCcy DEFAULT_CCY = VoteCcy.XNO;
    private static final VoteCcy UPDATED_CCY = VoteCcy.XNO;

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final Boolean DEFAULT_FUNDED = false;
    private static final Boolean UPDATED_FUNDED = true;

    private static final ZonedDateTime DEFAULT_CREATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_EXPIRY = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_EXPIRY = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Boolean DEFAULT_BOOSTED = false;
    private static final Boolean UPDATED_BOOSTED = true;

    private static final String ENTITY_API_URL = "/api/vote-targets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VoteTargetRepository voteTargetRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVoteTargetMockMvc;

    private VoteTarget voteTarget;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VoteTarget createEntity(EntityManager em) {
        VoteTarget voteTarget = new VoteTarget()
            .url(DEFAULT_URL)
            .votetype(DEFAULT_VOTETYPE)
            .payout(DEFAULT_PAYOUT)
            .ccy(DEFAULT_CCY)
            .comment(DEFAULT_COMMENT)
            .active(DEFAULT_ACTIVE)
            .funded(DEFAULT_FUNDED)
            .created(DEFAULT_CREATED)
            .expiry(DEFAULT_EXPIRY)
            .boosted(DEFAULT_BOOSTED);
        return voteTarget;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VoteTarget createUpdatedEntity(EntityManager em) {
        VoteTarget voteTarget = new VoteTarget()
            .url(UPDATED_URL)
            .votetype(UPDATED_VOTETYPE)
            .payout(UPDATED_PAYOUT)
            .ccy(UPDATED_CCY)
            .comment(UPDATED_COMMENT)
            .active(UPDATED_ACTIVE)
            .funded(UPDATED_FUNDED)
            .created(UPDATED_CREATED)
            .expiry(UPDATED_EXPIRY)
            .boosted(UPDATED_BOOSTED);
        return voteTarget;
    }

    @BeforeEach
    public void initTest() {
        voteTarget = createEntity(em);
    }

    @Test
    @Transactional
    void createVoteTarget() throws Exception {
        int databaseSizeBeforeCreate = voteTargetRepository.findAll().size();
        // Create the VoteTarget
        restVoteTargetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(voteTarget)))
            .andExpect(status().isCreated());

        // Validate the VoteTarget in the database
        List<VoteTarget> voteTargetList = voteTargetRepository.findAll();
        assertThat(voteTargetList).hasSize(databaseSizeBeforeCreate + 1);
        VoteTarget testVoteTarget = voteTargetList.get(voteTargetList.size() - 1);
        assertThat(testVoteTarget.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testVoteTarget.getVotetype()).isEqualTo(DEFAULT_VOTETYPE);
        assertThat(testVoteTarget.getPayout()).isEqualTo(DEFAULT_PAYOUT);
        assertThat(testVoteTarget.getCcy()).isEqualTo(DEFAULT_CCY);
        assertThat(testVoteTarget.getComment()).isEqualTo(DEFAULT_COMMENT);
        assertThat(testVoteTarget.getActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testVoteTarget.getFunded()).isEqualTo(DEFAULT_FUNDED);
        assertThat(testVoteTarget.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testVoteTarget.getExpiry()).isEqualTo(DEFAULT_EXPIRY);
        assertThat(testVoteTarget.getBoosted()).isEqualTo(DEFAULT_BOOSTED);
    }

    @Test
    @Transactional
    void createVoteTargetWithExistingId() throws Exception {
        // Create the VoteTarget with an existing ID
        voteTarget.setId(1L);

        int databaseSizeBeforeCreate = voteTargetRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVoteTargetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(voteTarget)))
            .andExpect(status().isBadRequest());

        // Validate the VoteTarget in the database
        List<VoteTarget> voteTargetList = voteTargetRepository.findAll();
        assertThat(voteTargetList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUrlIsRequired() throws Exception {
        int databaseSizeBeforeTest = voteTargetRepository.findAll().size();
        // set the field null
        voteTarget.setUrl(null);

        // Create the VoteTarget, which fails.

        restVoteTargetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(voteTarget)))
            .andExpect(status().isBadRequest());

        List<VoteTarget> voteTargetList = voteTargetRepository.findAll();
        assertThat(voteTargetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkVotetypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = voteTargetRepository.findAll().size();
        // set the field null
        voteTarget.setVotetype(null);

        // Create the VoteTarget, which fails.

        restVoteTargetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(voteTarget)))
            .andExpect(status().isBadRequest());

        List<VoteTarget> voteTargetList = voteTargetRepository.findAll();
        assertThat(voteTargetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPayoutIsRequired() throws Exception {
        int databaseSizeBeforeTest = voteTargetRepository.findAll().size();
        // set the field null
        voteTarget.setPayout(null);

        // Create the VoteTarget, which fails.

        restVoteTargetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(voteTarget)))
            .andExpect(status().isBadRequest());

        List<VoteTarget> voteTargetList = voteTargetRepository.findAll();
        assertThat(voteTargetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCcyIsRequired() throws Exception {
        int databaseSizeBeforeTest = voteTargetRepository.findAll().size();
        // set the field null
        voteTarget.setCcy(null);

        // Create the VoteTarget, which fails.

        restVoteTargetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(voteTarget)))
            .andExpect(status().isBadRequest());

        List<VoteTarget> voteTargetList = voteTargetRepository.findAll();
        assertThat(voteTargetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllVoteTargets() throws Exception {
        // Initialize the database
        voteTargetRepository.saveAndFlush(voteTarget);

        // Get all the voteTargetList
        restVoteTargetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(voteTarget.getId().intValue())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].votetype").value(hasItem(DEFAULT_VOTETYPE.toString())))
            .andExpect(jsonPath("$.[*].payout").value(hasItem(DEFAULT_PAYOUT.doubleValue())))
            .andExpect(jsonPath("$.[*].ccy").value(hasItem(DEFAULT_CCY.toString())))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].funded").value(hasItem(DEFAULT_FUNDED.booleanValue())))
            .andExpect(jsonPath("$.[*].created").value(hasItem(sameInstant(DEFAULT_CREATED))))
            .andExpect(jsonPath("$.[*].expiry").value(hasItem(sameInstant(DEFAULT_EXPIRY))))
            .andExpect(jsonPath("$.[*].boosted").value(hasItem(DEFAULT_BOOSTED.booleanValue())));
    }

    @Test
    @Transactional
    void getVoteTarget() throws Exception {
        // Initialize the database
        voteTargetRepository.saveAndFlush(voteTarget);

        // Get the voteTarget
        restVoteTargetMockMvc
            .perform(get(ENTITY_API_URL_ID, voteTarget.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(voteTarget.getId().intValue()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL))
            .andExpect(jsonPath("$.votetype").value(DEFAULT_VOTETYPE.toString()))
            .andExpect(jsonPath("$.payout").value(DEFAULT_PAYOUT.doubleValue()))
            .andExpect(jsonPath("$.ccy").value(DEFAULT_CCY.toString()))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.funded").value(DEFAULT_FUNDED.booleanValue()))
            .andExpect(jsonPath("$.created").value(sameInstant(DEFAULT_CREATED)))
            .andExpect(jsonPath("$.expiry").value(sameInstant(DEFAULT_EXPIRY)))
            .andExpect(jsonPath("$.boosted").value(DEFAULT_BOOSTED.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingVoteTarget() throws Exception {
        // Get the voteTarget
        restVoteTargetMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingVoteTarget() throws Exception {
        // Initialize the database
        voteTargetRepository.saveAndFlush(voteTarget);

        int databaseSizeBeforeUpdate = voteTargetRepository.findAll().size();

        // Update the voteTarget
        VoteTarget updatedVoteTarget = voteTargetRepository.findById(voteTarget.getId()).get();
        // Disconnect from session so that the updates on updatedVoteTarget are not directly saved in db
        em.detach(updatedVoteTarget);
        updatedVoteTarget
            .url(UPDATED_URL)
            .votetype(UPDATED_VOTETYPE)
            .payout(UPDATED_PAYOUT)
            .ccy(UPDATED_CCY)
            .comment(UPDATED_COMMENT)
            .active(UPDATED_ACTIVE)
            .funded(UPDATED_FUNDED)
            .created(UPDATED_CREATED)
            .expiry(UPDATED_EXPIRY)
            .boosted(UPDATED_BOOSTED);

        restVoteTargetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedVoteTarget.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedVoteTarget))
            )
            .andExpect(status().isOk());

        // Validate the VoteTarget in the database
        List<VoteTarget> voteTargetList = voteTargetRepository.findAll();
        assertThat(voteTargetList).hasSize(databaseSizeBeforeUpdate);
        VoteTarget testVoteTarget = voteTargetList.get(voteTargetList.size() - 1);
        assertThat(testVoteTarget.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testVoteTarget.getVotetype()).isEqualTo(UPDATED_VOTETYPE);
        assertThat(testVoteTarget.getPayout()).isEqualTo(UPDATED_PAYOUT);
        assertThat(testVoteTarget.getCcy()).isEqualTo(UPDATED_CCY);
        assertThat(testVoteTarget.getComment()).isEqualTo(UPDATED_COMMENT);
        assertThat(testVoteTarget.getActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testVoteTarget.getFunded()).isEqualTo(UPDATED_FUNDED);
        assertThat(testVoteTarget.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testVoteTarget.getExpiry()).isEqualTo(UPDATED_EXPIRY);
        assertThat(testVoteTarget.getBoosted()).isEqualTo(UPDATED_BOOSTED);
    }

    @Test
    @Transactional
    void putNonExistingVoteTarget() throws Exception {
        int databaseSizeBeforeUpdate = voteTargetRepository.findAll().size();
        voteTarget.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVoteTargetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, voteTarget.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(voteTarget))
            )
            .andExpect(status().isBadRequest());

        // Validate the VoteTarget in the database
        List<VoteTarget> voteTargetList = voteTargetRepository.findAll();
        assertThat(voteTargetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVoteTarget() throws Exception {
        int databaseSizeBeforeUpdate = voteTargetRepository.findAll().size();
        voteTarget.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVoteTargetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(voteTarget))
            )
            .andExpect(status().isBadRequest());

        // Validate the VoteTarget in the database
        List<VoteTarget> voteTargetList = voteTargetRepository.findAll();
        assertThat(voteTargetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVoteTarget() throws Exception {
        int databaseSizeBeforeUpdate = voteTargetRepository.findAll().size();
        voteTarget.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVoteTargetMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(voteTarget)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the VoteTarget in the database
        List<VoteTarget> voteTargetList = voteTargetRepository.findAll();
        assertThat(voteTargetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVoteTargetWithPatch() throws Exception {
        // Initialize the database
        voteTargetRepository.saveAndFlush(voteTarget);

        int databaseSizeBeforeUpdate = voteTargetRepository.findAll().size();

        // Update the voteTarget using partial update
        VoteTarget partialUpdatedVoteTarget = new VoteTarget();
        partialUpdatedVoteTarget.setId(voteTarget.getId());

        partialUpdatedVoteTarget
            .url(UPDATED_URL)
            .ccy(UPDATED_CCY)
            .comment(UPDATED_COMMENT)
            .active(UPDATED_ACTIVE)
            .funded(UPDATED_FUNDED)
            .created(UPDATED_CREATED)
            .expiry(UPDATED_EXPIRY);

        restVoteTargetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVoteTarget.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVoteTarget))
            )
            .andExpect(status().isOk());

        // Validate the VoteTarget in the database
        List<VoteTarget> voteTargetList = voteTargetRepository.findAll();
        assertThat(voteTargetList).hasSize(databaseSizeBeforeUpdate);
        VoteTarget testVoteTarget = voteTargetList.get(voteTargetList.size() - 1);
        assertThat(testVoteTarget.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testVoteTarget.getVotetype()).isEqualTo(DEFAULT_VOTETYPE);
        assertThat(testVoteTarget.getPayout()).isEqualTo(DEFAULT_PAYOUT);
        assertThat(testVoteTarget.getCcy()).isEqualTo(UPDATED_CCY);
        assertThat(testVoteTarget.getComment()).isEqualTo(UPDATED_COMMENT);
        assertThat(testVoteTarget.getActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testVoteTarget.getFunded()).isEqualTo(UPDATED_FUNDED);
        assertThat(testVoteTarget.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testVoteTarget.getExpiry()).isEqualTo(UPDATED_EXPIRY);
        assertThat(testVoteTarget.getBoosted()).isEqualTo(DEFAULT_BOOSTED);
    }

    @Test
    @Transactional
    void fullUpdateVoteTargetWithPatch() throws Exception {
        // Initialize the database
        voteTargetRepository.saveAndFlush(voteTarget);

        int databaseSizeBeforeUpdate = voteTargetRepository.findAll().size();

        // Update the voteTarget using partial update
        VoteTarget partialUpdatedVoteTarget = new VoteTarget();
        partialUpdatedVoteTarget.setId(voteTarget.getId());

        partialUpdatedVoteTarget
            .url(UPDATED_URL)
            .votetype(UPDATED_VOTETYPE)
            .payout(UPDATED_PAYOUT)
            .ccy(UPDATED_CCY)
            .comment(UPDATED_COMMENT)
            .active(UPDATED_ACTIVE)
            .funded(UPDATED_FUNDED)
            .created(UPDATED_CREATED)
            .expiry(UPDATED_EXPIRY)
            .boosted(UPDATED_BOOSTED);

        restVoteTargetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVoteTarget.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVoteTarget))
            )
            .andExpect(status().isOk());

        // Validate the VoteTarget in the database
        List<VoteTarget> voteTargetList = voteTargetRepository.findAll();
        assertThat(voteTargetList).hasSize(databaseSizeBeforeUpdate);
        VoteTarget testVoteTarget = voteTargetList.get(voteTargetList.size() - 1);
        assertThat(testVoteTarget.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testVoteTarget.getVotetype()).isEqualTo(UPDATED_VOTETYPE);
        assertThat(testVoteTarget.getPayout()).isEqualTo(UPDATED_PAYOUT);
        assertThat(testVoteTarget.getCcy()).isEqualTo(UPDATED_CCY);
        assertThat(testVoteTarget.getComment()).isEqualTo(UPDATED_COMMENT);
        assertThat(testVoteTarget.getActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testVoteTarget.getFunded()).isEqualTo(UPDATED_FUNDED);
        assertThat(testVoteTarget.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testVoteTarget.getExpiry()).isEqualTo(UPDATED_EXPIRY);
        assertThat(testVoteTarget.getBoosted()).isEqualTo(UPDATED_BOOSTED);
    }

    @Test
    @Transactional
    void patchNonExistingVoteTarget() throws Exception {
        int databaseSizeBeforeUpdate = voteTargetRepository.findAll().size();
        voteTarget.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVoteTargetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, voteTarget.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(voteTarget))
            )
            .andExpect(status().isBadRequest());

        // Validate the VoteTarget in the database
        List<VoteTarget> voteTargetList = voteTargetRepository.findAll();
        assertThat(voteTargetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVoteTarget() throws Exception {
        int databaseSizeBeforeUpdate = voteTargetRepository.findAll().size();
        voteTarget.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVoteTargetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(voteTarget))
            )
            .andExpect(status().isBadRequest());

        // Validate the VoteTarget in the database
        List<VoteTarget> voteTargetList = voteTargetRepository.findAll();
        assertThat(voteTargetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVoteTarget() throws Exception {
        int databaseSizeBeforeUpdate = voteTargetRepository.findAll().size();
        voteTarget.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVoteTargetMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(voteTarget))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the VoteTarget in the database
        List<VoteTarget> voteTargetList = voteTargetRepository.findAll();
        assertThat(voteTargetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVoteTarget() throws Exception {
        // Initialize the database
        voteTargetRepository.saveAndFlush(voteTarget);

        int databaseSizeBeforeDelete = voteTargetRepository.findAll().size();

        // Delete the voteTarget
        restVoteTargetMockMvc
            .perform(delete(ENTITY_API_URL_ID, voteTarget.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<VoteTarget> voteTargetList = voteTargetRepository.findAll();
        assertThat(voteTargetList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
