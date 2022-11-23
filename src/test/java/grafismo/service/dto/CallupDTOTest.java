package grafismo.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import grafismo.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CallupDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CallupDTO.class);
        CallupDTO callupDTO1 = new CallupDTO();
        callupDTO1.setId(1L);
        CallupDTO callupDTO2 = new CallupDTO();
        assertThat(callupDTO1).isNotEqualTo(callupDTO2);
        callupDTO2.setId(callupDTO1.getId());
        assertThat(callupDTO1).isEqualTo(callupDTO2);
        callupDTO2.setId(2L);
        assertThat(callupDTO1).isNotEqualTo(callupDTO2);
        callupDTO1.setId(null);
        assertThat(callupDTO1).isNotEqualTo(callupDTO2);
    }
}
