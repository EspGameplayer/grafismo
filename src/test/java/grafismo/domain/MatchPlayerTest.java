package grafismo.domain;

import static org.assertj.core.api.Assertions.assertThat;

import grafismo.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MatchPlayerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MatchPlayer.class);
        MatchPlayer matchPlayer1 = new MatchPlayer();
        matchPlayer1.setId(1L);
        MatchPlayer matchPlayer2 = new MatchPlayer();
        matchPlayer2.setId(matchPlayer1.getId());
        assertThat(matchPlayer1).isEqualTo(matchPlayer2);
        matchPlayer2.setId(2L);
        assertThat(matchPlayer1).isNotEqualTo(matchPlayer2);
        matchPlayer1.setId(null);
        assertThat(matchPlayer1).isNotEqualTo(matchPlayer2);
    }
}
