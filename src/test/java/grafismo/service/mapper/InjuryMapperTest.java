package grafismo.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InjuryMapperTest {

    private InjuryMapper injuryMapper;

    @BeforeEach
    public void setUp() {
        injuryMapper = new InjuryMapperImpl();
    }
}
