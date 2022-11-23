package grafismo.service.dto;

import grafismo.domain.enumeration.StaffMemberRole;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link grafismo.domain.TeamStaffMember} entity.
 */
public class TeamStaffMemberDTO implements Serializable {

    private Long id;

    private StaffMemberRole role;

    private PersonDTO person;

    private TeamDTO team;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StaffMemberRole getRole() {
        return role;
    }

    public void setRole(StaffMemberRole role) {
        this.role = role;
    }

    public PersonDTO getPerson() {
        return person;
    }

    public void setPerson(PersonDTO person) {
        this.person = person;
    }

    public TeamDTO getTeam() {
        return team;
    }

    public void setTeam(TeamDTO team) {
        this.team = team;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TeamStaffMemberDTO)) {
            return false;
        }

        TeamStaffMemberDTO teamStaffMemberDTO = (TeamStaffMemberDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, teamStaffMemberDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TeamStaffMemberDTO{" +
            "id=" + getId() +
            ", role='" + getRole() + "'" +
            ", person=" + getPerson() +
            ", team=" + getTeam() +
            "}";
    }
}
