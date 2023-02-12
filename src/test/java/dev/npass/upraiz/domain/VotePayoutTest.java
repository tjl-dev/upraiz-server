package dev.npass.upraiz.domain;

import static org.assertj.core.api.Assertions.assertThat;

import dev.npass.upraiz.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VotePayoutTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VotePayout.class);
        VotePayout votePayout1 = new VotePayout();
        votePayout1.setId(1L);
        VotePayout votePayout2 = new VotePayout();
        votePayout2.setId(votePayout1.getId());
        assertThat(votePayout1).isEqualTo(votePayout2);
        votePayout2.setId(2L);
        assertThat(votePayout1).isNotEqualTo(votePayout2);
        votePayout1.setId(null);
        assertThat(votePayout1).isNotEqualTo(votePayout2);
    }
}
