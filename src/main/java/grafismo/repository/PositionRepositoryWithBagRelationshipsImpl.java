package grafismo.repository;

import grafismo.domain.Position;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class PositionRepositoryWithBagRelationshipsImpl implements PositionRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Position> fetchBagRelationships(Optional<Position> position) {
        return position.map(this::fetchParents);
    }

    @Override
    public Page<Position> fetchBagRelationships(Page<Position> positions) {
        return new PageImpl<>(fetchBagRelationships(positions.getContent()), positions.getPageable(), positions.getTotalElements());
    }

    @Override
    public List<Position> fetchBagRelationships(List<Position> positions) {
        return Optional.of(positions).map(this::fetchParents).orElse(Collections.emptyList());
    }

    Position fetchParents(Position result) {
        return entityManager
            .createQuery(
                "select position from Position position left join fetch position.parents where position is :position",
                Position.class
            )
            .setParameter("position", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Position> fetchParents(List<Position> positions) {
        return entityManager
            .createQuery(
                "select distinct position from Position position left join fetch position.parents where position in :positions",
                Position.class
            )
            .setParameter("positions", positions)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
