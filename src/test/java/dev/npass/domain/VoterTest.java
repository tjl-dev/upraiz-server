package dev.npass.domain;

import static org.assertj.core.api.Assertions.assertThat;

import dev.npass.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VoterTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Voter.class);
        Voter voter1 = new Voter();
        voter1.setId(1L);
        Voter voter2 = new Voter();
        voter2.setId(voter1.getId());
        assertThat(voter1).isEqualTo(voter2);
        voter2.setId(2L);
        assertThat(voter1).isNotEqualTo(voter2);
        voter1.setId(null);
        assertThat(voter1).isNotEqualTo(voter2);
    }
}
