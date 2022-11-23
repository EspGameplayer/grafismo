package grafismo.domain;

import static org.assertj.core.api.Assertions.assertThat;

import grafismo.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ActionKeyTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ActionKey.class);
        ActionKey actionKey1 = new ActionKey();
        actionKey1.setId(1L);
        ActionKey actionKey2 = new ActionKey();
        actionKey2.setId(actionKey1.getId());
        assertThat(actionKey1).isEqualTo(actionKey2);
        actionKey2.setId(2L);
        assertThat(actionKey1).isNotEqualTo(actionKey2);
        actionKey1.setId(null);
        assertThat(actionKey1).isNotEqualTo(actionKey2);
    }
}
