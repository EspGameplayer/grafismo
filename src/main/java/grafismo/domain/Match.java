package grafismo.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Match.
 */
@Entity
@Table(name = "jhi_match")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Match implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "moment")
    private Instant moment;

    @Min(value = 0)
    @Column(name = "attendance")
    private Integer attendance;

    @JsonIgnoreProperties(value = { "player", "position", "motmMatch", "captainCallup", "callups", "lineups" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private MatchPlayer motm;

    @JsonIgnoreProperties(value = { "captain", "dt", "dt2", "teamDelegate", "players", "homeMatch", "awayMatch" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Callup homeCallup;

    @JsonIgnoreProperties(value = { "captain", "dt", "dt2", "teamDelegate", "players", "homeMatch", "awayMatch" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Callup awayCallup;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "preferredFormation", "stadiums", "competitions" }, allowSetters = true)
    private Team homeTeam;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "preferredFormation", "stadiums", "competitions" }, allowSetters = true)
    private Team awayTeam;

    @ManyToOne
    @JsonIgnoreProperties(value = { "teams" }, allowSetters = true)
    private Stadium stadium;

    @ManyToOne
    @JsonIgnoreProperties(value = { "person", "team" }, allowSetters = true)
    private TeamStaffMember matchDelegate;

    @ManyToOne
    @JsonIgnoreProperties(value = { "team", "season" }, allowSetters = true)
    private Shirt homeShirt;

    @ManyToOne
    @JsonIgnoreProperties(value = { "team", "season" }, allowSetters = true)
    private Shirt awayShirt;

    @ManyToOne
    @JsonIgnoreProperties(value = { "competition" }, allowSetters = true)
    private Matchday matchday;

    @ManyToMany
    @JoinTable(
        name = "rel_jhi_match__referee",
        joinColumns = @JoinColumn(name = "jhi_match_id"),
        inverseJoinColumns = @JoinColumn(name = "referee_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "person", "association", "matches" }, allowSetters = true)
    private Set<Referee> referees = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Match id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getMoment() {
        return this.moment;
    }

    public Match moment(Instant moment) {
        this.setMoment(moment);
        return this;
    }

    public void setMoment(Instant moment) {
        this.moment = moment;
    }

    public Integer getAttendance() {
        return this.attendance;
    }

    public Match attendance(Integer attendance) {
        this.setAttendance(attendance);
        return this;
    }

    public void setAttendance(Integer attendance) {
        this.attendance = attendance;
    }

    public MatchPlayer getMotm() {
        return this.motm;
    }

    public void setMotm(MatchPlayer matchPlayer) {
        this.motm = matchPlayer;
    }

    public Match motm(MatchPlayer matchPlayer) {
        this.setMotm(matchPlayer);
        return this;
    }

    public Callup getHomeCallup() {
        return this.homeCallup;
    }

    public void setHomeCallup(Callup callup) {
        this.homeCallup = callup;
    }

    public Match homeCallup(Callup callup) {
        this.setHomeCallup(callup);
        return this;
    }

    public Callup getAwayCallup() {
        return this.awayCallup;
    }

    public void setAwayCallup(Callup callup) {
        this.awayCallup = callup;
    }

    public Match awayCallup(Callup callup) {
        this.setAwayCallup(callup);
        return this;
    }

    public Team getHomeTeam() {
        return this.homeTeam;
    }

    public void setHomeTeam(Team team) {
        this.homeTeam = team;
    }

    public Match homeTeam(Team team) {
        this.setHomeTeam(team);
        return this;
    }

    public Team getAwayTeam() {
        return this.awayTeam;
    }

    public void setAwayTeam(Team team) {
        this.awayTeam = team;
    }

    public Match awayTeam(Team team) {
        this.setAwayTeam(team);
        return this;
    }

    public Stadium getStadium() {
        return this.stadium;
    }

    public void setStadium(Stadium stadium) {
        this.stadium = stadium;
    }

    public Match stadium(Stadium stadium) {
        this.setStadium(stadium);
        return this;
    }

    public TeamStaffMember getMatchDelegate() {
        return this.matchDelegate;
    }

    public void setMatchDelegate(TeamStaffMember teamStaffMember) {
        this.matchDelegate = teamStaffMember;
    }

    public Match matchDelegate(TeamStaffMember teamStaffMember) {
        this.setMatchDelegate(teamStaffMember);
        return this;
    }

    public Shirt getHomeShirt() {
        return this.homeShirt;
    }

    public void setHomeShirt(Shirt shirt) {
        this.homeShirt = shirt;
    }

    public Match homeShirt(Shirt shirt) {
        this.setHomeShirt(shirt);
        return this;
    }

    public Shirt getAwayShirt() {
        return this.awayShirt;
    }

    public void setAwayShirt(Shirt shirt) {
        this.awayShirt = shirt;
    }

    public Match awayShirt(Shirt shirt) {
        this.setAwayShirt(shirt);
        return this;
    }

    public Matchday getMatchday() {
        return this.matchday;
    }

    public void setMatchday(Matchday matchday) {
        this.matchday = matchday;
    }

    public Match matchday(Matchday matchday) {
        this.setMatchday(matchday);
        return this;
    }

    public Set<Referee> getReferees() {
        return this.referees;
    }

    public void setReferees(Set<Referee> referees) {
        this.referees = referees;
    }

    public Match referees(Set<Referee> referees) {
        this.setReferees(referees);
        return this;
    }

    public Match addReferee(Referee referee) {
        this.referees.add(referee);
        referee.getMatches().add(this);
        return this;
    }

    public Match removeReferee(Referee referee) {
        this.referees.remove(referee);
        referee.getMatches().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Match)) {
            return false;
        }
        return id != null && id.equals(((Match) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Match{" +
            "id=" + getId() +
            ", moment='" + getMoment() + "'" +
            ", attendance=" + getAttendance() +
            "}";
    }
}
