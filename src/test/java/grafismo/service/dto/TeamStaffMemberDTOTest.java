package grafismo.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import grafismo.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TeamStaffMemberDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TeamStaffMemberDTO.class);
        TeamStaffMemberDTO teamStaffMemberDTO1 = new TeamStaffMemberDTO();
        teamStaffMemberDTO1.setId(1L);
        TeamStaffMemberDTO teamStaffMemberDTO2 = new TeamStaffMemberDTO();
        assertThat(teamStaffMemberDTO1).isNotEqualTo(teamStaffMemberDTO2);
        teamStaffMemberDTO2.setId(teamStaffMemberDTO1.getId());
        assertThat(teamStaffMemberDTO1).isEqualTo(teamStaffMemberDTO2);
        teamStaffMemberDTO2.setId(2L);
        assertThat(teamStaffMemberDTO1).isNotEqualTo(teamStaffMemberDTO2);
        teamStaffMemberDTO1.setId(null);
        assertThat(teamStaffMemberDTO1).isNotEqualTo(teamStaffMemberDTO2);
    }
}
