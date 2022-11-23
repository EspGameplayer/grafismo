package grafismo.repository;

import grafismo.domain.CompetitionPlayer;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CompetitionPlayer entity.
 */
@Repository
public interface CompetitionPlayerRepository
    extends CompetitionPlayerRepositoryWithBagRelationships, JpaRepository<CompetitionPlayer, Long> {
    default Optional<CompetitionPlayer> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findOneWithToOneRelationships(id));
    }

    default List<CompetitionPlayer> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships());
    }

    default Page<CompetitionPlayer> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships(pageable));
    }

    @Query(
        value = "select distinct competitionPlayer from CompetitionPlayer competitionPlayer left join fetch competitionPlayer.competition",
        countQuery = "select count(distinct competitionPlayer) from CompetitionPlayer competitionPlayer"
    )
    Page<CompetitionPlayer> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct competitionPlayer from CompetitionPlayer competitionPlayer left join fetch competitionPlayer.competition")
    List<CompetitionPlayer> findAllWithToOneRelationships();

    @Query(
        "select competitionPlayer from CompetitionPlayer competitionPlayer left join fetch competitionPlayer.competition where competitionPlayer.id =:id"
    )
    Optional<CompetitionPlayer> findOneWithToOneRelationships(@Param("id") Long id);
}
