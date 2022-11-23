package grafismo.repository;

import grafismo.domain.Deduction;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Deduction entity.
 */
@Repository
public interface DeductionRepository extends JpaRepository<Deduction, Long> {
    default Optional<Deduction> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Deduction> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Deduction> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct deduction from Deduction deduction left join fetch deduction.team left join fetch deduction.competition",
        countQuery = "select count(distinct deduction) from Deduction deduction"
    )
    Page<Deduction> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct deduction from Deduction deduction left join fetch deduction.team left join fetch deduction.competition")
    List<Deduction> findAllWithToOneRelationships();

    @Query(
        "select deduction from Deduction deduction left join fetch deduction.team left join fetch deduction.competition where deduction.id =:id"
    )
    Optional<Deduction> findOneWithToOneRelationships(@Param("id") Long id);
}
