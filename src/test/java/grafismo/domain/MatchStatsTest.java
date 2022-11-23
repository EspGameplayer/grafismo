package grafismo.domain;

import static org.assertj.core.api.Assertions.assertThat;

import grafismo.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MatchStatsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MatchStats.class);
        MatchStats matchStats1 = new MatchStats();
        matchStats1.setId(1L);
        MatchStats matchStats2 = new MatchStats();
        matchStats2.setId(matchStats1.getId());
        assertThat(matchStats1).isEqualTo(matchStats2);
        matchStats2.setId(2L);
        assertThat(matchStats1).isNotEqualTo(matchStats2);
        matchStats1.setId(null);
        assertThat(matchStats1).isNotEqualTo(matchStats2);
    }
}
