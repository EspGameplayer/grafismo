package grafismo.domain;

import static org.assertj.core.api.Assertions.assertThat;

import grafismo.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CallupTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Callup.class);
        Callup callup1 = new Callup();
        callup1.setId(1L);
        Callup callup2 = new Callup();
        callup2.setId(callup1.getId());
        assertThat(callup1).isEqualTo(callup2);
        callup2.setId(2L);
        assertThat(callup1).isNotEqualTo(callup2);
        callup1.setId(null);
        assertThat(callup1).isNotEqualTo(callup2);
    }
}
