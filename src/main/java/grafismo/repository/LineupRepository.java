package grafismo.repository;

import grafismo.domain.Lineup;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Lineup entity.
 */
@Repository
public interface LineupRepository extends LineupRepositoryWithBagRelationships, JpaRepository<Lineup, Long> {
    default Optional<Lineup> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findOneWithToOneRelationships(id));
    }

    default List<Lineup> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships());
    }

    default Page<Lineup> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships(pageable));
    }

    @Query(
        value = "select distinct lineup from Lineup lineup left join fetch lineup.formation",
        countQuery = "select count(distinct lineup) from Lineup lineup"
    )
    Page<Lineup> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct lineup from Lineup lineup left join fetch lineup.formation")
    List<Lineup> findAllWithToOneRelationships();

    @Query("select lineup from Lineup lineup left join fetch lineup.formation where lineup.id =:id")
    Optional<Lineup> findOneWithToOneRelationships(@Param("id") Long id);
}
