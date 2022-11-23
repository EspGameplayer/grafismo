package grafismo.repository;

import grafismo.domain.Action;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface ActionRepositoryWithBagRelationships {
    Optional<Action> fetchBagRelationships(Optional<Action> action);

    List<Action> fetchBagRelationships(List<Action> actions);

    Page<Action> fetchBagRelationships(Page<Action> actions);
}
