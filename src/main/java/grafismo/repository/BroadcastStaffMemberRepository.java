package grafismo.repository;

import grafismo.domain.BroadcastStaffMember;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the BroadcastStaffMember entity.
 */
@Repository
public interface BroadcastStaffMemberRepository extends JpaRepository<BroadcastStaffMember, Long> {
    default Optional<BroadcastStaffMember> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<BroadcastStaffMember> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<BroadcastStaffMember> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct broadcastStaffMember from BroadcastStaffMember broadcastStaffMember left join fetch broadcastStaffMember.person",
        countQuery = "select count(distinct broadcastStaffMember) from BroadcastStaffMember broadcastStaffMember"
    )
    Page<BroadcastStaffMember> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct broadcastStaffMember from BroadcastStaffMember broadcastStaffMember left join fetch broadcastStaffMember.person"
    )
    List<BroadcastStaffMember> findAllWithToOneRelationships();

    @Query(
        "select broadcastStaffMember from BroadcastStaffMember broadcastStaffMember left join fetch broadcastStaffMember.person where broadcastStaffMember.id =:id"
    )
    Optional<BroadcastStaffMember> findOneWithToOneRelationships(@Param("id") Long id);
}
