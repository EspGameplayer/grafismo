package grafismo.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link grafismo.domain.Callup} entity.
 */
public class CallupDTO implements Serializable {

    private Long id;

    private MatchPlayerDTO captain;

    private TeamStaffMemberDTO dt;

    private TeamStaffMemberDTO dt2;

    private TeamStaffMemberDTO teamDelegate;

    private Set<MatchPlayerDTO> players = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MatchPlayerDTO getCaptain() {
        return captain;
    }

    public void setCaptain(MatchPlayerDTO captain) {
        this.captain = captain;
    }

    public TeamStaffMemberDTO getDt() {
        return dt;
    }

    public void setDt(TeamStaffMemberDTO dt) {
        this.dt = dt;
    }

    public TeamStaffMemberDTO getDt2() {
        return dt2;
    }

    public void setDt2(TeamStaffMemberDTO dt2) {
        this.dt2 = dt2;
    }

    public TeamStaffMemberDTO getTeamDelegate() {
        return teamDelegate;
    }

    public void setTeamDelegate(TeamStaffMemberDTO teamDelegate) {
        this.teamDelegate = teamDelegate;
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
        if (!(o instanceof CallupDTO)) {
            return false;
        }

        CallupDTO callupDTO = (CallupDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, callupDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CallupDTO{" +
            "id=" + getId() +
            ", captain=" + getCaptain() +
            ", dt=" + getDt() +
            ", dt2=" + getDt2() +
            ", teamDelegate=" + getTeamDelegate() +
            ", players=" + getPlayers() +
            "}";
    }
}
