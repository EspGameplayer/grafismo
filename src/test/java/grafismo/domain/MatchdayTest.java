package grafismo.domain;

import static org.assertj.core.api.Assertions.assertThat;

import grafismo.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MatchdayTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Matchday.class);
        Matchday matchday1 = new Matchday();
        matchday1.setId(1L);
        Matchday matchday2 = new Matchday();
        matchday2.setId(matchday1.getId());
        assertThat(matchday1).isEqualTo(matchday2);
        matchday2.setId(2L);
        assertThat(matchday1).isNotEqualTo(matchday2);
        matchday1.setId(null);
        assertThat(matchday1).isNotEqualTo(matchday2);
    }
}
