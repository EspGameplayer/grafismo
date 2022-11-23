package grafismo.domain;

import static org.assertj.core.api.Assertions.assertThat;

import grafismo.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GraphicElementPosTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GraphicElementPos.class);
        GraphicElementPos graphicElementPos1 = new GraphicElementPos();
        graphicElementPos1.setId(1L);
        GraphicElementPos graphicElementPos2 = new GraphicElementPos();
        graphicElementPos2.setId(graphicElementPos1.getId());
        assertThat(graphicElementPos1).isEqualTo(graphicElementPos2);
        graphicElementPos2.setId(2L);
        assertThat(graphicElementPos1).isNotEqualTo(graphicElementPos2);
        graphicElementPos1.setId(null);
        assertThat(graphicElementPos1).isNotEqualTo(graphicElementPos2);
    }
}
