package grafismo.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import grafismo.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CompetitionPlayerDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CompetitionPlayerDTO.class);
        CompetitionPlayerDTO competitionPlayerDTO1 = new CompetitionPlayerDTO();
        competitionPlayerDTO1.setId(1L);
        CompetitionPlayerDTO competitionPlayerDTO2 = new CompetitionPlayerDTO();
        assertThat(competitionPlayerDTO1).isNotEqualTo(competitionPlayerDTO2);
        competitionPlayerDTO2.setId(competitionPlayerDTO1.getId());
        assertThat(competitionPlayerDTO1).isEqualTo(competitionPlayerDTO2);
        competitionPlayerDTO2.setId(2L);
        assertThat(competitionPlayerDTO1).isNotEqualTo(competitionPlayerDTO2);
        competitionPlayerDTO1.setId(null);
        assertThat(competitionPlayerDTO1).isNotEqualTo(competitionPlayerDTO2);
    }
}
