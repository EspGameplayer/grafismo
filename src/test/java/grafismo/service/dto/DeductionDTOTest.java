package grafismo.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import grafismo.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DeductionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DeductionDTO.class);
        DeductionDTO deductionDTO1 = new DeductionDTO();
        deductionDTO1.setId(1L);
        DeductionDTO deductionDTO2 = new DeductionDTO();
        assertThat(deductionDTO1).isNotEqualTo(deductionDTO2);
        deductionDTO2.setId(deductionDTO1.getId());
        assertThat(deductionDTO1).isEqualTo(deductionDTO2);
        deductionDTO2.setId(2L);
        assertThat(deductionDTO1).isNotEqualTo(deductionDTO2);
        deductionDTO1.setId(null);
        assertThat(deductionDTO1).isNotEqualTo(deductionDTO2);
    }
}
