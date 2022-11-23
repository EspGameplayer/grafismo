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
 * A CompetitionPlayer.
 */
@Entity
@Table(name = "competition_player")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CompetitionPlayer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Min(value = 0)
    @Column(name = "preferred_shirt_number")
    private Integer preferredShirtNumber;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "person", "team", "positions", "actions" }, allowSetters = true)
    private Player player;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "sponsor", "motmSponsor", "parent", "teams", "children" }, allowSetters = true)
    private Competition competition;

    @ManyToMany
    @JoinTable(
        name = "rel_competition_player__preferred_position",
        joinColumns = @JoinColumn(name = "competition_player_id"),
        inverseJoinColumns = @JoinColumn(name = "preferred_position_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "parents", "children", "players", "competitionPlayers" }, allowSetters = true)
    private Set<Position> preferredPositions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CompetitionPlayer id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPreferredShirtNumber() {
        return this.preferredShirtNumber;
    }

    public CompetitionPlayer preferredShirtNumber(Integer preferredShirtNumber) {
        this.setPreferredShirtNumber(preferredShirtNumber);
        return this;
    }

    public void setPreferredShirtNumber(Integer preferredShirtNumber) {
        this.preferredShirtNumber = preferredShirtNumber;
    }

    public Player getPlayer() {
        return this.player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public CompetitionPlayer player(Player player) {
        this.setPlayer(player);
        return this;
    }

    public Competition getCompetition() {
        return this.competition;
    }

    public void setCompetition(Competition competition) {
        this.competition = competition;
    }

    public CompetitionPlayer competition(Competition competition) {
        this.setCompetition(competition);
        return this;
    }

    public Set<Position> getPreferredPositions() {
        return this.preferredPositions;
    }

    public void setPreferredPositions(Set<Position> positions) {
        this.preferredPositions = positions;
    }

    public CompetitionPlayer preferredPositions(Set<Position> positions) {
        this.setPreferredPositions(positions);
        return this;
    }

    public CompetitionPlayer addPreferredPosition(Position position) {
        this.preferredPositions.add(position);
        position.getCompetitionPlayers().add(this);
        return this;
    }

    public CompetitionPlayer removePreferredPosition(Position position) {
        this.preferredPositions.remove(position);
        position.getCompetitionPlayers().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CompetitionPlayer)) {
            return false;
        }
        return id != null && id.equals(((CompetitionPlayer) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CompetitionPlayer{" +
            "id=" + getId() +
            ", preferredShirtNumber=" + getPreferredShirtNumber() +
            "}";
    }
}
