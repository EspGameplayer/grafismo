package grafismo.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import grafismo.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InjuryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InjuryDTO.class);
        InjuryDTO injuryDTO1 = new InjuryDTO();
        injuryDTO1.setId(1L);
        InjuryDTO injuryDTO2 = new InjuryDTO();
        assertThat(injuryDTO1).isNotEqualTo(injuryDTO2);
        injuryDTO2.setId(injuryDTO1.getId());
        assertThat(injuryDTO1).isEqualTo(injuryDTO2);
        injuryDTO2.setId(2L);
        assertThat(injuryDTO1).isNotEqualTo(injuryDTO2);
        injuryDTO1.setId(null);
        assertThat(injuryDTO1).isNotEqualTo(injuryDTO2);
    }
}
