package grafismo.repository;

import grafismo.domain.Injury;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Injury entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InjuryRepository extends JpaRepository<Injury, Long> {}
