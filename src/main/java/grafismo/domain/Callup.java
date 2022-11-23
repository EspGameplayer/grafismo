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
 * A Callup.
 */
@Entity
@Table(name = "callup")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Callup implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JsonIgnoreProperties(value = { "player", "position", "motmMatch", "captainCallup", "callups", "lineups" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private MatchPlayer captain;

    @ManyToOne
    @JsonIgnoreProperties(value = { "person", "team" }, allowSetters = true)
    private TeamStaffMember dt;

    @ManyToOne
    @JsonIgnoreProperties(value = { "person", "team" }, allowSetters = true)
    private TeamStaffMember dt2;

    @ManyToOne
    @JsonIgnoreProperties(value = { "person", "team" }, allowSetters = true)
    private TeamStaffMember teamDelegate;

    @ManyToMany
    @JoinTable(
        name = "rel_callup__player",
        joinColumns = @JoinColumn(name = "callup_id"),
        inverseJoinColumns = @JoinColumn(name = "player_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "player", "position", "motmMatch", "captainCallup", "callups", "lineups" }, allowSetters = true)
    private Set<MatchPlayer> players = new HashSet<>();

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
    @OneToOne(mappedBy = "homeCallup")
    private Match homeMatch;

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
    @OneToOne(mappedBy = "awayCallup")
    private Match awayMatch;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Callup id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MatchPlayer getCaptain() {
        return this.captain;
    }

    public void setCaptain(MatchPlayer matchPlayer) {
        this.captain = matchPlayer;
    }

    public Callup captain(MatchPlayer matchPlayer) {
        this.setCaptain(matchPlayer);
        return this;
    }

    public TeamStaffMember getDt() {
        return this.dt;
    }

    public void setDt(TeamStaffMember teamStaffMember) {
        this.dt = teamStaffMember;
    }

    public Callup dt(TeamStaffMember teamStaffMember) {
        this.setDt(teamStaffMember);
        return this;
    }

    public TeamStaffMember getDt2() {
        return this.dt2;
    }

    public void setDt2(TeamStaffMember teamStaffMember) {
        this.dt2 = teamStaffMember;
    }

    public Callup dt2(TeamStaffMember teamStaffMember) {
        this.setDt2(teamStaffMember);
        return this;
    }

    public TeamStaffMember getTeamDelegate() {
        return this.teamDelegate;
    }

    public void setTeamDelegate(TeamStaffMember teamStaffMember) {
        this.teamDelegate = teamStaffMember;
    }

    public Callup teamDelegate(TeamStaffMember teamStaffMember) {
        this.setTeamDelegate(teamStaffMember);
        return this;
    }

    public Set<MatchPlayer> getPlayers() {
        return this.players;
    }

    public void setPlayers(Set<MatchPlayer> matchPlayers) {
        this.players = matchPlayers;
    }

    public Callup players(Set<MatchPlayer> matchPlayers) {
        this.setPlayers(matchPlayers);
        return this;
    }

    public Callup addPlayer(MatchPlayer matchPlayer) {
        this.players.add(matchPlayer);
        matchPlayer.getCallups().add(this);
        return this;
    }

    public Callup removePlayer(MatchPlayer matchPlayer) {
        this.players.remove(matchPlayer);
        matchPlayer.getCallups().remove(this);
        return this;
    }

    public Match getHomeMatch() {
        return this.homeMatch;
    }

    public void setHomeMatch(Match match) {
        if (this.homeMatch != null) {
            this.homeMatch.setHomeCallup(null);
        }
        if (match != null) {
            match.setHomeCallup(this);
        }
        this.homeMatch = match;
    }

    public Callup homeMatch(Match match) {
        this.setHomeMatch(match);
        return this;
    }

    public Match getAwayMatch() {
        return this.awayMatch;
    }

    public void setAwayMatch(Match match) {
        if (this.awayMatch != null) {
            this.awayMatch.setAwayCallup(null);
        }
        if (match != null) {
            match.setAwayCallup(this);
        }
        this.awayMatch = match;
    }

    public Callup awayMatch(Match match) {
        this.setAwayMatch(match);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Callup)) {
            return false;
        }
        return id != null && id.equals(((Callup) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Callup{" +
            "id=" + getId() +
            "}";
    }
}
