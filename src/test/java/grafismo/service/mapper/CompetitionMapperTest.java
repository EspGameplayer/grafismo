package grafismo.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CompetitionMapperTest {

    private CompetitionMapper competitionMapper;

    @BeforeEach
    public void setUp() {
        competitionMapper = new CompetitionMapperImpl();
    }
}
