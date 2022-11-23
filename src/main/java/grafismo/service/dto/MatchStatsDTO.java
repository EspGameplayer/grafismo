package grafismo.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link grafismo.domain.MatchStats} entity.
 */
public class MatchStatsDTO implements Serializable {

    private Long id;

    @Min(value = 0)
    private Integer homePossessionTime;

    @Min(value = 0)
    private Integer awayPossessionTime;

    @Min(value = 0)
    private Integer inContestPossessionTime;

    private MatchDTO match;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getHomePossessionTime() {
        return homePossessionTime;
    }

    public void setHomePossessionTime(Integer homePossessionTime) {
        this.homePossessionTime = homePossessionTime;
    }

    public Integer getAwayPossessionTime() {
        return awayPossessionTime;
    }

    public void setAwayPossessionTime(Integer awayPossessionTime) {
        this.awayPossessionTime = awayPossessionTime;
    }

    public Integer getInContestPossessionTime() {
        return inContestPossessionTime;
    }

    public void setInContestPossessionTime(Integer inContestPossessionTime) {
        this.inContestPossessionTime = inContestPossessionTime;
    }

    public MatchDTO getMatch() {
        return match;
    }

    public void setMatch(MatchDTO match) {
        this.match = match;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MatchStatsDTO)) {
            return false;
        }

        MatchStatsDTO matchStatsDTO = (MatchStatsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, matchStatsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MatchStatsDTO{" +
            "id=" + getId() +
            ", homePossessionTime=" + getHomePossessionTime() +
            ", awayPossessionTime=" + getAwayPossessionTime() +
            ", inContestPossessionTime=" + getInContestPossessionTime() +
            ", match=" + getMatch() +
            "}";
    }
}
