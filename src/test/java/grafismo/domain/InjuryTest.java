package grafismo.domain;

import static org.assertj.core.api.Assertions.assertThat;

import grafismo.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InjuryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Injury.class);
        Injury injury1 = new Injury();
        injury1.setId(1L);
        Injury injury2 = new Injury();
        injury2.setId(injury1.getId());
        assertThat(injury1).isEqualTo(injury2);
        injury2.setId(2L);
        assertThat(injury1).isNotEqualTo(injury2);
        injury1.setId(null);
        assertThat(injury1).isNotEqualTo(injury2);
    }
}
