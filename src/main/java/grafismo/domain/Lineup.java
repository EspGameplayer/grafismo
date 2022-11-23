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
 * A Lineup.
 */
@Entity
@Table(name = "lineup")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Lineup implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JsonIgnoreProperties(value = { "captain", "dt", "dt2", "teamDelegate", "players", "homeMatch", "awayMatch" }, allowSetters = true)
    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private Callup callup;

    @ManyToOne
    private Formation formation;

    @ManyToMany
    @JoinTable(
        name = "rel_lineup__player",
        joinColumns = @JoinColumn(name = "lineup_id"),
        inverseJoinColumns = @JoinColumn(name = "player_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "player", "position", "motmMatch", "captainCallup", "callups", "lineups" }, allowSetters = true)
    private Set<MatchPlayer> players = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Lineup id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Callup getCallup() {
        return this.callup;
    }

    public void setCallup(Callup callup) {
        this.callup = callup;
    }

    public Lineup callup(Callup callup) {
        this.setCallup(callup);
        return this;
    }

    public Formation getFormation() {
        return this.formation;
    }

    public void setFormation(Formation formation) {
        this.formation = formation;
    }

    public Lineup formation(Formation formation) {
        this.setFormation(formation);
        return this;
    }

    public Set<MatchPlayer> getPlayers() {
        return this.players;
    }

    public void setPlayers(Set<MatchPlayer> matchPlayers) {
        this.players = matchPlayers;
    }

    public Lineup players(Set<MatchPlayer> matchPlayers) {
        this.setPlayers(matchPlayers);
        return this;
    }

    public Lineup addPlayer(MatchPlayer matchPlayer) {
        this.players.add(matchPlayer);
        matchPlayer.getLineups().add(this);
        return this;
    }

    public Lineup removePlayer(MatchPlayer matchPlayer) {
        this.players.remove(matchPlayer);
        matchPlayer.getLineups().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Lineup)) {
            return false;
        }
        return id != null && id.equals(((Lineup) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Lineup{" +
            "id=" + getId() +
            "}";
    }
}
