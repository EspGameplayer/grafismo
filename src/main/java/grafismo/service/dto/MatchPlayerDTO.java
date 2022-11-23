package grafismo.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link grafismo.domain.MatchPlayer} entity.
 */
public class MatchPlayerDTO implements Serializable {

    private Long id;

    @Min(value = 0)
    private Integer shirtNumber;

    @Min(value = 0)
    @Max(value = 1)
    private Integer isWarned;

    private PlayerDTO player;

    private PositionDTO position;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getShirtNumber() {
        return shirtNumber;
    }

    public void setShirtNumber(Integer shirtNumber) {
        this.shirtNumber = shirtNumber;
    }

    public Integer getIsWarned() {
        return isWarned;
    }

    public void setIsWarned(Integer isWarned) {
        this.isWarned = isWarned;
    }

    public PlayerDTO getPlayer() {
        return player;
    }

    public void setPlayer(PlayerDTO player) {
        this.player = player;
    }

    public PositionDTO getPosition() {
        return position;
    }

    public void setPosition(PositionDTO position) {
        this.position = position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MatchPlayerDTO)) {
            return false;
        }

        MatchPlayerDTO matchPlayerDTO = (MatchPlayerDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, matchPlayerDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MatchPlayerDTO{" +
            "id=" + getId() +
            ", shirtNumber=" + getShirtNumber() +
            ", isWarned=" + getIsWarned() +
            ", player=" + getPlayer() +
            ", position=" + getPosition() +
            "}";
    }
}
