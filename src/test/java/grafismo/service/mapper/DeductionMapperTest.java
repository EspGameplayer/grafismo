package grafismo.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DeductionMapperTest {

    private DeductionMapper deductionMapper;

    @BeforeEach
    public void setUp() {
        deductionMapper = new DeductionMapperImpl();
    }
}
