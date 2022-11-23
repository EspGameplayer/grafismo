package grafismo.repository;

import grafismo.domain.TeamStaffMember;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TeamStaffMember entity.
 */
@Repository
public interface TeamStaffMemberRepository extends JpaRepository<TeamStaffMember, Long> {
    default Optional<TeamStaffMember> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<TeamStaffMember> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<TeamStaffMember> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct teamStaffMember from TeamStaffMember teamStaffMember left join fetch teamStaffMember.person left join fetch teamStaffMember.team",
        countQuery = "select count(distinct teamStaffMember) from TeamStaffMember teamStaffMember"
    )
    Page<TeamStaffMember> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct teamStaffMember from TeamStaffMember teamStaffMember left join fetch teamStaffMember.person left join fetch teamStaffMember.team"
    )
    List<TeamStaffMember> findAllWithToOneRelationships();

    @Query(
        "select teamStaffMember from TeamStaffMember teamStaffMember left join fetch teamStaffMember.person left join fetch teamStaffMember.team where teamStaffMember.id =:id"
    )
    Optional<TeamStaffMember> findOneWithToOneRelationships(@Param("id") Long id);
}
