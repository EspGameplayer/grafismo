package grafismo.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import grafismo.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AssociationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AssociationDTO.class);
        AssociationDTO associationDTO1 = new AssociationDTO();
        associationDTO1.setId(1L);
        AssociationDTO associationDTO2 = new AssociationDTO();
        assertThat(associationDTO1).isNotEqualTo(associationDTO2);
        associationDTO2.setId(associationDTO1.getId());
        assertThat(associationDTO1).isEqualTo(associationDTO2);
        associationDTO2.setId(2L);
        assertThat(associationDTO1).isNotEqualTo(associationDTO2);
        associationDTO1.setId(null);
        assertThat(associationDTO1).isNotEqualTo(associationDTO2);
    }
}
