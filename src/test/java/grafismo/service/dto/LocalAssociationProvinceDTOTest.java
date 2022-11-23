package grafismo.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import grafismo.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LocalAssociationProvinceDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LocalAssociationProvinceDTO.class);
        LocalAssociationProvinceDTO localAssociationProvinceDTO1 = new LocalAssociationProvinceDTO();
        localAssociationProvinceDTO1.setId(1L);
        LocalAssociationProvinceDTO localAssociationProvinceDTO2 = new LocalAssociationProvinceDTO();
        assertThat(localAssociationProvinceDTO1).isNotEqualTo(localAssociationProvinceDTO2);
        localAssociationProvinceDTO2.setId(localAssociationProvinceDTO1.getId());
        assertThat(localAssociationProvinceDTO1).isEqualTo(localAssociationProvinceDTO2);
        localAssociationProvinceDTO2.setId(2L);
        assertThat(localAssociationProvinceDTO1).isNotEqualTo(localAssociationProvinceDTO2);
        localAssociationProvinceDTO1.setId(null);
        assertThat(localAssociationProvinceDTO1).isNotEqualTo(localAssociationProvinceDTO2);
    }
}
