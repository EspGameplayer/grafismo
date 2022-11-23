package grafismo.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RefereeMapperTest {

    private RefereeMapper refereeMapper;

    @BeforeEach
    public void setUp() {
        refereeMapper = new RefereeMapperImpl();
    }
}
