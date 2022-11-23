package grafismo.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Suspension.
 */
@Entity
@Table(name = "suspension")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Suspension implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Min(value = 1)
    @Column(name = "matches")
    private Integer matches;

    @Column(name = "moment")
    private Instant moment;

    @Column(name = "reason")
    private String reason;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "nationality" }, allowSetters = true)
    private Person person;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "sponsor", "motmSponsor", "parent", "teams", "children" }, allowSetters = true)
    private Competition competition;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Suspension id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getMatches() {
        return this.matches;
    }

    public Suspension matches(Integer matches) {
        this.setMatches(matches);
        return this;
    }

    public void setMatches(Integer matches) {
        this.matches = matches;
    }

    public Instant getMoment() {
        return this.moment;
    }

    public Suspension moment(Instant moment) {
        this.setMoment(moment);
        return this;
    }

    public void setMoment(Instant moment) {
        this.moment = moment;
    }

    public String getReason() {
        return this.reason;
    }

    public Suspension reason(String reason) {
        this.setReason(reason);
        return this;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Person getPerson() {
        return this.person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Suspension person(Person person) {
        this.setPerson(person);
        return this;
    }

    public Competition getCompetition() {
        return this.competition;
    }

    public void setCompetition(Competition competition) {
        this.competition = competition;
    }

    public Suspension competition(Competition competition) {
        this.setCompetition(competition);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Suspension)) {
            return false;
        }
        return id != null && id.equals(((Suspension) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Suspension{" +
            "id=" + getId() +
            ", matches=" + getMatches() +
            ", moment='" + getMoment() + "'" +
            ", reason='" + getReason() + "'" +
            "}";
    }
}
