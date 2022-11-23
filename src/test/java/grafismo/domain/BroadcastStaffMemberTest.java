package grafismo.domain;

import static org.assertj.core.api.Assertions.assertThat;

import grafismo.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BroadcastStaffMemberTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BroadcastStaffMember.class);
        BroadcastStaffMember broadcastStaffMember1 = new BroadcastStaffMember();
        broadcastStaffMember1.setId(1L);
        BroadcastStaffMember broadcastStaffMember2 = new BroadcastStaffMember();
        broadcastStaffMember2.setId(broadcastStaffMember1.getId());
        assertThat(broadcastStaffMember1).isEqualTo(broadcastStaffMember2);
        broadcastStaffMember2.setId(2L);
        assertThat(broadcastStaffMember1).isNotEqualTo(broadcastStaffMember2);
        broadcastStaffMember1.setId(null);
        assertThat(broadcastStaffMember1).isNotEqualTo(broadcastStaffMember2);
    }
}
