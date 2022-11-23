package grafismo.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import grafismo.domain.enumeration.ActionType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Action.
 */
@Entity
@Table(name = "action")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Action implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Min(value = 0)
    @Column(name = "minute")
    private Integer minute;

    @Min(value = 0)
    @Max(value = 59)
    @Column(name = "second")
    private Integer second;

    @Min(value = 1)
    @Max(value = 5)
    @Column(name = "period")
    private Integer period;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private ActionType type;

    @Min(value = 0)
    @Column(name = "status")
    private Integer status;

    @ManyToOne
    @JsonIgnoreProperties(value = { "match", "actions" }, allowSetters = true)
    private MatchStats matchStats;

    @ManyToMany
    @JoinTable(
        name = "rel_action__player",
        joinColumns = @JoinColumn(name = "action_id"),
        inverseJoinColumns = @JoinColumn(name = "player_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "person", "team", "positions", "actions" }, allowSetters = true)
    private Set<Player> players = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Action id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getMinute() {
        return this.minute;
    }

    public Action minute(Integer minute) {
        this.setMinute(minute);
        return this;
    }

    public void setMinute(Integer minute) {
        this.minute = minute;
    }

    public Integer getSecond() {
        return this.second;
    }

    public Action second(Integer second) {
        this.setSecond(second);
        return this;
    }

    public void setSecond(Integer second) {
        this.second = second;
    }

    public Integer getPeriod() {
        return this.period;
    }

    public Action period(Integer period) {
        this.setPeriod(period);
        return this;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public ActionType getType() {
        return this.type;
    }

    public Action type(ActionType type) {
        this.setType(type);
        return this;
    }

    public void setType(ActionType type) {
        this.type = type;
    }

    public Integer getStatus() {
        return this.status;
    }

    public Action status(Integer status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public MatchStats getMatchStats() {
        return this.matchStats;
    }

    public void setMatchStats(MatchStats matchStats) {
        this.matchStats = matchStats;
    }

    public Action matchStats(MatchStats matchStats) {
        this.setMatchStats(matchStats);
        return this;
    }

    public Set<Player> getPlayers() {
        return this.players;
    }

    public void setPlayers(Set<Player> players) {
        this.players = players;
    }

    public Action players(Set<Player> players) {
        this.setPlayers(players);
        return this;
    }

    public Action addPlayer(Player player) {
        this.players.add(player);
        player.getActions().add(this);
        return this;
    }

    public Action removePlayer(Player player) {
        this.players.remove(player);
        player.getActions().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Action)) {
            return false;
        }
        return id != null && id.equals(((Action) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Action{" +
            "id=" + getId() +
            ", minute=" + getMinute() +
            ", second=" + getSecond() +
            ", period=" + getPeriod() +
            ", type='" + getType() + "'" +
            ", status=" + getStatus() +
            "}";
    }
}
