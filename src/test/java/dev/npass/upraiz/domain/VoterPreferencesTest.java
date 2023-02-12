package dev.npass.upraiz.domain;

import static org.assertj.core.api.Assertions.assertThat;

import dev.npass.upraiz.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VoterPreferencesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VoterPreferences.class);
        VoterPreferences voterPreferences1 = new VoterPreferences();
        voterPreferences1.setId(1L);
        VoterPreferences voterPreferences2 = new VoterPreferences();
        voterPreferences2.setId(voterPreferences1.getId());
        assertThat(voterPreferences1).isEqualTo(voterPreferences2);
        voterPreferences2.setId(2L);
        assertThat(voterPreferences1).isNotEqualTo(voterPreferences2);
        voterPreferences1.setId(null);
        assertThat(voterPreferences1).isNotEqualTo(voterPreferences2);
    }
}
