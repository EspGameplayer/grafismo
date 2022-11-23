package grafismo.repository;

import grafismo.domain.Stadium;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Stadium entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StadiumRepository extends JpaRepository<Stadium, Long> {}
