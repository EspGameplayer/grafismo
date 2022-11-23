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
 * A Team.
 */
@Entity
@Table(name = "team")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Team implements Serializable {

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
    @Column(name = "abb", nullable = false)
    private String abb;

    @Lob
    @Column(name = "badge")
    private byte[] badge;

    @Column(name = "badge_content_type")
    private String badgeContentType;

    @Lob
    @Column(name = "monoc_badge")
    private byte[] monocBadge;

    @Column(name = "monoc_badge_content_type")
    private String monocBadgeContentType;

    @ManyToOne
    private Formation preferredFormation;

    @ManyToMany
    @JoinTable(
        name = "rel_team__stadium",
        joinColumns = @JoinColumn(name = "team_id"),
        inverseJoinColumns = @JoinColumn(name = "stadium_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "teams" }, allowSetters = true)
    private Set<Stadium> stadiums = new HashSet<>();

    @ManyToMany(mappedBy = "teams")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "sponsor", "motmSponsor", "parent", "teams", "children" }, allowSetters = true)
    private Set<Competition> competitions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Team id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Team name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGraphicsName() {
        return this.graphicsName;
    }

    public Team graphicsName(String graphicsName) {
        this.setGraphicsName(graphicsName);
        return this;
    }

    public void setGraphicsName(String graphicsName) {
        this.graphicsName = graphicsName;
    }

    public String getAbb() {
        return this.abb;
    }

    public Team abb(String abb) {
        this.setAbb(abb);
        return this;
    }

    public void setAbb(String abb) {
        this.abb = abb;
    }

    public byte[] getBadge() {
        return this.badge;
    }

    public Team badge(byte[] badge) {
        this.setBadge(badge);
        return this;
    }

    public void setBadge(byte[] badge) {
        this.badge = badge;
    }

    public String getBadgeContentType() {
        return this.badgeContentType;
    }

    public Team badgeContentType(String badgeContentType) {
        this.badgeContentType = badgeContentType;
        return this;
    }

    public void setBadgeContentType(String badgeContentType) {
        this.badgeContentType = badgeContentType;
    }

    public byte[] getMonocBadge() {
        return this.monocBadge;
    }

    public Team monocBadge(byte[] monocBadge) {
        this.setMonocBadge(monocBadge);
        return this;
    }

    public void setMonocBadge(byte[] monocBadge) {
        this.monocBadge = monocBadge;
    }

    public String getMonocBadgeContentType() {
        return this.monocBadgeContentType;
    }

    public Team monocBadgeContentType(String monocBadgeContentType) {
        this.monocBadgeContentType = monocBadgeContentType;
        return this;
    }

    public void setMonocBadgeContentType(String monocBadgeContentType) {
        this.monocBadgeContentType = monocBadgeContentType;
    }

    public Formation getPreferredFormation() {
        return this.preferredFormation;
    }

    public void setPreferredFormation(Formation formation) {
        this.preferredFormation = formation;
    }

    public Team preferredFormation(Formation formation) {
        this.setPreferredFormation(formation);
        return this;
    }

    public Set<Stadium> getStadiums() {
        return this.stadiums;
    }

    public void setStadiums(Set<Stadium> stadiums) {
        this.stadiums = stadiums;
    }

    public Team stadiums(Set<Stadium> stadiums) {
        this.setStadiums(stadiums);
        return this;
    }

    public Team addStadium(Stadium stadium) {
        this.stadiums.add(stadium);
        stadium.getTeams().add(this);
        return this;
    }

    public Team removeStadium(Stadium stadium) {
        this.stadiums.remove(stadium);
        stadium.getTeams().remove(this);
        return this;
    }

    public Set<Competition> getCompetitions() {
        return this.competitions;
    }

    public void setCompetitions(Set<Competition> competitions) {
        if (this.competitions != null) {
            this.competitions.forEach(i -> i.removeTeam(this));
        }
        if (competitions != null) {
            competitions.forEach(i -> i.addTeam(this));
        }
        this.competitions = competitions;
    }

    public Team competitions(Set<Competition> competitions) {
        this.setCompetitions(competitions);
        return this;
    }

    public Team addCompetition(Competition competition) {
        this.competitions.add(competition);
        competition.getTeams().add(this);
        return this;
    }

    public Team removeCompetition(Competition competition) {
        this.competitions.remove(competition);
        competition.getTeams().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Team)) {
            return false;
        }
        return id != null && id.equals(((Team) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Team{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", graphicsName='" + getGraphicsName() + "'" +
            ", abb='" + getAbb() + "'" +
            ", badge='" + getBadge() + "'" +
            ", badgeContentType='" + getBadgeContentType() + "'" +
            ", monocBadge='" + getMonocBadge() + "'" +
            ", monocBadgeContentType='" + getMonocBadgeContentType() + "'" +
            "}";
    }
}
