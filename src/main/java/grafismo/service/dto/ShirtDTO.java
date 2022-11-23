package grafismo.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link grafismo.domain.Shirt} entity.
 */
public class ShirtDTO implements Serializable {

    private Long id;

    @Pattern(regexp = "^[0-9a-f]{6}$|^$")
    private String colour1;

    @Pattern(regexp = "^[0-9a-f]{6}$|^$")
    private String colour2;

    @Min(value = 1)
    @Max(value = 3)
    private Integer type;

    private TeamDTO team;

    private SeasonDTO season;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getColour1() {
        return colour1;
    }

    public void setColour1(String colour1) {
        this.colour1 = colour1;
    }

    public String getColour2() {
        return colour2;
    }

    public void setColour2(String colour2) {
        this.colour2 = colour2;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public TeamDTO getTeam() {
        return team;
    }

    public void setTeam(TeamDTO team) {
        this.team = team;
    }

    public SeasonDTO getSeason() {
        return season;
    }

    public void setSeason(SeasonDTO season) {
        this.season = season;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ShirtDTO)) {
            return false;
        }

        ShirtDTO shirtDTO = (ShirtDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, shirtDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ShirtDTO{" +
            "id=" + getId() +
            ", colour1='" + getColour1() + "'" +
            ", colour2='" + getColour2() + "'" +
            ", type=" + getType() +
            ", team=" + getTeam() +
            ", season=" + getSeason() +
            "}";
    }
}
