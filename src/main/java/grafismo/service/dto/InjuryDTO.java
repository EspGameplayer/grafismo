package grafismo.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link grafismo.domain.Injury} entity.
 */
public class InjuryDTO implements Serializable {

    private Long id;

    private Instant moment;

    private LocalDate estReturnDate;

    private String reason;

    private PlayerDTO player;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getMoment() {
        return moment;
    }

    public void setMoment(Instant moment) {
        this.moment = moment;
    }

    public LocalDate getEstReturnDate() {
        return estReturnDate;
    }

    public void setEstReturnDate(LocalDate estReturnDate) {
        this.estReturnDate = estReturnDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public PlayerDTO getPlayer() {
        return player;
    }

    public void setPlayer(PlayerDTO player) {
        this.player = player;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InjuryDTO)) {
            return false;
        }

        InjuryDTO injuryDTO = (InjuryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, injuryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InjuryDTO{" +
            "id=" + getId() +
            ", moment='" + getMoment() + "'" +
            ", estReturnDate='" + getEstReturnDate() + "'" +
            ", reason='" + getReason() + "'" +
            ", player=" + getPlayer() +
            "}";
    }
}
