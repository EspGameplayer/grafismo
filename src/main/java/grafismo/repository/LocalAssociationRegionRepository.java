package grafismo.repository;

import grafismo.domain.LocalAssociationRegion;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the LocalAssociationRegion entity.
 */
@Repository
public interface LocalAssociationRegionRepository extends JpaRepository<LocalAssociationRegion, Long> {
    default Optional<LocalAssociationRegion> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<LocalAssociationRegion> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<LocalAssociationRegion> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct localAssociationRegion from LocalAssociationRegion localAssociationRegion left join fetch localAssociationRegion.association",
        countQuery = "select count(distinct localAssociationRegion) from LocalAssociationRegion localAssociationRegion"
    )
    Page<LocalAssociationRegion> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct localAssociationRegion from LocalAssociationRegion localAssociationRegion left join fetch localAssociationRegion.association"
    )
    List<LocalAssociationRegion> findAllWithToOneRelationships();

    @Query(
        "select localAssociationRegion from LocalAssociationRegion localAssociationRegion left join fetch localAssociationRegion.association where localAssociationRegion.id =:id"
    )
    Optional<LocalAssociationRegion> findOneWithToOneRelationships(@Param("id") Long id);
}
