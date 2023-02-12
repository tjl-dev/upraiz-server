package dev.npass.upraiz.domain;

import static org.assertj.core.api.Assertions.assertThat;

import dev.npass.upraiz.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VoteTargetTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VoteTarget.class);
        VoteTarget voteTarget1 = new VoteTarget();
        voteTarget1.setId(1L);
        VoteTarget voteTarget2 = new VoteTarget();
        voteTarget2.setId(voteTarget1.getId());
        assertThat(voteTarget1).isEqualTo(voteTarget2);
        voteTarget2.setId(2L);
        assertThat(voteTarget1).isNotEqualTo(voteTarget2);
        voteTarget1.setId(null);
        assertThat(voteTarget1).isNotEqualTo(voteTarget2);
    }
}
