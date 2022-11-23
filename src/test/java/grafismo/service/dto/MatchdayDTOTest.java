package grafismo.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import grafismo.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MatchdayDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MatchdayDTO.class);
        MatchdayDTO matchdayDTO1 = new MatchdayDTO();
        matchdayDTO1.setId(1L);
        MatchdayDTO matchdayDTO2 = new MatchdayDTO();
        assertThat(matchdayDTO1).isNotEqualTo(matchdayDTO2);
        matchdayDTO2.setId(matchdayDTO1.getId());
        assertThat(matchdayDTO1).isEqualTo(matchdayDTO2);
        matchdayDTO2.setId(2L);
        assertThat(matchdayDTO1).isNotEqualTo(matchdayDTO2);
        matchdayDTO1.setId(null);
        assertThat(matchdayDTO1).isNotEqualTo(matchdayDTO2);
    }
}
