package grafismo.repository;

import grafismo.domain.GraphicElementPos;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the GraphicElementPos entity.
 */
@Repository
public interface GraphicElementPosRepository extends JpaRepository<GraphicElementPos, Long> {
    default Optional<GraphicElementPos> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<GraphicElementPos> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<GraphicElementPos> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct graphicElementPos from GraphicElementPos graphicElementPos left join fetch graphicElementPos.parent",
        countQuery = "select count(distinct graphicElementPos) from GraphicElementPos graphicElementPos"
    )
    Page<GraphicElementPos> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct graphicElementPos from GraphicElementPos graphicElementPos left join fetch graphicElementPos.parent")
    List<GraphicElementPos> findAllWithToOneRelationships();

    @Query(
        "select graphicElementPos from GraphicElementPos graphicElementPos left join fetch graphicElementPos.parent where graphicElementPos.id =:id"
    )
    Optional<GraphicElementPos> findOneWithToOneRelationships(@Param("id") Long id);
}
