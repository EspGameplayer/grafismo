package grafismo.service.dto;

import grafismo.domain.enumeration.Foot;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link grafismo.domain.Player} entity.
 */
public class PlayerDTO implements Serializable {

    private Long id;

    private Foot strongerFoot;

    @Min(value = 0)
    private Integer height;

    @Min(value = 0)
    private Integer weight;

    @Lob
    private byte[] photo;

    private String photoContentType;
    private PersonDTO person;

    private TeamDTO team;

    private Set<PositionDTO> positions = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Foot getStrongerFoot() {
        return strongerFoot;
    }

    public void setStrongerFoot(Foot strongerFoot) {
        this.strongerFoot = strongerFoot;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getPhotoContentType() {
        return photoContentType;
    }

    public void setPhotoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
    }

    public PersonDTO getPerson() {
        return person;
    }

    public void setPerson(PersonDTO person) {
        this.person = person;
    }

    public TeamDTO getTeam() {
        return team;
    }

    public void setTeam(TeamDTO team) {
        this.team = team;
    }

    public Set<PositionDTO> getPositions() {
        return positions;
    }

    public void setPositions(Set<PositionDTO> positions) {
        this.positions = positions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PlayerDTO)) {
            return false;
        }

        PlayerDTO playerDTO = (PlayerDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, playerDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PlayerDTO{" +
            "id=" + getId() +
            ", strongerFoot='" + getStrongerFoot() + "'" +
            ", height=" + getHeight() +
            ", weight=" + getWeight() +
            ", photo='" + getPhoto() + "'" +
            ", person=" + getPerson() +
            ", team=" + getTeam() +
            ", positions=" + getPositions() +
            "}";
    }
}
