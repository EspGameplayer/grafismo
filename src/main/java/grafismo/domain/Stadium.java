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
 * A Stadium.
 */
@Entity
@Table(name = "stadium")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Stadium implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @NotNull
    @Column(name = "graphics_name", nullable = false)
    private String graphicsName;

    @Column(name = "location")
    private String location;

    @Min(value = 0)
    @Column(name = "capacity")
    private Integer capacity;

    @Min(value = 0)
    @Column(name = "field_length")
    private Integer fieldLength;

    @Min(value = 0)
    @Column(name = "field_width")
    private Integer fieldWidth;

    @ManyToMany(mappedBy = "stadiums")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "preferredFormation", "stadiums", "competitions" }, allowSetters = true)
    private Set<Team> teams = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Stadium id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Stadium name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGraphicsName() {
        return this.graphicsName;
    }

    public Stadium graphicsName(String graphicsName) {
        this.setGraphicsName(graphicsName);
        return this;
    }

    public void setGraphicsName(String graphicsName) {
        this.graphicsName = graphicsName;
    }

    public String getLocation() {
        return this.location;
    }

    public Stadium location(String location) {
        this.setLocation(location);
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getCapacity() {
        return this.capacity;
    }

    public Stadium capacity(Integer capacity) {
        this.setCapacity(capacity);
        return this;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Integer getFieldLength() {
        return this.fieldLength;
    }

    public Stadium fieldLength(Integer fieldLength) {
        this.setFieldLength(fieldLength);
        return this;
    }

    public void setFieldLength(Integer fieldLength) {
        this.fieldLength = fieldLength;
    }

    public Integer getFieldWidth() {
        return this.fieldWidth;
    }

    public Stadium fieldWidth(Integer fieldWidth) {
        this.setFieldWidth(fieldWidth);
        return this;
    }

    public void setFieldWidth(Integer fieldWidth) {
        this.fieldWidth = fieldWidth;
    }

    public Set<Team> getTeams() {
        return this.teams;
    }

    public void setTeams(Set<Team> teams) {
        if (this.teams != null) {
            this.teams.forEach(i -> i.removeStadium(this));
        }
        if (teams != null) {
            teams.forEach(i -> i.addStadium(this));
        }
        this.teams = teams;
    }

    public Stadium teams(Set<Team> teams) {
        this.setTeams(teams);
        return this;
    }

    public Stadium addTeam(Team team) {
        this.teams.add(team);
        team.getStadiums().add(this);
        return this;
    }

    public Stadium removeTeam(Team team) {
        this.teams.remove(team);
        team.getStadiums().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Stadium)) {
            return false;
        }
        return id != null && id.equals(((Stadium) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Stadium{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", graphicsName='" + getGraphicsName() + "'" +
            ", location='" + getLocation() + "'" +
            ", capacity=" + getCapacity() +
            ", fieldLength=" + getFieldLength() +
            ", fieldWidth=" + getFieldWidth() +
            "}";
    }
}
