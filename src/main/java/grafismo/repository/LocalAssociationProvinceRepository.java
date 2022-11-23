package grafismo.repository;

import grafismo.domain.LocalAssociationProvince;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the LocalAssociationProvince entity.
 */
@Repository
public interface LocalAssociationProvinceRepository extends JpaRepository<LocalAssociationProvince, Long> {
    default Optional<LocalAssociationProvince> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<LocalAssociationProvince> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<LocalAssociationProvince> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct localAssociationProvince from LocalAssociationProvince localAssociationProvince left join fetch localAssociationProvince.association",
        countQuery = "select count(distinct localAssociationProvince) from LocalAssociationProvince localAssociationProvince"
    )
    Page<LocalAssociationProvince> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct localAssociationProvince from LocalAssociationProvince localAssociationProvince left join fetch localAssociationProvince.association"
    )
    List<LocalAssociationProvince> findAllWithToOneRelationships();

    @Query(
        "select localAssociationProvince from LocalAssociationProvince localAssociationProvince left join fetch localAssociationProvince.association where localAssociationProvince.id =:id"
    )
    Optional<LocalAssociationProvince> findOneWithToOneRelationships(@Param("id") Long id);
}
