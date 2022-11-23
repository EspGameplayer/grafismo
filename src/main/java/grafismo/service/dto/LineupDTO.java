package grafismo.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link grafismo.domain.Lineup} entity.
 */
public class LineupDTO implements Serializable {

    private Long id;

    private CallupDTO callup;

    private FormationDTO formation;

    private Set<MatchPlayerDTO> players = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CallupDTO getCallup() {
        return callup;
    }

    public void setCallup(CallupDTO callup) {
        this.callup = callup;
    }

    public FormationDTO getFormation() {
        return formation;
    }

    public void setFormation(FormationDTO formation) {
        this.formation = formation;
    }

    public Set<MatchPlayerDTO> getPlayers() {
        return players;
    }

    public void setPlayers(Set<MatchPlayerDTO> players) {
        this.players = players;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LineupDTO)) {
            return false;
        }

        LineupDTO lineupDTO = (LineupDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, lineupDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LineupDTO{" +
            "id=" + getId() +
            ", callup=" + getCallup() +
            ", formation=" + getFormation() +
            ", players=" + getPlayers() +
            "}";
    }
}
