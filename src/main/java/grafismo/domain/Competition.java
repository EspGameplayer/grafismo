package grafismo.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import grafismo.domain.enumeration.CompetitionType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Competition.
 */
@Entity
@Table(name = "competition")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Competition implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "graphics_name", nullable = false)
    private String graphicsName;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private CompetitionType type;

    @Pattern(regexp = "^[0-9a-fA-F]{6}$|^$")
    @Column(name = "colour")
    private String colour;

    @Min(value = 0)
    @Column(name = "suspension_yc_matches")
    private Integer suspensionYcMatches;

    @ManyToOne
    private Sponsor sponsor;

    @ManyToOne
    private Sponsor motmSponsor;

    @ManyToOne
    @JsonIgnoreProperties(value = { "sponsor", "motmSponsor", "parent", "teams", "children" }, allowSetters = true)
    private Competition parent;

    @ManyToMany
    @JoinTable(
        name = "rel_competition__team",
        joinColumns = @JoinColumn(name = "competition_id"),
        inverseJoinColumns = @JoinColumn(name = "team_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "preferredFormation", "stadiums", "competitions" }, allowSetters = true)
    private Set<Team> teams = new HashSet<>();

    @OneToMany(mappedBy = "parent")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "sponsor", "motmSponsor", "parent", "teams", "children" }, allowSetters = true)
    private Set<Competition> children = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Competition id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Competition name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGraphicsName() {
        return this.graphicsName;
    }

    public Competition graphicsName(String graphicsName) {
        this.setGraphicsName(graphicsName);
        return this;
    }

    public void setGraphicsName(String graphicsName) {
        this.graphicsName = graphicsName;
    }

    public CompetitionType getType() {
        return this.type;
    }

    public Competition type(CompetitionType type) {
        this.setType(type);
        return this;
    }

    public void setType(CompetitionType type) {
        this.type = type;
    }

    public String getColour() {
        return this.colour;
    }

    public Competition colour(String colour) {
        this.setColour(colour);
        return this;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public Integer getSuspensionYcMatches() {
        return this.suspensionYcMatches;
    }

    public Competition suspensionYcMatches(Integer suspensionYcMatches) {
        this.setSuspensionYcMatches(suspensionYcMatches);
        return this;
    }

    public void setSuspensionYcMatches(Integer suspensionYcMatches) {
        this.suspensionYcMatches = suspensionYcMatches;
    }

    public Sponsor getSponsor() {
        return this.sponsor;
    }

    public void setSponsor(Sponsor sponsor) {
        this.sponsor = sponsor;
    }

    public Competition sponsor(Sponsor sponsor) {
        this.setSponsor(sponsor);
        return this;
    }

    public Sponsor getMotmSponsor() {
        return this.motmSponsor;
    }

    public void setMotmSponsor(Sponsor sponsor) {
        this.motmSponsor = sponsor;
    }

    public Competition motmSponsor(Sponsor sponsor) {
        this.setMotmSponsor(sponsor);
        return this;
    }

    public Competition getParent() {
        return this.parent;
    }

    public void setParent(Competition competition) {
        this.parent = competition;
    }

    public Competition parent(Competition competition) {
        this.setParent(competition);
        return this;
    }

    public Set<Team> getTeams() {
        return this.teams;
    }

    public void setTeams(Set<Team> teams) {
        this.teams = teams;
    }

    public Competition teams(Set<Team> teams) {
        this.setTeams(teams);
        return this;
    }

    public Competition addTeam(Team team) {
        this.teams.add(team);
        team.getCompetitions().add(this);
        return this;
    }

    public Competition removeTeam(Team team) {
        this.teams.remove(team);
        team.getCompetitions().remove(this);
        return this;
    }

    public Set<Competition> getChildren() {
        return this.children;
    }

    public void setChildren(Set<Competition> competitions) {
        if (this.children != null) {
            this.children.forEach(i -> i.setParent(null));
        }
        if (competitions != null) {
            competitions.forEach(i -> i.setParent(this));
        }
        this.children = competitions;
    }

    public Competition children(Set<Competition> competitions) {
        this.setChildren(competitions);
        return this;
    }

    public Competition addChild(Competition competition) {
        this.children.add(competition);
        competition.setParent(this);
        return this;
    }

    public Competition removeChild(Competition competition) {
        this.children.remove(competition);
        competition.setParent(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Competition)) {
            return false;
        }
        return id != null && id.equals(((Competition) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Competition{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", graphicsName='" + getGraphicsName() + "'" +
            ", type='" + getType() + "'" +
            ", colour='" + getColour() + "'" +
            ", suspensionYcMatches=" + getSuspensionYcMatches() +
            "}";
    }
}
