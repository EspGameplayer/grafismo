package grafismo.repository;

import grafismo.domain.Match;
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
public class MatchRepositoryWithBagRelationshipsImpl implements MatchRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Match> fetchBagRelationships(Optional<Match> match) {
        return match.map(this::fetchReferees);
    }

    @Override
    public Page<Match> fetchBagRelationships(Page<Match> matches) {
        return new PageImpl<>(fetchBagRelationships(matches.getContent()), matches.getPageable(), matches.getTotalElements());
    }

    @Override
    public List<Match> fetchBagRelationships(List<Match> matches) {
        return Optional.of(matches).map(this::fetchReferees).orElse(Collections.emptyList());
    }

    Match fetchReferees(Match result) {
        return entityManager
            .createQuery("select match from Match match left join fetch match.referees where match is :match", Match.class)
            .setParameter("match", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Match> fetchReferees(List<Match> matches) {
        return entityManager
            .createQuery("select distinct match from Match match left join fetch match.referees where match in :matches", Match.class)
            .setParameter("matches", matches)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
