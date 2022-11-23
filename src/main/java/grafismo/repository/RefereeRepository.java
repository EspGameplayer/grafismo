package grafismo.repository;

import grafismo.domain.Referee;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Referee entity.
 */
@Repository
public interface RefereeRepository extends JpaRepository<Referee, Long> {
    default Optional<Referee> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Referee> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Referee> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct referee from Referee referee left join fetch referee.person left join fetch referee.association",
        countQuery = "select count(distinct referee) from Referee referee"
    )
    Page<Referee> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct referee from Referee referee left join fetch referee.person left join fetch referee.association")
    List<Referee> findAllWithToOneRelationships();

    @Query("select referee from Referee referee left join fetch referee.person left join fetch referee.association where referee.id =:id")
    Optional<Referee> findOneWithToOneRelationships(@Param("id") Long id);
}
