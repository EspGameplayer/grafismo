package grafismo.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import grafismo.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MatchPlayerDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MatchPlayerDTO.class);
        MatchPlayerDTO matchPlayerDTO1 = new MatchPlayerDTO();
        matchPlayerDTO1.setId(1L);
        MatchPlayerDTO matchPlayerDTO2 = new MatchPlayerDTO();
        assertThat(matchPlayerDTO1).isNotEqualTo(matchPlayerDTO2);
        matchPlayerDTO2.setId(matchPlayerDTO1.getId());
        assertThat(matchPlayerDTO1).isEqualTo(matchPlayerDTO2);
        matchPlayerDTO2.setId(2L);
        assertThat(matchPlayerDTO1).isNotEqualTo(matchPlayerDTO2);
        matchPlayerDTO1.setId(null);
        assertThat(matchPlayerDTO1).isNotEqualTo(matchPlayerDTO2);
    }
}
