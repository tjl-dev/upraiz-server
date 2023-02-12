package dev.npass.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import dev.npass.IntegrationTest;
import dev.npass.domain.ManagedAccount;
import dev.npass.domain.enumeration.VoteCcy;
import dev.npass.repository.ManagedAccountRepository;
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
 * Integration tests for the {@link ManagedAccountResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ManagedAccountResourceIT {

    private static final String DEFAULT_ACCOUNT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ACCOUNT_NAME = "BBBBBBBBBB";

    private static final VoteCcy DEFAULT_CCY = VoteCcy.XNO;
    private static final VoteCcy UPDATED_CCY = VoteCcy.XNO;

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_SEED = "AAAAAAAAAA";
    private static final String UPDATED_SEED = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/managed-accounts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ManagedAccountRepository managedAccountRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restManagedAccountMockMvc;

    private ManagedAccount managedAccount;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ManagedAccount createEntity(EntityManager em) {
        ManagedAccount managedAccount = new ManagedAccount()
            .accountName(DEFAULT_ACCOUNT_NAME)
            .ccy(DEFAULT_CCY)
            .address(DEFAULT_ADDRESS)
            .seed(DEFAULT_SEED);
        return managedAccount;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ManagedAccount createUpdatedEntity(EntityManager em) {
        ManagedAccount managedAccount = new ManagedAccount()
            .accountName(UPDATED_ACCOUNT_NAME)
            .ccy(UPDATED_CCY)
            .address(UPDATED_ADDRESS)
            .seed(UPDATED_SEED);
        return managedAccount;
    }

    @BeforeEach
    public void initTest() {
        managedAccount = createEntity(em);
    }

    @Test
    @Transactional
    void createManagedAccount() throws Exception {
        int databaseSizeBeforeCreate = managedAccountRepository.findAll().size();
        // Create the ManagedAccount
        restManagedAccountMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(managedAccount))
            )
            .andExpect(status().isCreated());

        // Validate the ManagedAccount in the database
        List<ManagedAccount> managedAccountList = managedAccountRepository.findAll();
        assertThat(managedAccountList).hasSize(databaseSizeBeforeCreate + 1);
        ManagedAccount testManagedAccount = managedAccountList.get(managedAccountList.size() - 1);
        assertThat(testManagedAccount.getAccountName()).isEqualTo(DEFAULT_ACCOUNT_NAME);
        assertThat(testManagedAccount.getCcy()).isEqualTo(DEFAULT_CCY);
        assertThat(testManagedAccount.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testManagedAccount.getSeed()).isEqualTo(DEFAULT_SEED);
    }

    @Test
    @Transactional
    void createManagedAccountWithExistingId() throws Exception {
        // Create the ManagedAccount with an existing ID
        managedAccount.setId(1L);

        int databaseSizeBeforeCreate = managedAccountRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restManagedAccountMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(managedAccount))
            )
            .andExpect(status().isBadRequest());

        // Validate the ManagedAccount in the database
        List<ManagedAccount> managedAccountList = managedAccountRepository.findAll();
        assertThat(managedAccountList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCcyIsRequired() throws Exception {
        int databaseSizeBeforeTest = managedAccountRepository.findAll().size();
        // set the field null
        managedAccount.setCcy(null);

        // Create the ManagedAccount, which fails.

        restManagedAccountMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(managedAccount))
            )
            .andExpect(status().isBadRequest());

        List<ManagedAccount> managedAccountList = managedAccountRepository.findAll();
        assertThat(managedAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = managedAccountRepository.findAll().size();
        // set the field null
        managedAccount.setAddress(null);

        // Create the ManagedAccount, which fails.

        restManagedAccountMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(managedAccount))
            )
            .andExpect(status().isBadRequest());

        List<ManagedAccount> managedAccountList = managedAccountRepository.findAll();
        assertThat(managedAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSeedIsRequired() throws Exception {
        int databaseSizeBeforeTest = managedAccountRepository.findAll().size();
        // set the field null
        managedAccount.setSeed(null);

        // Create the ManagedAccount, which fails.

        restManagedAccountMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(managedAccount))
            )
            .andExpect(status().isBadRequest());

        List<ManagedAccount> managedAccountList = managedAccountRepository.findAll();
        assertThat(managedAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllManagedAccounts() throws Exception {
        // Initialize the database
        managedAccountRepository.saveAndFlush(managedAccount);

        // Get all the managedAccountList
        restManagedAccountMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(managedAccount.getId().intValue())))
            .andExpect(jsonPath("$.[*].accountName").value(hasItem(DEFAULT_ACCOUNT_NAME)))
            .andExpect(jsonPath("$.[*].ccy").value(hasItem(DEFAULT_CCY.toString())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].seed").value(hasItem(DEFAULT_SEED)));
    }

    @Test
    @Transactional
    void getManagedAccount() throws Exception {
        // Initialize the database
        managedAccountRepository.saveAndFlush(managedAccount);

        // Get the managedAccount
        restManagedAccountMockMvc
            .perform(get(ENTITY_API_URL_ID, managedAccount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(managedAccount.getId().intValue()))
            .andExpect(jsonPath("$.accountName").value(DEFAULT_ACCOUNT_NAME))
            .andExpect(jsonPath("$.ccy").value(DEFAULT_CCY.toString()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.seed").value(DEFAULT_SEED));
    }

    @Test
    @Transactional
    void getNonExistingManagedAccount() throws Exception {
        // Get the managedAccount
        restManagedAccountMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingManagedAccount() throws Exception {
        // Initialize the database
        managedAccountRepository.saveAndFlush(managedAccount);

        int databaseSizeBeforeUpdate = managedAccountRepository.findAll().size();

        // Update the managedAccount
        ManagedAccount updatedManagedAccount = managedAccountRepository.findById(managedAccount.getId()).get();
        // Disconnect from session so that the updates on updatedManagedAccount are not directly saved in db
        em.detach(updatedManagedAccount);
        updatedManagedAccount.accountName(UPDATED_ACCOUNT_NAME).ccy(UPDATED_CCY).address(UPDATED_ADDRESS).seed(UPDATED_SEED);

        restManagedAccountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedManagedAccount.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedManagedAccount))
            )
            .andExpect(status().isOk());

        // Validate the ManagedAccount in the database
        List<ManagedAccount> managedAccountList = managedAccountRepository.findAll();
        assertThat(managedAccountList).hasSize(databaseSizeBeforeUpdate);
        ManagedAccount testManagedAccount = managedAccountList.get(managedAccountList.size() - 1);
        assertThat(testManagedAccount.getAccountName()).isEqualTo(UPDATED_ACCOUNT_NAME);
        assertThat(testManagedAccount.getCcy()).isEqualTo(UPDATED_CCY);
        assertThat(testManagedAccount.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testManagedAccount.getSeed()).isEqualTo(UPDATED_SEED);
    }

    @Test
    @Transactional
    void putNonExistingManagedAccount() throws Exception {
        int databaseSizeBeforeUpdate = managedAccountRepository.findAll().size();
        managedAccount.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restManagedAccountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, managedAccount.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(managedAccount))
            )
            .andExpect(status().isBadRequest());

        // Validate the ManagedAccount in the database
        List<ManagedAccount> managedAccountList = managedAccountRepository.findAll();
        assertThat(managedAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchManagedAccount() throws Exception {
        int databaseSizeBeforeUpdate = managedAccountRepository.findAll().size();
        managedAccount.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restManagedAccountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(managedAccount))
            )
            .andExpect(status().isBadRequest());

        // Validate the ManagedAccount in the database
        List<ManagedAccount> managedAccountList = managedAccountRepository.findAll();
        assertThat(managedAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamManagedAccount() throws Exception {
        int databaseSizeBeforeUpdate = managedAccountRepository.findAll().size();
        managedAccount.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restManagedAccountMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(managedAccount)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ManagedAccount in the database
        List<ManagedAccount> managedAccountList = managedAccountRepository.findAll();
        assertThat(managedAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateManagedAccountWithPatch() throws Exception {
        // Initialize the database
        managedAccountRepository.saveAndFlush(managedAccount);

        int databaseSizeBeforeUpdate = managedAccountRepository.findAll().size();

        // Update the managedAccount using partial update
        ManagedAccount partialUpdatedManagedAccount = new ManagedAccount();
        partialUpdatedManagedAccount.setId(managedAccount.getId());

        partialUpdatedManagedAccount.accountName(UPDATED_ACCOUNT_NAME).ccy(UPDATED_CCY).address(UPDATED_ADDRESS).seed(UPDATED_SEED);

        restManagedAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedManagedAccount.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedManagedAccount))
            )
            .andExpect(status().isOk());

        // Validate the ManagedAccount in the database
        List<ManagedAccount> managedAccountList = managedAccountRepository.findAll();
        assertThat(managedAccountList).hasSize(databaseSizeBeforeUpdate);
        ManagedAccount testManagedAccount = managedAccountList.get(managedAccountList.size() - 1);
        assertThat(testManagedAccount.getAccountName()).isEqualTo(UPDATED_ACCOUNT_NAME);
        assertThat(testManagedAccount.getCcy()).isEqualTo(UPDATED_CCY);
        assertThat(testManagedAccount.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testManagedAccount.getSeed()).isEqualTo(UPDATED_SEED);
    }

    @Test
    @Transactional
    void fullUpdateManagedAccountWithPatch() throws Exception {
        // Initialize the database
        managedAccountRepository.saveAndFlush(managedAccount);

        int databaseSizeBeforeUpdate = managedAccountRepository.findAll().size();

        // Update the managedAccount using partial update
        ManagedAccount partialUpdatedManagedAccount = new ManagedAccount();
        partialUpdatedManagedAccount.setId(managedAccount.getId());

        partialUpdatedManagedAccount.accountName(UPDATED_ACCOUNT_NAME).ccy(UPDATED_CCY).address(UPDATED_ADDRESS).seed(UPDATED_SEED);

        restManagedAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedManagedAccount.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedManagedAccount))
            )
            .andExpect(status().isOk());

        // Validate the ManagedAccount in the database
        List<ManagedAccount> managedAccountList = managedAccountRepository.findAll();
        assertThat(managedAccountList).hasSize(databaseSizeBeforeUpdate);
        ManagedAccount testManagedAccount = managedAccountList.get(managedAccountList.size() - 1);
        assertThat(testManagedAccount.getAccountName()).isEqualTo(UPDATED_ACCOUNT_NAME);
        assertThat(testManagedAccount.getCcy()).isEqualTo(UPDATED_CCY);
        assertThat(testManagedAccount.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testManagedAccount.getSeed()).isEqualTo(UPDATED_SEED);
    }

    @Test
    @Transactional
    void patchNonExistingManagedAccount() throws Exception {
        int databaseSizeBeforeUpdate = managedAccountRepository.findAll().size();
        managedAccount.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restManagedAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, managedAccount.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(managedAccount))
            )
            .andExpect(status().isBadRequest());

        // Validate the ManagedAccount in the database
        List<ManagedAccount> managedAccountList = managedAccountRepository.findAll();
        assertThat(managedAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchManagedAccount() throws Exception {
        int databaseSizeBeforeUpdate = managedAccountRepository.findAll().size();
        managedAccount.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restManagedAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(managedAccount))
            )
            .andExpect(status().isBadRequest());

        // Validate the ManagedAccount in the database
        List<ManagedAccount> managedAccountList = managedAccountRepository.findAll();
        assertThat(managedAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamManagedAccount() throws Exception {
        int databaseSizeBeforeUpdate = managedAccountRepository.findAll().size();
        managedAccount.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restManagedAccountMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(managedAccount))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ManagedAccount in the database
        List<ManagedAccount> managedAccountList = managedAccountRepository.findAll();
        assertThat(managedAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteManagedAccount() throws Exception {
        // Initialize the database
        managedAccountRepository.saveAndFlush(managedAccount);

        int databaseSizeBeforeDelete = managedAccountRepository.findAll().size();

        // Delete the managedAccount
        restManagedAccountMockMvc
            .perform(delete(ENTITY_API_URL_ID, managedAccount.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ManagedAccount> managedAccountList = managedAccountRepository.findAll();
        assertThat(managedAccountList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
