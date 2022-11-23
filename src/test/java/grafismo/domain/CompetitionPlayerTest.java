package grafismo.domain;

import static org.assertj.core.api.Assertions.assertThat;

import grafismo.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CompetitionPlayerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CompetitionPlayer.class);
        CompetitionPlayer competitionPlayer1 = new CompetitionPlayer();
        competitionPlayer1.setId(1L);
        CompetitionPlayer competitionPlayer2 = new CompetitionPlayer();
        competitionPlayer2.setId(competitionPlayer1.getId());
        assertThat(competitionPlayer1).isEqualTo(competitionPlayer2);
        competitionPlayer2.setId(2L);
        assertThat(competitionPlayer1).isNotEqualTo(competitionPlayer2);
        competitionPlayer1.setId(null);
        assertThat(competitionPlayer1).isNotEqualTo(competitionPlayer2);
    }
}
