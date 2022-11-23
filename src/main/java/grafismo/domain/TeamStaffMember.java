package grafismo.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import grafismo.domain.enumeration.StaffMemberRole;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TeamStaffMember.
 */
@Entity
@Table(name = "team_staff_member")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TeamStaffMember implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private StaffMemberRole role;

    @JsonIgnoreProperties(value = { "nationality" }, allowSetters = true)
    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private Person person;

    @ManyToOne
    @JsonIgnoreProperties(value = { "preferredFormation", "stadiums", "competitions" }, allowSetters = true)
    private Team team;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TeamStaffMember id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StaffMemberRole getRole() {
        return this.role;
    }

    public TeamStaffMember role(StaffMemberRole role) {
        this.setRole(role);
        return this;
    }

    public void setRole(StaffMemberRole role) {
        this.role = role;
    }

    public Person getPerson() {
        return this.person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public TeamStaffMember person(Person person) {
        this.setPerson(person);
        return this;
    }

    public Team getTeam() {
        return this.team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public TeamStaffMember team(Team team) {
        this.setTeam(team);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TeamStaffMember)) {
            return false;
        }
        return id != null && id.equals(((TeamStaffMember) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TeamStaffMember{" +
            "id=" + getId() +
            ", role='" + getRole() + "'" +
            "}";
    }
}
