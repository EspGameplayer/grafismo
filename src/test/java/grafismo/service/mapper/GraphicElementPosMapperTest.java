package grafismo.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GraphicElementPosMapperTest {

    private GraphicElementPosMapper graphicElementPosMapper;

    @BeforeEach
    public void setUp() {
        graphicElementPosMapper = new GraphicElementPosMapperImpl();
    }
}
