package dev.npass.domain;

import static org.assertj.core.api.Assertions.assertThat;

import dev.npass.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AccountReclaimPayoutTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AccountReclaimPayout.class);
        AccountReclaimPayout accountReclaimPayout1 = new AccountReclaimPayout();
        accountReclaimPayout1.setId(1L);
        AccountReclaimPayout accountReclaimPayout2 = new AccountReclaimPayout();
        accountReclaimPayout2.setId(accountReclaimPayout1.getId());
        assertThat(accountReclaimPayout1).isEqualTo(accountReclaimPayout2);
        accountReclaimPayout2.setId(2L);
        assertThat(accountReclaimPayout1).isNotEqualTo(accountReclaimPayout2);
        accountReclaimPayout1.setId(null);
        assertThat(accountReclaimPayout1).isNotEqualTo(accountReclaimPayout2);
    }
}
