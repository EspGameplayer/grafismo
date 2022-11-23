package grafismo.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Shirt.
 */
@Entity
@Table(name = "shirt")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Shirt implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Pattern(regexp = "^[0-9a-f]{6}$|^$")
    @Column(name = "colour_1")
    private String colour1;

    @Pattern(regexp = "^[0-9a-f]{6}$|^$")
    @Column(name = "colour_2")
    private String colour2;

    @Min(value = 1)
    @Max(value = 3)
    @Column(name = "type")
    private Integer type;

    @ManyToOne
    @JsonIgnoreProperties(value = { "preferredFormation", "stadiums", "competitions" }, allowSetters = true)
    private Team team;

    @ManyToOne
    private Season season;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Shirt id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getColour1() {
        return this.colour1;
    }

    public Shirt colour1(String colour1) {
        this.setColour1(colour1);
        return this;
    }

    public void setColour1(String colour1) {
        this.colour1 = colour1;
    }

    public String getColour2() {
        return this.colour2;
    }

    public Shirt colour2(String colour2) {
        this.setColour2(colour2);
        return this;
    }

    public void setColour2(String colour2) {
        this.colour2 = colour2;
    }

    public Integer getType() {
        return this.type;
    }

    public Shirt type(Integer type) {
        this.setType(type);
        return this;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Team getTeam() {
        return this.team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Shirt team(Team team) {
        this.setTeam(team);
        return this;
    }

    public Season getSeason() {
        return this.season;
    }

    public void setSeason(Season season) {
        this.season = season;
    }

    public Shirt season(Season season) {
        this.setSeason(season);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Shirt)) {
            return false;
        }
        return id != null && id.equals(((Shirt) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Shirt{" +
            "id=" + getId() +
            ", colour1='" + getColour1() + "'" +
            ", colour2='" + getColour2() + "'" +
            ", type=" + getType() +
            "}";
    }
}
