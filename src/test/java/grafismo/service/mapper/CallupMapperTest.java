package grafismo.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CallupMapperTest {

    private CallupMapper callupMapper;

    @BeforeEach
    public void setUp() {
        callupMapper = new CallupMapperImpl();
    }
}
