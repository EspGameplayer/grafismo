package grafismo.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import grafismo.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RefereeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RefereeDTO.class);
        RefereeDTO refereeDTO1 = new RefereeDTO();
        refereeDTO1.setId(1L);
        RefereeDTO refereeDTO2 = new RefereeDTO();
        assertThat(refereeDTO1).isNotEqualTo(refereeDTO2);
        refereeDTO2.setId(refereeDTO1.getId());
        assertThat(refereeDTO1).isEqualTo(refereeDTO2);
        refereeDTO2.setId(2L);
        assertThat(refereeDTO1).isNotEqualTo(refereeDTO2);
        refereeDTO1.setId(null);
        assertThat(refereeDTO1).isNotEqualTo(refereeDTO2);
    }
}
