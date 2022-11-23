package grafismo.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import grafismo.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LineupDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LineupDTO.class);
        LineupDTO lineupDTO1 = new LineupDTO();
        lineupDTO1.setId(1L);
        LineupDTO lineupDTO2 = new LineupDTO();
        assertThat(lineupDTO1).isNotEqualTo(lineupDTO2);
        lineupDTO2.setId(lineupDTO1.getId());
        assertThat(lineupDTO1).isEqualTo(lineupDTO2);
        lineupDTO2.setId(2L);
        assertThat(lineupDTO1).isNotEqualTo(lineupDTO2);
        lineupDTO1.setId(null);
        assertThat(lineupDTO1).isNotEqualTo(lineupDTO2);
    }
}
