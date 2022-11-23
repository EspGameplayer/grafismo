package grafismo.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Referee.
 */
@Entity
@Table(name = "referee")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Referee implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JsonIgnoreProperties(value = { "nationality" }, allowSetters = true)
    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private Person person;

    @ManyToOne
    @JsonIgnoreProperties(value = { "association" }, allowSetters = true)
    private LocalAssociationProvince association;

    @ManyToMany(mappedBy = "referees")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = {
            "motm",
            "homeCallup",
            "awayCallup",
            "homeTeam",
            "awayTeam",
            "stadium",
            "matchDelegate",
            "homeShirt",
            "awayShirt",
            "matchday",
            "referees",
        },
        allowSetters = true
    )
    private Set<Match> matches = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Referee id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Person getPerson() {
        return this.person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Referee person(Person person) {
        this.setPerson(person);
        return this;
    }

    public LocalAssociationProvince getAssociation() {
        return this.association;
    }

    public void setAssociation(LocalAssociationProvince localAssociationProvince) {
        this.association = localAssociationProvince;
    }

    public Referee association(LocalAssociationProvince localAssociationProvince) {
        this.setAssociation(localAssociationProvince);
        return this;
    }

    public Set<Match> getMatches() {
        return this.matches;
    }

    public void setMatches(Set<Match> matches) {
        if (this.matches != null) {
            this.matches.forEach(i -> i.removeReferee(this));
        }
        if (matches != null) {
            matches.forEach(i -> i.addReferee(this));
        }
        this.matches = matches;
    }

    public Referee matches(Set<Match> matches) {
        this.setMatches(matches);
        return this;
    }

    public Referee addMatch(Match match) {
        this.matches.add(match);
        match.getReferees().add(this);
        return this;
    }

    public Referee removeMatch(Match match) {
        this.matches.remove(match);
        match.getReferees().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Referee)) {
            return false;
        }
        return id != null && id.equals(((Referee) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Referee{" +
            "id=" + getId() +
            "}";
    }
}
