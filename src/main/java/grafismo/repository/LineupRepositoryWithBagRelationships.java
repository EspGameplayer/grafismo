package grafismo.repository;

import grafismo.domain.Lineup;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface LineupRepositoryWithBagRelationships {
    Optional<Lineup> fetchBagRelationships(Optional<Lineup> lineup);

    List<Lineup> fetchBagRelationships(List<Lineup> lineups);

    Page<Lineup> fetchBagRelationships(Page<Lineup> lineups);
}
