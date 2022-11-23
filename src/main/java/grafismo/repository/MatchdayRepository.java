package grafismo.repository;

import grafismo.domain.Matchday;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Matchday entity.
 */
@Repository
public interface MatchdayRepository extends JpaRepository<Matchday, Long> {
    default Optional<Matchday> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Matchday> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Matchday> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct matchday from Matchday matchday left join fetch matchday.competition",
        countQuery = "select count(distinct matchday) from Matchday matchday"
    )
    Page<Matchday> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct matchday from Matchday matchday left join fetch matchday.competition")
    List<Matchday> findAllWithToOneRelationships();

    @Query("select matchday from Matchday matchday left join fetch matchday.competition where matchday.id =:id")
    Optional<Matchday> findOneWithToOneRelationships(@Param("id") Long id);
}
