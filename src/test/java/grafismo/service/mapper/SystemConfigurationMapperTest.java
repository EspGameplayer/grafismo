package grafismo.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SystemConfigurationMapperTest {

    private SystemConfigurationMapper systemConfigurationMapper;

    @BeforeEach
    public void setUp() {
        systemConfigurationMapper = new SystemConfigurationMapperImpl();
    }
}
