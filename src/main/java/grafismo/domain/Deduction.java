package grafismo.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Deduction.
 */
@Entity
@Table(name = "deduction")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Deduction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Min(value = 1)
    @Column(name = "points")
    private Integer points;

    @Column(name = "moment")
    private Instant moment;

    @Column(name = "reason")
    private String reason;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "preferredFormation", "stadiums", "competitions" }, allowSetters = true)
    private Team team;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "sponsor", "motmSponsor", "parent", "teams", "children" }, allowSetters = true)
    private Competition competition;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Deduction id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPoints() {
        return this.points;
    }

    public Deduction points(Integer points) {
        this.setPoints(points);
        return this;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Instant getMoment() {
        return this.moment;
    }

    public Deduction moment(Instant moment) {
        this.setMoment(moment);
        return this;
    }

    public void setMoment(Instant moment) {
        this.moment = moment;
    }

    public String getReason() {
        return this.reason;
    }

    public Deduction reason(String reason) {
        this.setReason(reason);
        return this;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Team getTeam() {
        return this.team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Deduction team(Team team) {
        this.setTeam(team);
        return this;
    }

    public Competition getCompetition() {
        return this.competition;
    }

    public void setCompetition(Competition competition) {
        this.competition = competition;
    }

    public Deduction competition(Competition competition) {
        this.setCompetition(competition);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Deduction)) {
            return false;
        }
        return id != null && id.equals(((Deduction) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Deduction{" +
            "id=" + getId() +
            ", points=" + getPoints() +
            ", moment='" + getMoment() + "'" +
            ", reason='" + getReason() + "'" +
            "}";
    }
}
