package grafismo.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import grafismo.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GraphicElementPosDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(GraphicElementPosDTO.class);
        GraphicElementPosDTO graphicElementPosDTO1 = new GraphicElementPosDTO();
        graphicElementPosDTO1.setId(1L);
        GraphicElementPosDTO graphicElementPosDTO2 = new GraphicElementPosDTO();
        assertThat(graphicElementPosDTO1).isNotEqualTo(graphicElementPosDTO2);
        graphicElementPosDTO2.setId(graphicElementPosDTO1.getId());
        assertThat(graphicElementPosDTO1).isEqualTo(graphicElementPosDTO2);
        graphicElementPosDTO2.setId(2L);
        assertThat(graphicElementPosDTO1).isNotEqualTo(graphicElementPosDTO2);
        graphicElementPosDTO1.setId(null);
        assertThat(graphicElementPosDTO1).isNotEqualTo(graphicElementPosDTO2);
    }
}
