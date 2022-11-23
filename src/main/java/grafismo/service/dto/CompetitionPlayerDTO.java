package grafismo.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link grafismo.domain.CompetitionPlayer} entity.
 */
public class CompetitionPlayerDTO implements Serializable {

    private Long id;

    @Min(value = 0)
    private Integer preferredShirtNumber;

    private PlayerDTO player;

    private CompetitionDTO competition;

    private Set<PositionDTO> preferredPositions = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPreferredShirtNumber() {
        return preferredShirtNumber;
    }

    public void setPreferredShirtNumber(Integer preferredShirtNumber) {
        this.preferredShirtNumber = preferredShirtNumber;
    }

    public PlayerDTO getPlayer() {
        return player;
    }

    public void setPlayer(PlayerDTO player) {
        this.player = player;
    }

    public CompetitionDTO getCompetition() {
        return competition;
    }

    public void setCompetition(CompetitionDTO competition) {
        this.competition = competition;
    }

    public Set<PositionDTO> getPreferredPositions() {
        return preferredPositions;
    }

    public void setPreferredPositions(Set<PositionDTO> preferredPositions) {
        this.preferredPositions = preferredPositions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CompetitionPlayerDTO)) {
            return false;
        }

        CompetitionPlayerDTO competitionPlayerDTO = (CompetitionPlayerDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, competitionPlayerDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CompetitionPlayerDTO{" +
            "id=" + getId() +
            ", preferredShirtNumber=" + getPreferredShirtNumber() +
            ", player=" + getPlayer() +
            ", competition=" + getCompetition() +
            ", preferredPositions=" + getPreferredPositions() +
            "}";
    }
}
