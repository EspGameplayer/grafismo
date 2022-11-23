package grafismo.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TeamStaffMemberMapperTest {

    private TeamStaffMemberMapper teamStaffMemberMapper;

    @BeforeEach
    public void setUp() {
        teamStaffMemberMapper = new TeamStaffMemberMapperImpl();
    }
}
