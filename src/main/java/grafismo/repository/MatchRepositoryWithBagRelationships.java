package grafismo.repository;

import grafismo.domain.Match;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface MatchRepositoryWithBagRelationships {
    Optional<Match> fetchBagRelationships(Optional<Match> match);

    List<Match> fetchBagRelationships(List<Match> matches);

    Page<Match> fetchBagRelationships(Page<Match> matches);
}
