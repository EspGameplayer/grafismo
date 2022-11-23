package grafismo.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MatchStatsMapperTest {

    private MatchStatsMapper matchStatsMapper;

    @BeforeEach
    public void setUp() {
        matchStatsMapper = new MatchStatsMapperImpl();
    }
}
