package grafismo.repository;

import grafismo.domain.ActionKey;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ActionKey entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ActionKeyRepository extends JpaRepository<ActionKey, Long> {}
