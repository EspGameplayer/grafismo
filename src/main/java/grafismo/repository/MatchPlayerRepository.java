package grafismo.repository;

import grafismo.domain.MatchPlayer;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the MatchPlayer entity.
 */
@Repository
public interface MatchPlayerRepository extends JpaRepository<MatchPlayer, Long> {
    default Optional<MatchPlayer> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<MatchPlayer> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<MatchPlayer> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct matchPlayer from MatchPlayer matchPlayer left join fetch matchPlayer.position",
        countQuery = "select count(distinct matchPlayer) from MatchPlayer matchPlayer"
    )
    Page<MatchPlayer> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct matchPlayer from MatchPlayer matchPlayer left join fetch matchPlayer.position")
    List<MatchPlayer> findAllWithToOneRelationships();

    @Query("select matchPlayer from MatchPlayer matchPlayer left join fetch matchPlayer.position where matchPlayer.id =:id")
    Optional<MatchPlayer> findOneWithToOneRelationships(@Param("id") Long id);
}
