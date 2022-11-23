package grafismo.domain;

import static org.assertj.core.api.Assertions.assertThat;

import grafismo.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StadiumTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Stadium.class);
        Stadium stadium1 = new Stadium();
        stadium1.setId(1L);
        Stadium stadium2 = new Stadium();
        stadium2.setId(stadium1.getId());
        assertThat(stadium1).isEqualTo(stadium2);
        stadium2.setId(2L);
        assertThat(stadium1).isNotEqualTo(stadium2);
        stadium1.setId(null);
        assertThat(stadium1).isNotEqualTo(stadium2);
    }
}
