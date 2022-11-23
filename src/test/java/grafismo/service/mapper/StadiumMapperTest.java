package grafismo.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StadiumMapperTest {

    private StadiumMapper stadiumMapper;

    @BeforeEach
    public void setUp() {
        stadiumMapper = new StadiumMapperImpl();
    }
}
