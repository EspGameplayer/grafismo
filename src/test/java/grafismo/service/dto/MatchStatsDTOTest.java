package grafismo.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import grafismo.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MatchStatsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MatchStatsDTO.class);
        MatchStatsDTO matchStatsDTO1 = new MatchStatsDTO();
        matchStatsDTO1.setId(1L);
        MatchStatsDTO matchStatsDTO2 = new MatchStatsDTO();
        assertThat(matchStatsDTO1).isNotEqualTo(matchStatsDTO2);
        matchStatsDTO2.setId(matchStatsDTO1.getId());
        assertThat(matchStatsDTO1).isEqualTo(matchStatsDTO2);
        matchStatsDTO2.setId(2L);
        assertThat(matchStatsDTO1).isNotEqualTo(matchStatsDTO2);
        matchStatsDTO1.setId(null);
        assertThat(matchStatsDTO1).isNotEqualTo(matchStatsDTO2);
    }
}
