package grafismo.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import grafismo.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ShirtDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ShirtDTO.class);
        ShirtDTO shirtDTO1 = new ShirtDTO();
        shirtDTO1.setId(1L);
        ShirtDTO shirtDTO2 = new ShirtDTO();
        assertThat(shirtDTO1).isNotEqualTo(shirtDTO2);
        shirtDTO2.setId(shirtDTO1.getId());
        assertThat(shirtDTO1).isEqualTo(shirtDTO2);
        shirtDTO2.setId(2L);
        assertThat(shirtDTO1).isNotEqualTo(shirtDTO2);
        shirtDTO1.setId(null);
        assertThat(shirtDTO1).isNotEqualTo(shirtDTO2);
    }
}
