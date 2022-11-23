package grafismo.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import grafismo.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SystemConfigurationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SystemConfigurationDTO.class);
        SystemConfigurationDTO systemConfigurationDTO1 = new SystemConfigurationDTO();
        systemConfigurationDTO1.setId(1L);
        SystemConfigurationDTO systemConfigurationDTO2 = new SystemConfigurationDTO();
        assertThat(systemConfigurationDTO1).isNotEqualTo(systemConfigurationDTO2);
        systemConfigurationDTO2.setId(systemConfigurationDTO1.getId());
        assertThat(systemConfigurationDTO1).isEqualTo(systemConfigurationDTO2);
        systemConfigurationDTO2.setId(2L);
        assertThat(systemConfigurationDTO1).isNotEqualTo(systemConfigurationDTO2);
        systemConfigurationDTO1.setId(null);
        assertThat(systemConfigurationDTO1).isNotEqualTo(systemConfigurationDTO2);
    }
}
