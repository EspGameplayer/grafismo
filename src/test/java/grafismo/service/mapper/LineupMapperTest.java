package grafismo.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LineupMapperTest {

    private LineupMapper lineupMapper;

    @BeforeEach
    public void setUp() {
        lineupMapper = new LineupMapperImpl();
    }
}
