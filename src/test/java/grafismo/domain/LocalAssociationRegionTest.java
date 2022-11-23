package grafismo.domain;

import static org.assertj.core.api.Assertions.assertThat;

import grafismo.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LocalAssociationRegionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LocalAssociationRegion.class);
        LocalAssociationRegion localAssociationRegion1 = new LocalAssociationRegion();
        localAssociationRegion1.setId(1L);
        LocalAssociationRegion localAssociationRegion2 = new LocalAssociationRegion();
        localAssociationRegion2.setId(localAssociationRegion1.getId());
        assertThat(localAssociationRegion1).isEqualTo(localAssociationRegion2);
        localAssociationRegion2.setId(2L);
        assertThat(localAssociationRegion1).isNotEqualTo(localAssociationRegion2);
        localAssociationRegion1.setId(null);
        assertThat(localAssociationRegion1).isNotEqualTo(localAssociationRegion2);
    }
}
