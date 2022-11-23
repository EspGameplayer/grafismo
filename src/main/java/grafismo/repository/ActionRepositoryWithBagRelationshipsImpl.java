package grafismo.repository;

import grafismo.domain.Action;
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
public class ActionRepositoryWithBagRelationshipsImpl implements ActionRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Action> fetchBagRelationships(Optional<Action> action) {
        return action.map(this::fetchPlayers);
    }

    @Override
    public Page<Action> fetchBagRelationships(Page<Action> actions) {
        return new PageImpl<>(fetchBagRelationships(actions.getContent()), actions.getPageable(), actions.getTotalElements());
    }

    @Override
    public List<Action> fetchBagRelationships(List<Action> actions) {
        return Optional.of(actions).map(this::fetchPlayers).orElse(Collections.emptyList());
    }

    Action fetchPlayers(Action result) {
        return entityManager
            .createQuery("select action from Action action left join fetch action.players where action is :action", Action.class)
            .setParameter("action", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Action> fetchPlayers(List<Action> actions) {
        return entityManager
            .createQuery("select distinct action from Action action left join fetch action.players where action in :actions", Action.class)
            .setParameter("actions", actions)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
