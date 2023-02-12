package dev.npass.upraiz.domain;

import static org.assertj.core.api.Assertions.assertThat;

import dev.npass.upraiz.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VoterAccountTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VoterAccount.class);
        VoterAccount voterAccount1 = new VoterAccount();
        voterAccount1.setId(1L);
        VoterAccount voterAccount2 = new VoterAccount();
        voterAccount2.setId(voterAccount1.getId());
        assertThat(voterAccount1).isEqualTo(voterAccount2);
        voterAccount2.setId(2L);
        assertThat(voterAccount1).isNotEqualTo(voterAccount2);
        voterAccount1.setId(null);
        assertThat(voterAccount1).isNotEqualTo(voterAccount2);
    }
}
