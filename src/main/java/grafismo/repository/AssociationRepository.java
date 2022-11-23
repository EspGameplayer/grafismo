package grafismo.repository;

import grafismo.domain.Association;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Association entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AssociationRepository extends JpaRepository<Association, Long> {}
