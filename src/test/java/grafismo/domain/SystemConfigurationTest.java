package grafismo.domain;

import static org.assertj.core.api.Assertions.assertThat;

import grafismo.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SystemConfigurationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SystemConfiguration.class);
        SystemConfiguration systemConfiguration1 = new SystemConfiguration();
        systemConfiguration1.setId(1L);
        SystemConfiguration systemConfiguration2 = new SystemConfiguration();
        systemConfiguration2.setId(systemConfiguration1.getId());
        assertThat(systemConfiguration1).isEqualTo(systemConfiguration2);
        systemConfiguration2.setId(2L);
        assertThat(systemConfiguration1).isNotEqualTo(systemConfiguration2);
        systemConfiguration1.setId(null);
        assertThat(systemConfiguration1).isNotEqualTo(systemConfiguration2);
    }
}
