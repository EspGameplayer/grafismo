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
 * A MatchPlayer.
 */
@Entity
@Table(name = "match_player")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MatchPlayer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Min(value = 0)
    @Column(name = "shirt_number")
    private Integer shirtNumber;

    @Min(value = 0)
    @Max(value = 1)
    @Column(name = "is_warned")
    private Integer isWarned;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "person", "team", "positions", "actions" }, allowSetters = true)
    private Player player;

    @ManyToOne
    @JsonIgnoreProperties(value = { "parents", "children", "players", "competitionPlayers" }, allowSetters = true)
    private Position position;

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
    @OneToOne(mappedBy = "motm")
    private Match motmMatch;

    @JsonIgnoreProperties(value = { "captain", "dt", "dt2", "teamDelegate", "players", "homeMatch", "awayMatch" }, allowSetters = true)
    @OneToOne(mappedBy = "captain")
    private Callup captainCallup;

    @ManyToMany(mappedBy = "players")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "captain", "dt", "dt2", "teamDelegate", "players", "homeMatch", "awayMatch" }, allowSetters = true)
    private Set<Callup> callups = new HashSet<>();

    @ManyToMany(mappedBy = "players")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "callup", "formation", "players" }, allowSetters = true)
    private Set<Lineup> lineups = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MatchPlayer id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getShirtNumber() {
        return this.shirtNumber;
    }

    public MatchPlayer shirtNumber(Integer shirtNumber) {
        this.setShirtNumber(shirtNumber);
        return this;
    }

    public void setShirtNumber(Integer shirtNumber) {
        this.shirtNumber = shirtNumber;
    }

    public Integer getIsWarned() {
        return this.isWarned;
    }

    public MatchPlayer isWarned(Integer isWarned) {
        this.setIsWarned(isWarned);
        return this;
    }

    public void setIsWarned(Integer isWarned) {
        this.isWarned = isWarned;
    }

    public Player getPlayer() {
        return this.player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public MatchPlayer player(Player player) {
        this.setPlayer(player);
        return this;
    }

    public Position getPosition() {
        return this.position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public MatchPlayer position(Position position) {
        this.setPosition(position);
        return this;
    }

    public Match getMotmMatch() {
        return this.motmMatch;
    }

    public void setMotmMatch(Match match) {
        if (this.motmMatch != null) {
            this.motmMatch.setMotm(null);
        }
        if (match != null) {
            match.setMotm(this);
        }
        this.motmMatch = match;
    }

    public MatchPlayer motmMatch(Match match) {
        this.setMotmMatch(match);
        return this;
    }

    public Callup getCaptainCallup() {
        return this.captainCallup;
    }

    public void setCaptainCallup(Callup callup) {
        if (this.captainCallup != null) {
            this.captainCallup.setCaptain(null);
        }
        if (callup != null) {
            callup.setCaptain(this);
        }
        this.captainCallup = callup;
    }

    public MatchPlayer captainCallup(Callup callup) {
        this.setCaptainCallup(callup);
        return this;
    }

    public Set<Callup> getCallups() {
        return this.callups;
    }

    public void setCallups(Set<Callup> callups) {
        if (this.callups != null) {
            this.callups.forEach(i -> i.removePlayer(this));
        }
        if (callups != null) {
            callups.forEach(i -> i.addPlayer(this));
        }
        this.callups = callups;
    }

    public MatchPlayer callups(Set<Callup> callups) {
        this.setCallups(callups);
        return this;
    }

    public MatchPlayer addCallup(Callup callup) {
        this.callups.add(callup);
        callup.getPlayers().add(this);
        return this;
    }

    public MatchPlayer removeCallup(Callup callup) {
        this.callups.remove(callup);
        callup.getPlayers().remove(this);
        return this;
    }

    public Set<Lineup> getLineups() {
        return this.lineups;
    }

    public void setLineups(Set<Lineup> lineups) {
        if (this.lineups != null) {
            this.lineups.forEach(i -> i.removePlayer(this));
        }
        if (lineups != null) {
            lineups.forEach(i -> i.addPlayer(this));
        }
        this.lineups = lineups;
    }

    public MatchPlayer lineups(Set<Lineup> lineups) {
        this.setLineups(lineups);
        return this;
    }

    public MatchPlayer addLineup(Lineup lineup) {
        this.lineups.add(lineup);
        lineup.getPlayers().add(this);
        return this;
    }

    public MatchPlayer removeLineup(Lineup lineup) {
        this.lineups.remove(lineup);
        lineup.getPlayers().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MatchPlayer)) {
            return false;
        }
        return id != null && id.equals(((MatchPlayer) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MatchPlayer{" +
            "id=" + getId() +
            ", shirtNumber=" + getShirtNumber() +
            ", isWarned=" + getIsWarned() +
            "}";
    }
}
