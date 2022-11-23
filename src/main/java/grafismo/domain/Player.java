package grafismo.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import grafismo.domain.enumeration.Foot;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Player.
 */
@Entity
@Table(name = "player")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Player implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "stronger_foot")
    private Foot strongerFoot;

    @Min(value = 0)
    @Column(name = "height")
    private Integer height;

    @Min(value = 0)
    @Column(name = "weight")
    private Integer weight;

    @Lob
    @Column(name = "photo")
    private byte[] photo;

    @Column(name = "photo_content_type")
    private String photoContentType;

    @JsonIgnoreProperties(value = { "nationality" }, allowSetters = true)
    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private Person person;

    @ManyToOne
    @JsonIgnoreProperties(value = { "preferredFormation", "stadiums", "competitions" }, allowSetters = true)
    private Team team;

    @ManyToMany
    @JoinTable(
        name = "rel_player__position",
        joinColumns = @JoinColumn(name = "player_id"),
        inverseJoinColumns = @JoinColumn(name = "position_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "parents", "children", "players", "competitionPlayers" }, allowSetters = true)
    private Set<Position> positions = new HashSet<>();

    @ManyToMany(mappedBy = "players")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "matchStats", "players" }, allowSetters = true)
    private Set<Action> actions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Player id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Foot getStrongerFoot() {
        return this.strongerFoot;
    }

    public Player strongerFoot(Foot strongerFoot) {
        this.setStrongerFoot(strongerFoot);
        return this;
    }

    public void setStrongerFoot(Foot strongerFoot) {
        this.strongerFoot = strongerFoot;
    }

    public Integer getHeight() {
        return this.height;
    }

    public Player height(Integer height) {
        this.setHeight(height);
        return this;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getWeight() {
        return this.weight;
    }

    public Player weight(Integer weight) {
        this.setWeight(weight);
        return this;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public byte[] getPhoto() {
        return this.photo;
    }

    public Player photo(byte[] photo) {
        this.setPhoto(photo);
        return this;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getPhotoContentType() {
        return this.photoContentType;
    }

    public Player photoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
        return this;
    }

    public void setPhotoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
    }

    public Person getPerson() {
        return this.person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Player person(Person person) {
        this.setPerson(person);
        return this;
    }

    public Team getTeam() {
        return this.team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Player team(Team team) {
        this.setTeam(team);
        return this;
    }

    public Set<Position> getPositions() {
        return this.positions;
    }

    public void setPositions(Set<Position> positions) {
        this.positions = positions;
    }

    public Player positions(Set<Position> positions) {
        this.setPositions(positions);
        return this;
    }

    public Player addPosition(Position position) {
        this.positions.add(position);
        position.getPlayers().add(this);
        return this;
    }

    public Player removePosition(Position position) {
        this.positions.remove(position);
        position.getPlayers().remove(this);
        return this;
    }

    public Set<Action> getActions() {
        return this.actions;
    }

    public void setActions(Set<Action> actions) {
        if (this.actions != null) {
            this.actions.forEach(i -> i.removePlayer(this));
        }
        if (actions != null) {
            actions.forEach(i -> i.addPlayer(this));
        }
        this.actions = actions;
    }

    public Player actions(Set<Action> actions) {
        this.setActions(actions);
        return this;
    }

    public Player addAction(Action action) {
        this.actions.add(action);
        action.getPlayers().add(this);
        return this;
    }

    public Player removeAction(Action action) {
        this.actions.remove(action);
        action.getPlayers().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Player)) {
            return false;
        }
        return id != null && id.equals(((Player) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Player{" +
            "id=" + getId() +
            ", strongerFoot='" + getStrongerFoot() + "'" +
            ", height=" + getHeight() +
            ", weight=" + getWeight() +
            ", photo='" + getPhoto() + "'" +
            ", photoContentType='" + getPhotoContentType() + "'" +
            "}";
    }
}
