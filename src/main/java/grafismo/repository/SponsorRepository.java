package grafismo.repository;

import grafismo.domain.Sponsor;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Sponsor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SponsorRepository extends JpaRepository<Sponsor, Long> {}
