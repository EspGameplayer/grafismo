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
 * A Position.
 */
@Entity
@Table(name = "position")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Position implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "abb", nullable = false)
    private String abb;

    @ManyToMany
    @JoinTable(
        name = "rel_position__parent",
        joinColumns = @JoinColumn(name = "position_id"),
        inverseJoinColumns = @JoinColumn(name = "parent_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "parents", "children", "players", "competitionPlayers" }, allowSetters = true)
    private Set<Position> parents = new HashSet<>();

    @ManyToMany(mappedBy = "parents")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "parents", "children", "players", "competitionPlayers" }, allowSetters = true)
    private Set<Position> children = new HashSet<>();

    @ManyToMany(mappedBy = "positions")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "person", "team", "positions", "actions" }, allowSetters = true)
    private Set<Player> players = new HashSet<>();

    @ManyToMany(mappedBy = "preferredPositions")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "player", "competition", "preferredPositions" }, allowSetters = true)
    private Set<CompetitionPlayer> competitionPlayers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Position id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Position name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbb() {
        return this.abb;
    }

    public Position abb(String abb) {
        this.setAbb(abb);
        return this;
    }

    public void setAbb(String abb) {
        this.abb = abb;
    }

    public Set<Position> getParents() {
        return this.parents;
    }

    public void setParents(Set<Position> positions) {
        this.parents = positions;
    }

    public Position parents(Set<Position> positions) {
        this.setParents(positions);
        return this;
    }

    public Position addParent(Position position) {
        this.parents.add(position);
        position.getChildren().add(this);
        return this;
    }

    public Position removeParent(Position position) {
        this.parents.remove(position);
        position.getChildren().remove(this);
        return this;
    }

    public Set<Position> getChildren() {
        return this.children;
    }

    public void setChildren(Set<Position> positions) {
        if (this.children != null) {
            this.children.forEach(i -> i.removeParent(this));
        }
        if (positions != null) {
            positions.forEach(i -> i.addParent(this));
        }
        this.children = positions;
    }

    public Position children(Set<Position> positions) {
        this.setChildren(positions);
        return this;
    }

    public Position addChild(Position position) {
        this.children.add(position);
        position.getParents().add(this);
        return this;
    }

    public Position removeChild(Position position) {
        this.children.remove(position);
        position.getParents().remove(this);
        return this;
    }

    public Set<Player> getPlayers() {
        return this.players;
    }

    public void setPlayers(Set<Player> players) {
        if (this.players != null) {
            this.players.forEach(i -> i.removePosition(this));
        }
        if (players != null) {
            players.forEach(i -> i.addPosition(this));
        }
        this.players = players;
    }

    public Position players(Set<Player> players) {
        this.setPlayers(players);
        return this;
    }

    public Position addPlayer(Player player) {
        this.players.add(player);
        player.getPositions().add(this);
        return this;
    }

    public Position removePlayer(Player player) {
        this.players.remove(player);
        player.getPositions().remove(this);
        return this;
    }

    public Set<CompetitionPlayer> getCompetitionPlayers() {
        return this.competitionPlayers;
    }

    public void setCompetitionPlayers(Set<CompetitionPlayer> competitionPlayers) {
        if (this.competitionPlayers != null) {
            this.competitionPlayers.forEach(i -> i.removePreferredPosition(this));
        }
        if (competitionPlayers != null) {
            competitionPlayers.forEach(i -> i.addPreferredPosition(this));
        }
        this.competitionPlayers = competitionPlayers;
    }

    public Position competitionPlayers(Set<CompetitionPlayer> competitionPlayers) {
        this.setCompetitionPlayers(competitionPlayers);
        return this;
    }

    public Position addCompetitionPlayer(CompetitionPlayer competitionPlayer) {
        this.competitionPlayers.add(competitionPlayer);
        competitionPlayer.getPreferredPositions().add(this);
        return this;
    }

    public Position removeCompetitionPlayer(CompetitionPlayer competitionPlayer) {
        this.competitionPlayers.remove(competitionPlayer);
        competitionPlayer.getPreferredPositions().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Position)) {
            return false;
        }
        return id != null && id.equals(((Position) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Position{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", abb='" + getAbb() + "'" +
            "}";
    }
}
