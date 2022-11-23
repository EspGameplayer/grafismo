package grafismo.repository;

import grafismo.domain.Competition;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Competition entity.
 */
@Repository
public interface CompetitionRepository extends CompetitionRepositoryWithBagRelationships, JpaRepository<Competition, Long> {
    default Optional<Competition> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findOneWithToOneRelationships(id));
    }

    default List<Competition> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships());
    }

    default Page<Competition> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships(pageable));
    }

    @Query(
        value = "select distinct competition from Competition competition left join fetch competition.sponsor left join fetch competition.motmSponsor left join fetch competition.parent",
        countQuery = "select count(distinct competition) from Competition competition"
    )
    Page<Competition> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct competition from Competition competition left join fetch competition.sponsor left join fetch competition.motmSponsor left join fetch competition.parent"
    )
    List<Competition> findAllWithToOneRelationships();

    @Query(
        "select competition from Competition competition left join fetch competition.sponsor left join fetch competition.motmSponsor left join fetch competition.parent where competition.id =:id"
    )
    Optional<Competition> findOneWithToOneRelationships(@Param("id") Long id);
}
