package grafismo.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link grafismo.domain.Suspension} entity.
 */
public class SuspensionDTO implements Serializable {

    private Long id;

    @Min(value = 1)
    private Integer matches;

    private Instant moment;

    private String reason;

    private PersonDTO person;

    private CompetitionDTO competition;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getMatches() {
        return matches;
    }

    public void setMatches(Integer matches) {
        this.matches = matches;
    }

    public Instant getMoment() {
        return moment;
    }

    public void setMoment(Instant moment) {
        this.moment = moment;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public PersonDTO getPerson() {
        return person;
    }

    public void setPerson(PersonDTO person) {
        this.person = person;
    }

    public CompetitionDTO getCompetition() {
        return competition;
    }

    public void setCompetition(CompetitionDTO competition) {
        this.competition = competition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SuspensionDTO)) {
            return false;
        }

        SuspensionDTO suspensionDTO = (SuspensionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, suspensionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SuspensionDTO{" +
            "id=" + getId() +
            ", matches=" + getMatches() +
            ", moment='" + getMoment() + "'" +
            ", reason='" + getReason() + "'" +
            ", person=" + getPerson() +
            ", competition=" + getCompetition() +
            "}";
    }
}
