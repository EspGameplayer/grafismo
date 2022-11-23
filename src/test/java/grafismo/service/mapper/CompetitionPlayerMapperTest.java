package grafismo.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CompetitionPlayerMapperTest {

    private CompetitionPlayerMapper competitionPlayerMapper;

    @BeforeEach
    public void setUp() {
        competitionPlayerMapper = new CompetitionPlayerMapperImpl();
    }
}
