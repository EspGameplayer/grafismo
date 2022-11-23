package grafismo.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MatchdayMapperTest {

    private MatchdayMapper matchdayMapper;

    @BeforeEach
    public void setUp() {
        matchdayMapper = new MatchdayMapperImpl();
    }
}
