package grafismo.domain;

import static org.assertj.core.api.Assertions.assertThat;

import grafismo.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ShirtTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Shirt.class);
        Shirt shirt1 = new Shirt();
        shirt1.setId(1L);
        Shirt shirt2 = new Shirt();
        shirt2.setId(shirt1.getId());
        assertThat(shirt1).isEqualTo(shirt2);
        shirt2.setId(2L);
        assertThat(shirt1).isNotEqualTo(shirt2);
        shirt1.setId(null);
        assertThat(shirt1).isNotEqualTo(shirt2);
    }
}
