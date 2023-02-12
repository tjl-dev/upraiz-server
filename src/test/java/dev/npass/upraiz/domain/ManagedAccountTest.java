package dev.npass.upraiz.domain;

import static org.assertj.core.api.Assertions.assertThat;

import dev.npass.upraiz.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ManagedAccountTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ManagedAccount.class);
        ManagedAccount managedAccount1 = new ManagedAccount();
        managedAccount1.setId(1L);
        ManagedAccount managedAccount2 = new ManagedAccount();
        managedAccount2.setId(managedAccount1.getId());
        assertThat(managedAccount1).isEqualTo(managedAccount2);
        managedAccount2.setId(2L);
        assertThat(managedAccount1).isNotEqualTo(managedAccount2);
        managedAccount1.setId(null);
        assertThat(managedAccount1).isNotEqualTo(managedAccount2);
    }
}
