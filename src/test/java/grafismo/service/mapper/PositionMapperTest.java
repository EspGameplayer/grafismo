package grafismo.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PositionMapperTest {

    private PositionMapper positionMapper;

    @BeforeEach
    public void setUp() {
        positionMapper = new PositionMapperImpl();
    }
}
