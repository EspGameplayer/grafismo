package grafismo.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ShirtMapperTest {

    private ShirtMapper shirtMapper;

    @BeforeEach
    public void setUp() {
        shirtMapper = new ShirtMapperImpl();
    }
}
