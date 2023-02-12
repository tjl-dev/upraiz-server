package dev.npass.domain;

import static org.assertj.core.api.Assertions.assertThat;

import dev.npass.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VoteManagerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VoteManager.class);
        VoteManager voteManager1 = new VoteManager();
        voteManager1.setId(1L);
        VoteManager voteManager2 = new VoteManager();
        voteManager2.setId(voteManager1.getId());
        assertThat(voteManager1).isEqualTo(voteManager2);
        voteManager2.setId(2L);
        assertThat(voteManager1).isNotEqualTo(voteManager2);
        voteManager1.setId(null);
        assertThat(voteManager1).isNotEqualTo(voteManager2);
    }
}
