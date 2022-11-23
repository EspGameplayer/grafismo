package grafismo.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import grafismo.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ActionKeyDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ActionKeyDTO.class);
        ActionKeyDTO actionKeyDTO1 = new ActionKeyDTO();
        actionKeyDTO1.setId(1L);
        ActionKeyDTO actionKeyDTO2 = new ActionKeyDTO();
        assertThat(actionKeyDTO1).isNotEqualTo(actionKeyDTO2);
        actionKeyDTO2.setId(actionKeyDTO1.getId());
        assertThat(actionKeyDTO1).isEqualTo(actionKeyDTO2);
        actionKeyDTO2.setId(2L);
        assertThat(actionKeyDTO1).isNotEqualTo(actionKeyDTO2);
        actionKeyDTO1.setId(null);
        assertThat(actionKeyDTO1).isNotEqualTo(actionKeyDTO2);
    }
}
