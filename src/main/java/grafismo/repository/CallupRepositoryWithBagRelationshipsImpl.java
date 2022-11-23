package grafismo.repository;

import grafismo.domain.Callup;
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
public class CallupRepositoryWithBagRelationshipsImpl implements CallupRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Callup> fetchBagRelationships(Optional<Callup> callup) {
        return callup.map(this::fetchPlayers);
    }

    @Override
    public Page<Callup> fetchBagRelationships(Page<Callup> callups) {
        return new PageImpl<>(fetchBagRelationships(callups.getContent()), callups.getPageable(), callups.getTotalElements());
    }

    @Override
    public List<Callup> fetchBagRelationships(List<Callup> callups) {
        return Optional.of(callups).map(this::fetchPlayers).orElse(Collections.emptyList());
    }

    Callup fetchPlayers(Callup result) {
        return entityManager
            .createQuery("select callup from Callup callup left join fetch callup.players where callup is :callup", Callup.class)
            .setParameter("callup", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Callup> fetchPlayers(List<Callup> callups) {
        return entityManager
            .createQuery("select distinct callup from Callup callup left join fetch callup.players where callup in :callups", Callup.class)
            .setParameter("callups", callups)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
