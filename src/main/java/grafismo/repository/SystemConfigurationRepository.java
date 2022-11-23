package grafismo.repository;

import grafismo.domain.SystemConfiguration;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SystemConfiguration entity.
 */
@Repository
public interface SystemConfigurationRepository extends JpaRepository<SystemConfiguration, Long> {
    default Optional<SystemConfiguration> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<SystemConfiguration> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<SystemConfiguration> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct systemConfiguration from SystemConfiguration systemConfiguration left join fetch systemConfiguration.currentSeason left join fetch systemConfiguration.defaultSponsorLogo left join fetch systemConfiguration.defaultMotmSponsorLogo",
        countQuery = "select count(distinct systemConfiguration) from SystemConfiguration systemConfiguration"
    )
    Page<SystemConfiguration> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct systemConfiguration from SystemConfiguration systemConfiguration left join fetch systemConfiguration.currentSeason left join fetch systemConfiguration.defaultSponsorLogo left join fetch systemConfiguration.defaultMotmSponsorLogo"
    )
    List<SystemConfiguration> findAllWithToOneRelationships();

    @Query(
        "select systemConfiguration from SystemConfiguration systemConfiguration left join fetch systemConfiguration.currentSeason left join fetch systemConfiguration.defaultSponsorLogo left join fetch systemConfiguration.defaultMotmSponsorLogo where systemConfiguration.id =:id"
    )
    Optional<SystemConfiguration> findOneWithToOneRelationships(@Param("id") Long id);
}
