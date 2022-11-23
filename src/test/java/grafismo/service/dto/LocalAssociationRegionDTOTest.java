package grafismo.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import grafismo.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LocalAssociationRegionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LocalAssociationRegionDTO.class);
        LocalAssociationRegionDTO localAssociationRegionDTO1 = new LocalAssociationRegionDTO();
        localAssociationRegionDTO1.setId(1L);
        LocalAssociationRegionDTO localAssociationRegionDTO2 = new LocalAssociationRegionDTO();
        assertThat(localAssociationRegionDTO1).isNotEqualTo(localAssociationRegionDTO2);
        localAssociationRegionDTO2.setId(localAssociationRegionDTO1.getId());
        assertThat(localAssociationRegionDTO1).isEqualTo(localAssociationRegionDTO2);
        localAssociationRegionDTO2.setId(2L);
        assertThat(localAssociationRegionDTO1).isNotEqualTo(localAssociationRegionDTO2);
        localAssociationRegionDTO1.setId(null);
        assertThat(localAssociationRegionDTO1).isNotEqualTo(localAssociationRegionDTO2);
    }
}
