package grafismo.repository;

import grafismo.domain.Lineup;
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
public class LineupRepositoryWithBagRelationshipsImpl implements LineupRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Lineup> fetchBagRelationships(Optional<Lineup> lineup) {
        return lineup.map(this::fetchPlayers);
    }

    @Override
    public Page<Lineup> fetchBagRelationships(Page<Lineup> lineups) {
        return new PageImpl<>(fetchBagRelationships(lineups.getContent()), lineups.getPageable(), lineups.getTotalElements());
    }

    @Override
    public List<Lineup> fetchBagRelationships(List<Lineup> lineups) {
        return Optional.of(lineups).map(this::fetchPlayers).orElse(Collections.emptyList());
    }

    Lineup fetchPlayers(Lineup result) {
        return entityManager
            .createQuery("select lineup from Lineup lineup left join fetch lineup.players where lineup is :lineup", Lineup.class)
            .setParameter("lineup", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Lineup> fetchPlayers(List<Lineup> lineups) {
        return entityManager
            .createQuery("select distinct lineup from Lineup lineup left join fetch lineup.players where lineup in :lineups", Lineup.class)
            .setParameter("lineups", lineups)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
