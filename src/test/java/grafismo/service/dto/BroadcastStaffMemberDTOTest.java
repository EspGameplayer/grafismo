package grafismo.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import grafismo.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BroadcastStaffMemberDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BroadcastStaffMemberDTO.class);
        BroadcastStaffMemberDTO broadcastStaffMemberDTO1 = new BroadcastStaffMemberDTO();
        broadcastStaffMemberDTO1.setId(1L);
        BroadcastStaffMemberDTO broadcastStaffMemberDTO2 = new BroadcastStaffMemberDTO();
        assertThat(broadcastStaffMemberDTO1).isNotEqualTo(broadcastStaffMemberDTO2);
        broadcastStaffMemberDTO2.setId(broadcastStaffMemberDTO1.getId());
        assertThat(broadcastStaffMemberDTO1).isEqualTo(broadcastStaffMemberDTO2);
        broadcastStaffMemberDTO2.setId(2L);
        assertThat(broadcastStaffMemberDTO1).isNotEqualTo(broadcastStaffMemberDTO2);
        broadcastStaffMemberDTO1.setId(null);
        assertThat(broadcastStaffMemberDTO1).isNotEqualTo(broadcastStaffMemberDTO2);
    }
}
