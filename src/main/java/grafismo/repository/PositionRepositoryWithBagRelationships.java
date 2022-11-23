package grafismo.repository;

import grafismo.domain.Position;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface PositionRepositoryWithBagRelationships {
    Optional<Position> fetchBagRelationships(Optional<Position> position);

    List<Position> fetchBagRelationships(List<Position> positions);

    Page<Position> fetchBagRelationships(Page<Position> positions);
}
