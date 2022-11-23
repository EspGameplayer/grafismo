package grafismo.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BroadcastStaffMemberMapperTest {

    private BroadcastStaffMemberMapper broadcastStaffMemberMapper;

    @BeforeEach
    public void setUp() {
        broadcastStaffMemberMapper = new BroadcastStaffMemberMapperImpl();
    }
}
