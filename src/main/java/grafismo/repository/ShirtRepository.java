package grafismo.repository;

import grafismo.domain.Shirt;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Shirt entity.
 */
@Repository
public interface ShirtRepository extends JpaRepository<Shirt, Long> {
    default Optional<Shirt> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Shirt> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Shirt> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct shirt from Shirt shirt left join fetch shirt.team left join fetch shirt.season",
        countQuery = "select count(distinct shirt) from Shirt shirt"
    )
    Page<Shirt> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct shirt from Shirt shirt left join fetch shirt.team left join fetch shirt.season")
    List<Shirt> findAllWithToOneRelationships();

    @Query("select shirt from Shirt shirt left join fetch shirt.team left join fetch shirt.season where shirt.id =:id")
    Optional<Shirt> findOneWithToOneRelationships(@Param("id") Long id);
}
