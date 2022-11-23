package grafismo.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Injury.
 */
@Entity
@Table(name = "injury")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Injury implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "moment")
    private Instant moment;

    @Column(name = "est_return_date")
    private LocalDate estReturnDate;

    @Column(name = "reason")
    private String reason;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "person", "team", "positions", "actions" }, allowSetters = true)
    private Player player;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Injury id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getMoment() {
        return this.moment;
    }

    public Injury moment(Instant moment) {
        this.setMoment(moment);
        return this;
    }

    public void setMoment(Instant moment) {
        this.moment = moment;
    }

    public LocalDate getEstReturnDate() {
        return this.estReturnDate;
    }

    public Injury estReturnDate(LocalDate estReturnDate) {
        this.setEstReturnDate(estReturnDate);
        return this;
    }

    public void setEstReturnDate(LocalDate estReturnDate) {
        this.estReturnDate = estReturnDate;
    }

    public String getReason() {
        return this.reason;
    }

    public Injury reason(String reason) {
        this.setReason(reason);
        return this;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Player getPlayer() {
        return this.player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Injury player(Player player) {
        this.setPlayer(player);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Injury)) {
            return false;
        }
        return id != null && id.equals(((Injury) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Injury{" +
            "id=" + getId() +
            ", moment='" + getMoment() + "'" +
            ", estReturnDate='" + getEstReturnDate() + "'" +
            ", reason='" + getReason() + "'" +
            "}";
    }
}
