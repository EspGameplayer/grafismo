package grafismo.repository;

import grafismo.domain.MatchStats;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the MatchStats entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MatchStatsRepository extends JpaRepository<MatchStats, Long> {}
