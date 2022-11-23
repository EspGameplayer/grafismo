package grafismo.repository;

import grafismo.domain.CompetitionPlayer;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface CompetitionPlayerRepositoryWithBagRelationships {
    Optional<CompetitionPlayer> fetchBagRelationships(Optional<CompetitionPlayer> competitionPlayer);

    List<CompetitionPlayer> fetchBagRelationships(List<CompetitionPlayer> competitionPlayers);

    Page<CompetitionPlayer> fetchBagRelationships(Page<CompetitionPlayer> competitionPlayers);
}
