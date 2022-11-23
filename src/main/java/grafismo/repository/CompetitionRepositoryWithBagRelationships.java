package grafismo.repository;

import grafismo.domain.Competition;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface CompetitionRepositoryWithBagRelationships {
    Optional<Competition> fetchBagRelationships(Optional<Competition> competition);

    List<Competition> fetchBagRelationships(List<Competition> competitions);

    Page<Competition> fetchBagRelationships(Page<Competition> competitions);
}
