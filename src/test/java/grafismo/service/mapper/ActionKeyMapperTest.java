package grafismo.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ActionKeyMapperTest {

    private ActionKeyMapper actionKeyMapper;

    @BeforeEach
    public void setUp() {
        actionKeyMapper = new ActionKeyMapperImpl();
    }
}
