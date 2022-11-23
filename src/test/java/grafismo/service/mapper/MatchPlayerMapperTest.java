package grafismo.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MatchPlayerMapperTest {

    private MatchPlayerMapper matchPlayerMapper;

    @BeforeEach
    public void setUp() {
        matchPlayerMapper = new MatchPlayerMapperImpl();
    }
}
