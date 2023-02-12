package dev.npass.upraiz.web.rest;

import static dev.npass.upraiz.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import dev.npass.upraiz.IntegrationTest;
import dev.npass.upraiz.domain.AccountReclaimRequest;
import dev.npass.upraiz.domain.enumeration.VoteCcy;
import dev.npass.upraiz.repository.AccountReclaimRequestRepository;
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
 * Integration tests for the {@link AccountReclaimRequestResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AccountReclaimRequestResourceIT {

    private static final Double DEFAULT_AMOUNT = 1D;
    private static final Double UPDATED_AMOUNT = 2D;

    private static final ZonedDateTime DEFAULT_TIMESTAMP = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_TIMESTAMP = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final VoteCcy DEFAULT_CCY = VoteCcy.XNO;
    private static final VoteCcy UPDATED_CCY = VoteCcy.XNO;

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/account-reclaim-requests";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AccountReclaimRequestRepository accountReclaimRequestRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAccountReclaimRequestMockMvc;

    private AccountReclaimRequest accountReclaimRequest;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AccountReclaimRequest createEntity(EntityManager em) {
        AccountReclaimRequest accountReclaimRequest = new AccountReclaimRequest()
            .amount(DEFAULT_AMOUNT)
            .timestamp(DEFAULT_TIMESTAMP)
            .ccy(DEFAULT_CCY)
            .active(DEFAULT_ACTIVE);
        return accountReclaimRequest;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AccountReclaimRequest createUpdatedEntity(EntityManager em) {
        AccountReclaimRequest accountReclaimRequest = new AccountReclaimRequest()
            .amount(UPDATED_AMOUNT)
            .timestamp(UPDATED_TIMESTAMP)
            .ccy(UPDATED_CCY)
            .active(UPDATED_ACTIVE);
        return accountReclaimRequest;
    }

    @BeforeEach
    public void initTest() {
        accountReclaimRequest = createEntity(em);
    }

    @Test
    @Transactional
    void createAccountReclaimRequest() throws Exception {
        int databaseSizeBeforeCreate = accountReclaimRequestRepository.findAll().size();
        // Create the AccountReclaimRequest
        restAccountReclaimRequestMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(accountReclaimRequest))
            )
            .andExpect(status().isCreated());

        // Validate the AccountReclaimRequest in the database
        List<AccountReclaimRequest> accountReclaimRequestList = accountReclaimRequestRepository.findAll();
        assertThat(accountReclaimRequestList).hasSize(databaseSizeBeforeCreate + 1);
        AccountReclaimRequest testAccountReclaimRequest = accountReclaimRequestList.get(accountReclaimRequestList.size() - 1);
        assertThat(testAccountReclaimRequest.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testAccountReclaimRequest.getTimestamp()).isEqualTo(DEFAULT_TIMESTAMP);
        assertThat(testAccountReclaimRequest.getCcy()).isEqualTo(DEFAULT_CCY);
        assertThat(testAccountReclaimRequest.getActive()).isEqualTo(DEFAULT_ACTIVE);
    }

    @Test
    @Transactional
    void createAccountReclaimRequestWithExistingId() throws Exception {
        // Create the AccountReclaimRequest with an existing ID
        accountReclaimRequest.setId(1L);

        int databaseSizeBeforeCreate = accountReclaimRequestRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAccountReclaimRequestMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(accountReclaimRequest))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountReclaimRequest in the database
        List<AccountReclaimRequest> accountReclaimRequestList = accountReclaimRequestRepository.findAll();
        assertThat(accountReclaimRequestList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = accountReclaimRequestRepository.findAll().size();
        // set the field null
        accountReclaimRequest.setAmount(null);

        // Create the AccountReclaimRequest, which fails.

        restAccountReclaimRequestMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(accountReclaimRequest))
            )
            .andExpect(status().isBadRequest());

        List<AccountReclaimRequest> accountReclaimRequestList = accountReclaimRequestRepository.findAll();
        assertThat(accountReclaimRequestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTimestampIsRequired() throws Exception {
        int databaseSizeBeforeTest = accountReclaimRequestRepository.findAll().size();
        // set the field null
        accountReclaimRequest.setTimestamp(null);

        // Create the AccountReclaimRequest, which fails.

        restAccountReclaimRequestMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(accountReclaimRequest))
            )
            .andExpect(status().isBadRequest());

        List<AccountReclaimRequest> accountReclaimRequestList = accountReclaimRequestRepository.findAll();
        assertThat(accountReclaimRequestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCcyIsRequired() throws Exception {
        int databaseSizeBeforeTest = accountReclaimRequestRepository.findAll().size();
        // set the field null
        accountReclaimRequest.setCcy(null);

        // Create the AccountReclaimRequest, which fails.

        restAccountReclaimRequestMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(accountReclaimRequest))
            )
            .andExpect(status().isBadRequest());

        List<AccountReclaimRequest> accountReclaimRequestList = accountReclaimRequestRepository.findAll();
        assertThat(accountReclaimRequestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAccountReclaimRequests() throws Exception {
        // Initialize the database
        accountReclaimRequestRepository.saveAndFlush(accountReclaimRequest);

        // Get all the accountReclaimRequestList
        restAccountReclaimRequestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(accountReclaimRequest.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].timestamp").value(hasItem(sameInstant(DEFAULT_TIMESTAMP))))
            .andExpect(jsonPath("$.[*].ccy").value(hasItem(DEFAULT_CCY.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    void getAccountReclaimRequest() throws Exception {
        // Initialize the database
        accountReclaimRequestRepository.saveAndFlush(accountReclaimRequest);

        // Get the accountReclaimRequest
        restAccountReclaimRequestMockMvc
            .perform(get(ENTITY_API_URL_ID, accountReclaimRequest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(accountReclaimRequest.getId().intValue()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.timestamp").value(sameInstant(DEFAULT_TIMESTAMP)))
            .andExpect(jsonPath("$.ccy").value(DEFAULT_CCY.toString()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingAccountReclaimRequest() throws Exception {
        // Get the accountReclaimRequest
        restAccountReclaimRequestMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAccountReclaimRequest() throws Exception {
        // Initialize the database
        accountReclaimRequestRepository.saveAndFlush(accountReclaimRequest);

        int databaseSizeBeforeUpdate = accountReclaimRequestRepository.findAll().size();

        // Update the accountReclaimRequest
        AccountReclaimRequest updatedAccountReclaimRequest = accountReclaimRequestRepository.findById(accountReclaimRequest.getId()).get();
        // Disconnect from session so that the updates on updatedAccountReclaimRequest are not directly saved in db
        em.detach(updatedAccountReclaimRequest);
        updatedAccountReclaimRequest.amount(UPDATED_AMOUNT).timestamp(UPDATED_TIMESTAMP).ccy(UPDATED_CCY).active(UPDATED_ACTIVE);

        restAccountReclaimRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAccountReclaimRequest.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAccountReclaimRequest))
            )
            .andExpect(status().isOk());

        // Validate the AccountReclaimRequest in the database
        List<AccountReclaimRequest> accountReclaimRequestList = accountReclaimRequestRepository.findAll();
        assertThat(accountReclaimRequestList).hasSize(databaseSizeBeforeUpdate);
        AccountReclaimRequest testAccountReclaimRequest = accountReclaimRequestList.get(accountReclaimRequestList.size() - 1);
        assertThat(testAccountReclaimRequest.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testAccountReclaimRequest.getTimestamp()).isEqualTo(UPDATED_TIMESTAMP);
        assertThat(testAccountReclaimRequest.getCcy()).isEqualTo(UPDATED_CCY);
        assertThat(testAccountReclaimRequest.getActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void putNonExistingAccountReclaimRequest() throws Exception {
        int databaseSizeBeforeUpdate = accountReclaimRequestRepository.findAll().size();
        accountReclaimRequest.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAccountReclaimRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, accountReclaimRequest.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(accountReclaimRequest))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountReclaimRequest in the database
        List<AccountReclaimRequest> accountReclaimRequestList = accountReclaimRequestRepository.findAll();
        assertThat(accountReclaimRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAccountReclaimRequest() throws Exception {
        int databaseSizeBeforeUpdate = accountReclaimRequestRepository.findAll().size();
        accountReclaimRequest.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountReclaimRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(accountReclaimRequest))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountReclaimRequest in the database
        List<AccountReclaimRequest> accountReclaimRequestList = accountReclaimRequestRepository.findAll();
        assertThat(accountReclaimRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAccountReclaimRequest() throws Exception {
        int databaseSizeBeforeUpdate = accountReclaimRequestRepository.findAll().size();
        accountReclaimRequest.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountReclaimRequestMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(accountReclaimRequest))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AccountReclaimRequest in the database
        List<AccountReclaimRequest> accountReclaimRequestList = accountReclaimRequestRepository.findAll();
        assertThat(accountReclaimRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAccountReclaimRequestWithPatch() throws Exception {
        // Initialize the database
        accountReclaimRequestRepository.saveAndFlush(accountReclaimRequest);

        int databaseSizeBeforeUpdate = accountReclaimRequestRepository.findAll().size();

        // Update the accountReclaimRequest using partial update
        AccountReclaimRequest partialUpdatedAccountReclaimRequest = new AccountReclaimRequest();
        partialUpdatedAccountReclaimRequest.setId(accountReclaimRequest.getId());

        partialUpdatedAccountReclaimRequest.timestamp(UPDATED_TIMESTAMP);

        restAccountReclaimRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAccountReclaimRequest.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAccountReclaimRequest))
            )
            .andExpect(status().isOk());

        // Validate the AccountReclaimRequest in the database
        List<AccountReclaimRequest> accountReclaimRequestList = accountReclaimRequestRepository.findAll();
        assertThat(accountReclaimRequestList).hasSize(databaseSizeBeforeUpdate);
        AccountReclaimRequest testAccountReclaimRequest = accountReclaimRequestList.get(accountReclaimRequestList.size() - 1);
        assertThat(testAccountReclaimRequest.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testAccountReclaimRequest.getTimestamp()).isEqualTo(UPDATED_TIMESTAMP);
        assertThat(testAccountReclaimRequest.getCcy()).isEqualTo(DEFAULT_CCY);
        assertThat(testAccountReclaimRequest.getActive()).isEqualTo(DEFAULT_ACTIVE);
    }

    @Test
    @Transactional
    void fullUpdateAccountReclaimRequestWithPatch() throws Exception {
        // Initialize the database
        accountReclaimRequestRepository.saveAndFlush(accountReclaimRequest);

        int databaseSizeBeforeUpdate = accountReclaimRequestRepository.findAll().size();

        // Update the accountReclaimRequest using partial update
        AccountReclaimRequest partialUpdatedAccountReclaimRequest = new AccountReclaimRequest();
        partialUpdatedAccountReclaimRequest.setId(accountReclaimRequest.getId());

        partialUpdatedAccountReclaimRequest.amount(UPDATED_AMOUNT).timestamp(UPDATED_TIMESTAMP).ccy(UPDATED_CCY).active(UPDATED_ACTIVE);

        restAccountReclaimRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAccountReclaimRequest.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAccountReclaimRequest))
            )
            .andExpect(status().isOk());

        // Validate the AccountReclaimRequest in the database
        List<AccountReclaimRequest> accountReclaimRequestList = accountReclaimRequestRepository.findAll();
        assertThat(accountReclaimRequestList).hasSize(databaseSizeBeforeUpdate);
        AccountReclaimRequest testAccountReclaimRequest = accountReclaimRequestList.get(accountReclaimRequestList.size() - 1);
        assertThat(testAccountReclaimRequest.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testAccountReclaimRequest.getTimestamp()).isEqualTo(UPDATED_TIMESTAMP);
        assertThat(testAccountReclaimRequest.getCcy()).isEqualTo(UPDATED_CCY);
        assertThat(testAccountReclaimRequest.getActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void patchNonExistingAccountReclaimRequest() throws Exception {
        int databaseSizeBeforeUpdate = accountReclaimRequestRepository.findAll().size();
        accountReclaimRequest.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAccountReclaimRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, accountReclaimRequest.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(accountReclaimRequest))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountReclaimRequest in the database
        List<AccountReclaimRequest> accountReclaimRequestList = accountReclaimRequestRepository.findAll();
        assertThat(accountReclaimRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAccountReclaimRequest() throws Exception {
        int databaseSizeBeforeUpdate = accountReclaimRequestRepository.findAll().size();
        accountReclaimRequest.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountReclaimRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(accountReclaimRequest))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountReclaimRequest in the database
        List<AccountReclaimRequest> accountReclaimRequestList = accountReclaimRequestRepository.findAll();
        assertThat(accountReclaimRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAccountReclaimRequest() throws Exception {
        int databaseSizeBeforeUpdate = accountReclaimRequestRepository.findAll().size();
        accountReclaimRequest.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountReclaimRequestMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(accountReclaimRequest))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AccountReclaimRequest in the database
        List<AccountReclaimRequest> accountReclaimRequestList = accountReclaimRequestRepository.findAll();
        assertThat(accountReclaimRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAccountReclaimRequest() throws Exception {
        // Initialize the database
        accountReclaimRequestRepository.saveAndFlush(accountReclaimRequest);

        int databaseSizeBeforeDelete = accountReclaimRequestRepository.findAll().size();

        // Delete the accountReclaimRequest
        restAccountReclaimRequestMockMvc
            .perform(delete(ENTITY_API_URL_ID, accountReclaimRequest.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AccountReclaimRequest> accountReclaimRequestList = accountReclaimRequestRepository.findAll();
        assertThat(accountReclaimRequestList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
