package grafismo.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A MatchStats.
 */
@Entity
@Table(name = "match_stats")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MatchStats implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Min(value = 0)
    @Column(name = "home_possession_time")
    private Integer homePossessionTime;

    @Min(value = 0)
    @Column(name = "away_possession_time")
    private Integer awayPossessionTime;

    @Min(value = 0)
    @Column(name = "in_contest_possession_time")
    private Integer inContestPossessionTime;

    @JsonIgnoreProperties(
        value = {
            "motm",
            "homeCallup",
            "awayCallup",
            "homeTeam",
            "awayTeam",
            "stadium",
            "matchDelegate",
            "homeShirt",
            "awayShirt",
            "matchday",
            "referees",
        },
        allowSetters = true
    )
    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private Match match;

    @OneToMany(mappedBy = "matchStats")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "matchStats", "players" }, allowSetters = true)
    private Set<Action> actions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MatchStats id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getHomePossessionTime() {
        return this.homePossessionTime;
    }

    public MatchStats homePossessionTime(Integer homePossessionTime) {
        this.setHomePossessionTime(homePossessionTime);
        return this;
    }

    public void setHomePossessionTime(Integer homePossessionTime) {
        this.homePossessionTime = homePossessionTime;
    }

    public Integer getAwayPossessionTime() {
        return this.awayPossessionTime;
    }

    public MatchStats awayPossessionTime(Integer awayPossessionTime) {
        this.setAwayPossessionTime(awayPossessionTime);
        return this;
    }

    public void setAwayPossessionTime(Integer awayPossessionTime) {
        this.awayPossessionTime = awayPossessionTime;
    }

    public Integer getInContestPossessionTime() {
        return this.inContestPossessionTime;
    }

    public MatchStats inContestPossessionTime(Integer inContestPossessionTime) {
        this.setInContestPossessionTime(inContestPossessionTime);
        return this;
    }

    public void setInContestPossessionTime(Integer inContestPossessionTime) {
        this.inContestPossessionTime = inContestPossessionTime;
    }

    public Match getMatch() {
        return this.match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public MatchStats match(Match match) {
        this.setMatch(match);
        return this;
    }

    public Set<Action> getActions() {
        return this.actions;
    }

    public void setActions(Set<Action> actions) {
        if (this.actions != null) {
            this.actions.forEach(i -> i.setMatchStats(null));
        }
        if (actions != null) {
            actions.forEach(i -> i.setMatchStats(this));
        }
        this.actions = actions;
    }

    public MatchStats actions(Set<Action> actions) {
        this.setActions(actions);
        return this;
    }

    public MatchStats addAction(Action action) {
        this.actions.add(action);
        action.setMatchStats(this);
        return this;
    }

    public MatchStats removeAction(Action action) {
        this.actions.remove(action);
        action.setMatchStats(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MatchStats)) {
            return false;
        }
        return id != null && id.equals(((MatchStats) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MatchStats{" +
            "id=" + getId() +
            ", homePossessionTime=" + getHomePossessionTime() +
            ", awayPossessionTime=" + getAwayPossessionTime() +
            ", inContestPossessionTime=" + getInContestPossessionTime() +
            "}";
    }
}
