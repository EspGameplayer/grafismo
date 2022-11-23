package grafismo.repository;

import grafismo.domain.Suspension;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Suspension entity.
 */
@Repository
public interface SuspensionRepository extends JpaRepository<Suspension, Long> {
    default Optional<Suspension> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Suspension> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Suspension> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct suspension from Suspension suspension left join fetch suspension.person left join fetch suspension.competition",
        countQuery = "select count(distinct suspension) from Suspension suspension"
    )
    Page<Suspension> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct suspension from Suspension suspension left join fetch suspension.person left join fetch suspension.competition")
    List<Suspension> findAllWithToOneRelationships();

    @Query(
        "select suspension from Suspension suspension left join fetch suspension.person left join fetch suspension.competition where suspension.id =:id"
    )
    Optional<Suspension> findOneWithToOneRelationships(@Param("id") Long id);
}
