package grafismo.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AssociationMapperTest {

    private AssociationMapper associationMapper;

    @BeforeEach
    public void setUp() {
        associationMapper = new AssociationMapperImpl();
    }
}
