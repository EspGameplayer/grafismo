package grafismo.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LocalAssociationRegionMapperTest {

    private LocalAssociationRegionMapper localAssociationRegionMapper;

    @BeforeEach
    public void setUp() {
        localAssociationRegionMapper = new LocalAssociationRegionMapperImpl();
    }
}
