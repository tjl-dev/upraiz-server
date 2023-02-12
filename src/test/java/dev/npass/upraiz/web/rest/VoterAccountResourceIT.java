package dev.npass.upraiz.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import dev.npass.upraiz.IntegrationTest;
import dev.npass.upraiz.domain.VoterAccount;
import dev.npass.upraiz.domain.enumeration.VoteCcy;
import dev.npass.upraiz.repository.VoterAccountRepository;
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
 * Integration tests for the {@link VoterAccountResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VoterAccountResourceIT {

    private static final String DEFAULT_ACCOUNTNAME = "AAAAAAAAAA";
    private static final String UPDATED_ACCOUNTNAME = "BBBBBBBBBB";

    private static final VoteCcy DEFAULT_CCY = VoteCcy.XNO;
    private static final VoteCcy UPDATED_CCY = VoteCcy.XNO;

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/voter-accounts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VoterAccountRepository voterAccountRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVoterAccountMockMvc;

    private VoterAccount voterAccount;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VoterAccount createEntity(EntityManager em) {
        VoterAccount voterAccount = new VoterAccount().accountname(DEFAULT_ACCOUNTNAME).ccy(DEFAULT_CCY).address(DEFAULT_ADDRESS);
        return voterAccount;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VoterAccount createUpdatedEntity(EntityManager em) {
        VoterAccount voterAccount = new VoterAccount().accountname(UPDATED_ACCOUNTNAME).ccy(UPDATED_CCY).address(UPDATED_ADDRESS);
        return voterAccount;
    }

    @BeforeEach
    public void initTest() {
        voterAccount = createEntity(em);
    }

    @Test
    @Transactional
    void createVoterAccount() throws Exception {
        int databaseSizeBeforeCreate = voterAccountRepository.findAll().size();
        // Create the VoterAccount
        restVoterAccountMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(voterAccount)))
            .andExpect(status().isCreated());

        // Validate the VoterAccount in the database
        List<VoterAccount> voterAccountList = voterAccountRepository.findAll();
        assertThat(voterAccountList).hasSize(databaseSizeBeforeCreate + 1);
        VoterAccount testVoterAccount = voterAccountList.get(voterAccountList.size() - 1);
        assertThat(testVoterAccount.getAccountname()).isEqualTo(DEFAULT_ACCOUNTNAME);
        assertThat(testVoterAccount.getCcy()).isEqualTo(DEFAULT_CCY);
        assertThat(testVoterAccount.getAddress()).isEqualTo(DEFAULT_ADDRESS);
    }

    @Test
    @Transactional
    void createVoterAccountWithExistingId() throws Exception {
        // Create the VoterAccount with an existing ID
        voterAccount.setId(1L);

        int databaseSizeBeforeCreate = voterAccountRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVoterAccountMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(voterAccount)))
            .andExpect(status().isBadRequest());

        // Validate the VoterAccount in the database
        List<VoterAccount> voterAccountList = voterAccountRepository.findAll();
        assertThat(voterAccountList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCcyIsRequired() throws Exception {
        int databaseSizeBeforeTest = voterAccountRepository.findAll().size();
        // set the field null
        voterAccount.setCcy(null);

        // Create the VoterAccount, which fails.

        restVoterAccountMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(voterAccount)))
            .andExpect(status().isBadRequest());

        List<VoterAccount> voterAccountList = voterAccountRepository.findAll();
        assertThat(voterAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = voterAccountRepository.findAll().size();
        // set the field null
        voterAccount.setAddress(null);

        // Create the VoterAccount, which fails.

        restVoterAccountMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(voterAccount)))
            .andExpect(status().isBadRequest());

        List<VoterAccount> voterAccountList = voterAccountRepository.findAll();
        assertThat(voterAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllVoterAccounts() throws Exception {
        // Initialize the database
        voterAccountRepository.saveAndFlush(voterAccount);

        // Get all the voterAccountList
        restVoterAccountMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(voterAccount.getId().intValue())))
            .andExpect(jsonPath("$.[*].accountname").value(hasItem(DEFAULT_ACCOUNTNAME)))
            .andExpect(jsonPath("$.[*].ccy").value(hasItem(DEFAULT_CCY.toString())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)));
    }

    @Test
    @Transactional
    void getVoterAccount() throws Exception {
        // Initialize the database
        voterAccountRepository.saveAndFlush(voterAccount);

        // Get the voterAccount
        restVoterAccountMockMvc
            .perform(get(ENTITY_API_URL_ID, voterAccount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(voterAccount.getId().intValue()))
            .andExpect(jsonPath("$.accountname").value(DEFAULT_ACCOUNTNAME))
            .andExpect(jsonPath("$.ccy").value(DEFAULT_CCY.toString()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS));
    }

    @Test
    @Transactional
    void getNonExistingVoterAccount() throws Exception {
        // Get the voterAccount
        restVoterAccountMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingVoterAccount() throws Exception {
        // Initialize the database
        voterAccountRepository.saveAndFlush(voterAccount);

        int databaseSizeBeforeUpdate = voterAccountRepository.findAll().size();

        // Update the voterAccount
        VoterAccount updatedVoterAccount = voterAccountRepository.findById(voterAccount.getId()).get();
        // Disconnect from session so that the updates on updatedVoterAccount are not directly saved in db
        em.detach(updatedVoterAccount);
        updatedVoterAccount.accountname(UPDATED_ACCOUNTNAME).ccy(UPDATED_CCY).address(UPDATED_ADDRESS);

        restVoterAccountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedVoterAccount.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedVoterAccount))
            )
            .andExpect(status().isOk());

        // Validate the VoterAccount in the database
        List<VoterAccount> voterAccountList = voterAccountRepository.findAll();
        assertThat(voterAccountList).hasSize(databaseSizeBeforeUpdate);
        VoterAccount testVoterAccount = voterAccountList.get(voterAccountList.size() - 1);
        assertThat(testVoterAccount.getAccountname()).isEqualTo(UPDATED_ACCOUNTNAME);
        assertThat(testVoterAccount.getCcy()).isEqualTo(UPDATED_CCY);
        assertThat(testVoterAccount.getAddress()).isEqualTo(UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void putNonExistingVoterAccount() throws Exception {
        int databaseSizeBeforeUpdate = voterAccountRepository.findAll().size();
        voterAccount.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVoterAccountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, voterAccount.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(voterAccount))
            )
            .andExpect(status().isBadRequest());

        // Validate the VoterAccount in the database
        List<VoterAccount> voterAccountList = voterAccountRepository.findAll();
        assertThat(voterAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVoterAccount() throws Exception {
        int databaseSizeBeforeUpdate = voterAccountRepository.findAll().size();
        voterAccount.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVoterAccountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(voterAccount))
            )
            .andExpect(status().isBadRequest());

        // Validate the VoterAccount in the database
        List<VoterAccount> voterAccountList = voterAccountRepository.findAll();
        assertThat(voterAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVoterAccount() throws Exception {
        int databaseSizeBeforeUpdate = voterAccountRepository.findAll().size();
        voterAccount.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVoterAccountMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(voterAccount)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the VoterAccount in the database
        List<VoterAccount> voterAccountList = voterAccountRepository.findAll();
        assertThat(voterAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVoterAccountWithPatch() throws Exception {
        // Initialize the database
        voterAccountRepository.saveAndFlush(voterAccount);

        int databaseSizeBeforeUpdate = voterAccountRepository.findAll().size();

        // Update the voterAccount using partial update
        VoterAccount partialUpdatedVoterAccount = new VoterAccount();
        partialUpdatedVoterAccount.setId(voterAccount.getId());

        partialUpdatedVoterAccount.ccy(UPDATED_CCY).address(UPDATED_ADDRESS);

        restVoterAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVoterAccount.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVoterAccount))
            )
            .andExpect(status().isOk());

        // Validate the VoterAccount in the database
        List<VoterAccount> voterAccountList = voterAccountRepository.findAll();
        assertThat(voterAccountList).hasSize(databaseSizeBeforeUpdate);
        VoterAccount testVoterAccount = voterAccountList.get(voterAccountList.size() - 1);
        assertThat(testVoterAccount.getAccountname()).isEqualTo(DEFAULT_ACCOUNTNAME);
        assertThat(testVoterAccount.getCcy()).isEqualTo(UPDATED_CCY);
        assertThat(testVoterAccount.getAddress()).isEqualTo(UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void fullUpdateVoterAccountWithPatch() throws Exception {
        // Initialize the database
        voterAccountRepository.saveAndFlush(voterAccount);

        int databaseSizeBeforeUpdate = voterAccountRepository.findAll().size();

        // Update the voterAccount using partial update
        VoterAccount partialUpdatedVoterAccount = new VoterAccount();
        partialUpdatedVoterAccount.setId(voterAccount.getId());

        partialUpdatedVoterAccount.accountname(UPDATED_ACCOUNTNAME).ccy(UPDATED_CCY).address(UPDATED_ADDRESS);

        restVoterAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVoterAccount.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVoterAccount))
            )
            .andExpect(status().isOk());

        // Validate the VoterAccount in the database
        List<VoterAccount> voterAccountList = voterAccountRepository.findAll();
        assertThat(voterAccountList).hasSize(databaseSizeBeforeUpdate);
        VoterAccount testVoterAccount = voterAccountList.get(voterAccountList.size() - 1);
        assertThat(testVoterAccount.getAccountname()).isEqualTo(UPDATED_ACCOUNTNAME);
        assertThat(testVoterAccount.getCcy()).isEqualTo(UPDATED_CCY);
        assertThat(testVoterAccount.getAddress()).isEqualTo(UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void patchNonExistingVoterAccount() throws Exception {
        int databaseSizeBeforeUpdate = voterAccountRepository.findAll().size();
        voterAccount.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVoterAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, voterAccount.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(voterAccount))
            )
            .andExpect(status().isBadRequest());

        // Validate the VoterAccount in the database
        List<VoterAccount> voterAccountList = voterAccountRepository.findAll();
        assertThat(voterAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVoterAccount() throws Exception {
        int databaseSizeBeforeUpdate = voterAccountRepository.findAll().size();
        voterAccount.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVoterAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(voterAccount))
            )
            .andExpect(status().isBadRequest());

        // Validate the VoterAccount in the database
        List<VoterAccount> voterAccountList = voterAccountRepository.findAll();
        assertThat(voterAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVoterAccount() throws Exception {
        int databaseSizeBeforeUpdate = voterAccountRepository.findAll().size();
        voterAccount.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVoterAccountMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(voterAccount))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the VoterAccount in the database
        List<VoterAccount> voterAccountList = voterAccountRepository.findAll();
        assertThat(voterAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVoterAccount() throws Exception {
        // Initialize the database
        voterAccountRepository.saveAndFlush(voterAccount);

        int databaseSizeBeforeDelete = voterAccountRepository.findAll().size();

        // Delete the voterAccount
        restVoterAccountMockMvc
            .perform(delete(ENTITY_API_URL_ID, voterAccount.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<VoterAccount> voterAccountList = voterAccountRepository.findAll();
        assertThat(voterAccountList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
