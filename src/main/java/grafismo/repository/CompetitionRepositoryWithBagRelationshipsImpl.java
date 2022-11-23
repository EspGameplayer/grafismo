package grafismo.repository;

import grafismo.domain.Competition;
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
public class CompetitionRepositoryWithBagRelationshipsImpl implements CompetitionRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Competition> fetchBagRelationships(Optional<Competition> competition) {
        return competition.map(this::fetchTeams);
    }

    @Override
    public Page<Competition> fetchBagRelationships(Page<Competition> competitions) {
        return new PageImpl<>(
            fetchBagRelationships(competitions.getContent()),
            competitions.getPageable(),
            competitions.getTotalElements()
        );
    }

    @Override
    public List<Competition> fetchBagRelationships(List<Competition> competitions) {
        return Optional.of(competitions).map(this::fetchTeams).orElse(Collections.emptyList());
    }

    Competition fetchTeams(Competition result) {
        return entityManager
            .createQuery(
                "select competition from Competition competition left join fetch competition.teams where competition is :competition",
                Competition.class
            )
            .setParameter("competition", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Competition> fetchTeams(List<Competition> competitions) {
        return entityManager
            .createQuery(
                "select distinct competition from Competition competition left join fetch competition.teams where competition in :competitions",
                Competition.class
            )
            .setParameter("competitions", competitions)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
