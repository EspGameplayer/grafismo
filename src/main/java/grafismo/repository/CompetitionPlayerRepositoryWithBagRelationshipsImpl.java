package grafismo.repository;

import grafismo.domain.CompetitionPlayer;
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
public class CompetitionPlayerRepositoryWithBagRelationshipsImpl implements CompetitionPlayerRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<CompetitionPlayer> fetchBagRelationships(Optional<CompetitionPlayer> competitionPlayer) {
        return competitionPlayer.map(this::fetchPreferredPositions);
    }

    @Override
    public Page<CompetitionPlayer> fetchBagRelationships(Page<CompetitionPlayer> competitionPlayers) {
        return new PageImpl<>(
            fetchBagRelationships(competitionPlayers.getContent()),
            competitionPlayers.getPageable(),
            competitionPlayers.getTotalElements()
        );
    }

    @Override
    public List<CompetitionPlayer> fetchBagRelationships(List<CompetitionPlayer> competitionPlayers) {
        return Optional.of(competitionPlayers).map(this::fetchPreferredPositions).orElse(Collections.emptyList());
    }

    CompetitionPlayer fetchPreferredPositions(CompetitionPlayer result) {
        return entityManager
            .createQuery(
                "select competitionPlayer from CompetitionPlayer competitionPlayer left join fetch competitionPlayer.preferredPositions where competitionPlayer is :competitionPlayer",
                CompetitionPlayer.class
            )
            .setParameter("competitionPlayer", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<CompetitionPlayer> fetchPreferredPositions(List<CompetitionPlayer> competitionPlayers) {
        return entityManager
            .createQuery(
                "select distinct competitionPlayer from CompetitionPlayer competitionPlayer left join fetch competitionPlayer.preferredPositions where competitionPlayer in :competitionPlayers",
                CompetitionPlayer.class
            )
            .setParameter("competitionPlayers", competitionPlayers)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
