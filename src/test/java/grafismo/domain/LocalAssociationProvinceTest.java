package grafismo.domain;

import static org.assertj.core.api.Assertions.assertThat;

import grafismo.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LocalAssociationProvinceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LocalAssociationProvince.class);
        LocalAssociationProvince localAssociationProvince1 = new LocalAssociationProvince();
        localAssociationProvince1.setId(1L);
        LocalAssociationProvince localAssociationProvince2 = new LocalAssociationProvince();
        localAssociationProvince2.setId(localAssociationProvince1.getId());
        assertThat(localAssociationProvince1).isEqualTo(localAssociationProvince2);
        localAssociationProvince2.setId(2L);
        assertThat(localAssociationProvince1).isNotEqualTo(localAssociationProvince2);
        localAssociationProvince1.setId(null);
        assertThat(localAssociationProvince1).isNotEqualTo(localAssociationProvince2);
    }
}
