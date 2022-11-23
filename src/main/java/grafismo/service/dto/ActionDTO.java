package grafismo.service.dto;

import grafismo.domain.enumeration.ActionType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link grafismo.domain.Action} entity.
 */
public class ActionDTO implements Serializable {

    private Long id;

    @Min(value = 0)
    private Integer minute;

    @Min(value = 0)
    @Max(value = 59)
    private Integer second;

    @Min(value = 1)
    @Max(value = 5)
    private Integer period;

    @NotNull
    private ActionType type;

    @Min(value = 0)
    private Integer status;

    private MatchStatsDTO matchStats;

    private Set<PlayerDTO> players = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getMinute() {
        return minute;
    }

    public void setMinute(Integer minute) {
        this.minute = minute;
    }

    public Integer getSecond() {
        return second;
    }

    public void setSecond(Integer second) {
        this.second = second;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public ActionType getType() {
        return type;
    }

    public void setType(ActionType type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public MatchStatsDTO getMatchStats() {
        return matchStats;
    }

    public void setMatchStats(MatchStatsDTO matchStats) {
        this.matchStats = matchStats;
    }

    public Set<PlayerDTO> getPlayers() {
        return players;
    }

    public void setPlayers(Set<PlayerDTO> players) {
        this.players = players;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ActionDTO)) {
            return false;
        }

        ActionDTO actionDTO = (ActionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, actionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ActionDTO{" +
            "id=" + getId() +
            ", minute=" + getMinute() +
            ", second=" + getSecond() +
            ", period=" + getPeriod() +
            ", type='" + getType() + "'" +
            ", status=" + getStatus() +
            ", matchStats=" + getMatchStats() +
            ", players=" + getPlayers() +
            "}";
    }
}
