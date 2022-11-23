package grafismo.domain;

import static org.assertj.core.api.Assertions.assertThat;

import grafismo.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RefereeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Referee.class);
        Referee referee1 = new Referee();
        referee1.setId(1L);
        Referee referee2 = new Referee();
        referee2.setId(referee1.getId());
        assertThat(referee1).isEqualTo(referee2);
        referee2.setId(2L);
        assertThat(referee1).isNotEqualTo(referee2);
        referee1.setId(null);
        assertThat(referee1).isNotEqualTo(referee2);
    }
}
