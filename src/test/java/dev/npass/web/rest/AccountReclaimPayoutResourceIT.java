package dev.npass.web.rest;

import static dev.npass.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import dev.npass.IntegrationTest;
import dev.npass.domain.AccountReclaimPayout;
import dev.npass.domain.enumeration.VoteCcy;
import dev.npass.repository.AccountReclaimPayoutRepository;
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
 * Integration tests for the {@link AccountReclaimPayoutResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AccountReclaimPayoutResourceIT {

    private static final Double DEFAULT_AMOUNT = 1D;
    private static final Double UPDATED_AMOUNT = 2D;

    private static final ZonedDateTime DEFAULT_TIMESTAMP = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_TIMESTAMP = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final VoteCcy DEFAULT_CCY = VoteCcy.XNO;
    private static final VoteCcy UPDATED_CCY = VoteCcy.XNO;

    private static final String DEFAULT_TXN_REF = "AAAAAAAAAA";
    private static final String UPDATED_TXN_REF = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/account-reclaim-payouts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AccountReclaimPayoutRepository accountReclaimPayoutRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAccountReclaimPayoutMockMvc;

    private AccountReclaimPayout accountReclaimPayout;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AccountReclaimPayout createEntity(EntityManager em) {
        AccountReclaimPayout accountReclaimPayout = new AccountReclaimPayout()
            .amount(DEFAULT_AMOUNT)
            .timestamp(DEFAULT_TIMESTAMP)
            .ccy(DEFAULT_CCY)
            .txnRef(DEFAULT_TXN_REF);
        return accountReclaimPayout;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AccountReclaimPayout createUpdatedEntity(EntityManager em) {
        AccountReclaimPayout accountReclaimPayout = new AccountReclaimPayout()
            .amount(UPDATED_AMOUNT)
            .timestamp(UPDATED_TIMESTAMP)
            .ccy(UPDATED_CCY)
            .txnRef(UPDATED_TXN_REF);
        return accountReclaimPayout;
    }

    @BeforeEach
    public void initTest() {
        accountReclaimPayout = createEntity(em);
    }

    @Test
    @Transactional
    void createAccountReclaimPayout() throws Exception {
        int databaseSizeBeforeCreate = accountReclaimPayoutRepository.findAll().size();
        // Create the AccountReclaimPayout
        restAccountReclaimPayoutMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(accountReclaimPayout))
            )
            .andExpect(status().isCreated());

        // Validate the AccountReclaimPayout in the database
        List<AccountReclaimPayout> accountReclaimPayoutList = accountReclaimPayoutRepository.findAll();
        assertThat(accountReclaimPayoutList).hasSize(databaseSizeBeforeCreate + 1);
        AccountReclaimPayout testAccountReclaimPayout = accountReclaimPayoutList.get(accountReclaimPayoutList.size() - 1);
        assertThat(testAccountReclaimPayout.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testAccountReclaimPayout.getTimestamp()).isEqualTo(DEFAULT_TIMESTAMP);
        assertThat(testAccountReclaimPayout.getCcy()).isEqualTo(DEFAULT_CCY);
        assertThat(testAccountReclaimPayout.getTxnRef()).isEqualTo(DEFAULT_TXN_REF);
    }

    @Test
    @Transactional
    void createAccountReclaimPayoutWithExistingId() throws Exception {
        // Create the AccountReclaimPayout with an existing ID
        accountReclaimPayout.setId(1L);

        int databaseSizeBeforeCreate = accountReclaimPayoutRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAccountReclaimPayoutMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(accountReclaimPayout))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountReclaimPayout in the database
        List<AccountReclaimPayout> accountReclaimPayoutList = accountReclaimPayoutRepository.findAll();
        assertThat(accountReclaimPayoutList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = accountReclaimPayoutRepository.findAll().size();
        // set the field null
        accountReclaimPayout.setAmount(null);

        // Create the AccountReclaimPayout, which fails.

        restAccountReclaimPayoutMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(accountReclaimPayout))
            )
            .andExpect(status().isBadRequest());

        List<AccountReclaimPayout> accountReclaimPayoutList = accountReclaimPayoutRepository.findAll();
        assertThat(accountReclaimPayoutList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTimestampIsRequired() throws Exception {
        int databaseSizeBeforeTest = accountReclaimPayoutRepository.findAll().size();
        // set the field null
        accountReclaimPayout.setTimestamp(null);

        // Create the AccountReclaimPayout, which fails.

        restAccountReclaimPayoutMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(accountReclaimPayout))
            )
            .andExpect(status().isBadRequest());

        List<AccountReclaimPayout> accountReclaimPayoutList = accountReclaimPayoutRepository.findAll();
        assertThat(accountReclaimPayoutList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCcyIsRequired() throws Exception {
        int databaseSizeBeforeTest = accountReclaimPayoutRepository.findAll().size();
        // set the field null
        accountReclaimPayout.setCcy(null);

        // Create the AccountReclaimPayout, which fails.

        restAccountReclaimPayoutMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(accountReclaimPayout))
            )
            .andExpect(status().isBadRequest());

        List<AccountReclaimPayout> accountReclaimPayoutList = accountReclaimPayoutRepository.findAll();
        assertThat(accountReclaimPayoutList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTxnRefIsRequired() throws Exception {
        int databaseSizeBeforeTest = accountReclaimPayoutRepository.findAll().size();
        // set the field null
        accountReclaimPayout.setTxnRef(null);

        // Create the AccountReclaimPayout, which fails.

        restAccountReclaimPayoutMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(accountReclaimPayout))
            )
            .andExpect(status().isBadRequest());

        List<AccountReclaimPayout> accountReclaimPayoutList = accountReclaimPayoutRepository.findAll();
        assertThat(accountReclaimPayoutList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAccountReclaimPayouts() throws Exception {
        // Initialize the database
        accountReclaimPayoutRepository.saveAndFlush(accountReclaimPayout);

        // Get all the accountReclaimPayoutList
        restAccountReclaimPayoutMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(accountReclaimPayout.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].timestamp").value(hasItem(sameInstant(DEFAULT_TIMESTAMP))))
            .andExpect(jsonPath("$.[*].ccy").value(hasItem(DEFAULT_CCY.toString())))
            .andExpect(jsonPath("$.[*].txnRef").value(hasItem(DEFAULT_TXN_REF)));
    }

    @Test
    @Transactional
    void getAccountReclaimPayout() throws Exception {
        // Initialize the database
        accountReclaimPayoutRepository.saveAndFlush(accountReclaimPayout);

        // Get the accountReclaimPayout
        restAccountReclaimPayoutMockMvc
            .perform(get(ENTITY_API_URL_ID, accountReclaimPayout.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(accountReclaimPayout.getId().intValue()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.timestamp").value(sameInstant(DEFAULT_TIMESTAMP)))
            .andExpect(jsonPath("$.ccy").value(DEFAULT_CCY.toString()))
            .andExpect(jsonPath("$.txnRef").value(DEFAULT_TXN_REF));
    }

    @Test
    @Transactional
    void getNonExistingAccountReclaimPayout() throws Exception {
        // Get the accountReclaimPayout
        restAccountReclaimPayoutMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAccountReclaimPayout() throws Exception {
        // Initialize the database
        accountReclaimPayoutRepository.saveAndFlush(accountReclaimPayout);

        int databaseSizeBeforeUpdate = accountReclaimPayoutRepository.findAll().size();

        // Update the accountReclaimPayout
        AccountReclaimPayout updatedAccountReclaimPayout = accountReclaimPayoutRepository.findById(accountReclaimPayout.getId()).get();
        // Disconnect from session so that the updates on updatedAccountReclaimPayout are not directly saved in db
        em.detach(updatedAccountReclaimPayout);
        updatedAccountReclaimPayout.amount(UPDATED_AMOUNT).timestamp(UPDATED_TIMESTAMP).ccy(UPDATED_CCY).txnRef(UPDATED_TXN_REF);

        restAccountReclaimPayoutMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAccountReclaimPayout.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAccountReclaimPayout))
            )
            .andExpect(status().isOk());

        // Validate the AccountReclaimPayout in the database
        List<AccountReclaimPayout> accountReclaimPayoutList = accountReclaimPayoutRepository.findAll();
        assertThat(accountReclaimPayoutList).hasSize(databaseSizeBeforeUpdate);
        AccountReclaimPayout testAccountReclaimPayout = accountReclaimPayoutList.get(accountReclaimPayoutList.size() - 1);
        assertThat(testAccountReclaimPayout.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testAccountReclaimPayout.getTimestamp()).isEqualTo(UPDATED_TIMESTAMP);
        assertThat(testAccountReclaimPayout.getCcy()).isEqualTo(UPDATED_CCY);
        assertThat(testAccountReclaimPayout.getTxnRef()).isEqualTo(UPDATED_TXN_REF);
    }

    @Test
    @Transactional
    void putNonExistingAccountReclaimPayout() throws Exception {
        int databaseSizeBeforeUpdate = accountReclaimPayoutRepository.findAll().size();
        accountReclaimPayout.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAccountReclaimPayoutMockMvc
            .perform(
                put(ENTITY_API_URL_ID, accountReclaimPayout.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(accountReclaimPayout))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountReclaimPayout in the database
        List<AccountReclaimPayout> accountReclaimPayoutList = accountReclaimPayoutRepository.findAll();
        assertThat(accountReclaimPayoutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAccountReclaimPayout() throws Exception {
        int databaseSizeBeforeUpdate = accountReclaimPayoutRepository.findAll().size();
        accountReclaimPayout.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountReclaimPayoutMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(accountReclaimPayout))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountReclaimPayout in the database
        List<AccountReclaimPayout> accountReclaimPayoutList = accountReclaimPayoutRepository.findAll();
        assertThat(accountReclaimPayoutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAccountReclaimPayout() throws Exception {
        int databaseSizeBeforeUpdate = accountReclaimPayoutRepository.findAll().size();
        accountReclaimPayout.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountReclaimPayoutMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(accountReclaimPayout))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AccountReclaimPayout in the database
        List<AccountReclaimPayout> accountReclaimPayoutList = accountReclaimPayoutRepository.findAll();
        assertThat(accountReclaimPayoutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAccountReclaimPayoutWithPatch() throws Exception {
        // Initialize the database
        accountReclaimPayoutRepository.saveAndFlush(accountReclaimPayout);

        int databaseSizeBeforeUpdate = accountReclaimPayoutRepository.findAll().size();

        // Update the accountReclaimPayout using partial update
        AccountReclaimPayout partialUpdatedAccountReclaimPayout = new AccountReclaimPayout();
        partialUpdatedAccountReclaimPayout.setId(accountReclaimPayout.getId());

        partialUpdatedAccountReclaimPayout.ccy(UPDATED_CCY);

        restAccountReclaimPayoutMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAccountReclaimPayout.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAccountReclaimPayout))
            )
            .andExpect(status().isOk());

        // Validate the AccountReclaimPayout in the database
        List<AccountReclaimPayout> accountReclaimPayoutList = accountReclaimPayoutRepository.findAll();
        assertThat(accountReclaimPayoutList).hasSize(databaseSizeBeforeUpdate);
        AccountReclaimPayout testAccountReclaimPayout = accountReclaimPayoutList.get(accountReclaimPayoutList.size() - 1);
        assertThat(testAccountReclaimPayout.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testAccountReclaimPayout.getTimestamp()).isEqualTo(DEFAULT_TIMESTAMP);
        assertThat(testAccountReclaimPayout.getCcy()).isEqualTo(UPDATED_CCY);
        assertThat(testAccountReclaimPayout.getTxnRef()).isEqualTo(DEFAULT_TXN_REF);
    }

    @Test
    @Transactional
    void fullUpdateAccountReclaimPayoutWithPatch() throws Exception {
        // Initialize the database
        accountReclaimPayoutRepository.saveAndFlush(accountReclaimPayout);

        int databaseSizeBeforeUpdate = accountReclaimPayoutRepository.findAll().size();

        // Update the accountReclaimPayout using partial update
        AccountReclaimPayout partialUpdatedAccountReclaimPayout = new AccountReclaimPayout();
        partialUpdatedAccountReclaimPayout.setId(accountReclaimPayout.getId());

        partialUpdatedAccountReclaimPayout.amount(UPDATED_AMOUNT).timestamp(UPDATED_TIMESTAMP).ccy(UPDATED_CCY).txnRef(UPDATED_TXN_REF);

        restAccountReclaimPayoutMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAccountReclaimPayout.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAccountReclaimPayout))
            )
            .andExpect(status().isOk());

        // Validate the AccountReclaimPayout in the database
        List<AccountReclaimPayout> accountReclaimPayoutList = accountReclaimPayoutRepository.findAll();
        assertThat(accountReclaimPayoutList).hasSize(databaseSizeBeforeUpdate);
        AccountReclaimPayout testAccountReclaimPayout = accountReclaimPayoutList.get(accountReclaimPayoutList.size() - 1);
        assertThat(testAccountReclaimPayout.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testAccountReclaimPayout.getTimestamp()).isEqualTo(UPDATED_TIMESTAMP);
        assertThat(testAccountReclaimPayout.getCcy()).isEqualTo(UPDATED_CCY);
        assertThat(testAccountReclaimPayout.getTxnRef()).isEqualTo(UPDATED_TXN_REF);
    }

    @Test
    @Transactional
    void patchNonExistingAccountReclaimPayout() throws Exception {
        int databaseSizeBeforeUpdate = accountReclaimPayoutRepository.findAll().size();
        accountReclaimPayout.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAccountReclaimPayoutMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, accountReclaimPayout.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(accountReclaimPayout))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountReclaimPayout in the database
        List<AccountReclaimPayout> accountReclaimPayoutList = accountReclaimPayoutRepository.findAll();
        assertThat(accountReclaimPayoutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAccountReclaimPayout() throws Exception {
        int databaseSizeBeforeUpdate = accountReclaimPayoutRepository.findAll().size();
        accountReclaimPayout.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountReclaimPayoutMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(accountReclaimPayout))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountReclaimPayout in the database
        List<AccountReclaimPayout> accountReclaimPayoutList = accountReclaimPayoutRepository.findAll();
        assertThat(accountReclaimPayoutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAccountReclaimPayout() throws Exception {
        int databaseSizeBeforeUpdate = accountReclaimPayoutRepository.findAll().size();
        accountReclaimPayout.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountReclaimPayoutMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(accountReclaimPayout))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AccountReclaimPayout in the database
        List<AccountReclaimPayout> accountReclaimPayoutList = accountReclaimPayoutRepository.findAll();
        assertThat(accountReclaimPayoutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAccountReclaimPayout() throws Exception {
        // Initialize the database
        accountReclaimPayoutRepository.saveAndFlush(accountReclaimPayout);

        int databaseSizeBeforeDelete = accountReclaimPayoutRepository.findAll().size();

        // Delete the accountReclaimPayout
        restAccountReclaimPayoutMockMvc
            .perform(delete(ENTITY_API_URL_ID, accountReclaimPayout.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AccountReclaimPayout> accountReclaimPayoutList = accountReclaimPayoutRepository.findAll();
        assertThat(accountReclaimPayoutList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
