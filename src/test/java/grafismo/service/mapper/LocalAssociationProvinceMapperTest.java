package grafismo.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LocalAssociationProvinceMapperTest {

    private LocalAssociationProvinceMapper localAssociationProvinceMapper;

    @BeforeEach
    public void setUp() {
        localAssociationProvinceMapper = new LocalAssociationProvinceMapperImpl();
    }
}
