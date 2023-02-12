package dev.npass.domain;

import static org.assertj.core.api.Assertions.assertThat;

import dev.npass.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AccountReclaimRequestTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AccountReclaimRequest.class);
        AccountReclaimRequest accountReclaimRequest1 = new AccountReclaimRequest();
        accountReclaimRequest1.setId(1L);
        AccountReclaimRequest accountReclaimRequest2 = new AccountReclaimRequest();
        accountReclaimRequest2.setId(accountReclaimRequest1.getId());
        assertThat(accountReclaimRequest1).isEqualTo(accountReclaimRequest2);
        accountReclaimRequest2.setId(2L);
        assertThat(accountReclaimRequest1).isNotEqualTo(accountReclaimRequest2);
        accountReclaimRequest1.setId(null);
        assertThat(accountReclaimRequest1).isNotEqualTo(accountReclaimRequest2);
    }
}
