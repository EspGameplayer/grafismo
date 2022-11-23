package grafismo.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link grafismo.domain.Deduction} entity.
 */
public class DeductionDTO implements Serializable {

    private Long id;

    @Min(value = 1)
    private Integer points;

    private Instant moment;

    private String reason;

    private TeamDTO team;

    private CompetitionDTO competition;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
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

    public TeamDTO getTeam() {
        return team;
    }

    public void setTeam(TeamDTO team) {
        this.team = team;
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
        if (!(o instanceof DeductionDTO)) {
            return false;
        }

        DeductionDTO deductionDTO = (DeductionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, deductionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DeductionDTO{" +
            "id=" + getId() +
            ", points=" + getPoints() +
            ", moment='" + getMoment() + "'" +
            ", reason='" + getReason() + "'" +
            ", team=" + getTeam() +
            ", competition=" + getCompetition() +
            "}";
    }
}
