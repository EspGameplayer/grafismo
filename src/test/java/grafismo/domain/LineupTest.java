package grafismo.domain;

import static org.assertj.core.api.Assertions.assertThat;

import grafismo.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LineupTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Lineup.class);
        Lineup lineup1 = new Lineup();
        lineup1.setId(1L);
        Lineup lineup2 = new Lineup();
        lineup2.setId(lineup1.getId());
        assertThat(lineup1).isEqualTo(lineup2);
        lineup2.setId(2L);
        assertThat(lineup1).isNotEqualTo(lineup2);
        lineup1.setId(null);
        assertThat(lineup1).isNotEqualTo(lineup2);
    }
}
