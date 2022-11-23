package grafismo.repository;

import grafismo.domain.Callup;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface CallupRepositoryWithBagRelationships {
    Optional<Callup> fetchBagRelationships(Optional<Callup> callup);

    List<Callup> fetchBagRelationships(List<Callup> callups);

    Page<Callup> fetchBagRelationships(Page<Callup> callups);
}
