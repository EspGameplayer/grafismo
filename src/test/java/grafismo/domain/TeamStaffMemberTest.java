package grafismo.domain;

import static org.assertj.core.api.Assertions.assertThat;

import grafismo.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TeamStaffMemberTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TeamStaffMember.class);
        TeamStaffMember teamStaffMember1 = new TeamStaffMember();
        teamStaffMember1.setId(1L);
        TeamStaffMember teamStaffMember2 = new TeamStaffMember();
        teamStaffMember2.setId(teamStaffMember1.getId());
        assertThat(teamStaffMember1).isEqualTo(teamStaffMember2);
        teamStaffMember2.setId(2L);
        assertThat(teamStaffMember1).isNotEqualTo(teamStaffMember2);
        teamStaffMember1.setId(null);
        assertThat(teamStaffMember1).isNotEqualTo(teamStaffMember2);
    }
}
