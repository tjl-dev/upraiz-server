package dev.npass.upraiz.domain;

import static org.assertj.core.api.Assertions.assertThat;

import dev.npass.upraiz.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VoteManagerPreferencesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VoteManagerPreferences.class);
        VoteManagerPreferences voteManagerPreferences1 = new VoteManagerPreferences();
        voteManagerPreferences1.setId(1L);
        VoteManagerPreferences voteManagerPreferences2 = new VoteManagerPreferences();
        voteManagerPreferences2.setId(voteManagerPreferences1.getId());
        assertThat(voteManagerPreferences1).isEqualTo(voteManagerPreferences2);
        voteManagerPreferences2.setId(2L);
        assertThat(voteManagerPreferences1).isNotEqualTo(voteManagerPreferences2);
        voteManagerPreferences1.setId(null);
        assertThat(voteManagerPreferences1).isNotEqualTo(voteManagerPreferences2);
    }
}
