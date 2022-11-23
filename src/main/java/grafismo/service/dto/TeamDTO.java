package grafismo.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link grafismo.domain.Team} entity.
 */
public class TeamDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String graphicsName;

    @NotNull
    private String abb;

    @Lob
    private byte[] badge;

    private String badgeContentType;

    @Lob
    private byte[] monocBadge;

    private String monocBadgeContentType;
    private FormationDTO preferredFormation;

    private Set<StadiumDTO> stadiums = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGraphicsName() {
        return graphicsName;
    }

    public void setGraphicsName(String graphicsName) {
        this.graphicsName = graphicsName;
    }

    public String getAbb() {
        return abb;
    }

    public void setAbb(String abb) {
        this.abb = abb;
    }

    public byte[] getBadge() {
        return badge;
    }

    public void setBadge(byte[] badge) {
        this.badge = badge;
    }

    public String getBadgeContentType() {
        return badgeContentType;
    }

    public void setBadgeContentType(String badgeContentType) {
        this.badgeContentType = badgeContentType;
    }

    public byte[] getMonocBadge() {
        return monocBadge;
    }

    public void setMonocBadge(byte[] monocBadge) {
        this.monocBadge = monocBadge;
    }

    public String getMonocBadgeContentType() {
        return monocBadgeContentType;
    }

    public void setMonocBadgeContentType(String monocBadgeContentType) {
        this.monocBadgeContentType = monocBadgeContentType;
    }

    public FormationDTO getPreferredFormation() {
        return preferredFormation;
    }

    public void setPreferredFormation(FormationDTO preferredFormation) {
        this.preferredFormation = preferredFormation;
    }

    public Set<StadiumDTO> getStadiums() {
        return stadiums;
    }

    public void setStadiums(Set<StadiumDTO> stadiums) {
        this.stadiums = stadiums;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TeamDTO)) {
            return false;
        }

        TeamDTO teamDTO = (TeamDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, teamDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TeamDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", graphicsName='" + getGraphicsName() + "'" +
            ", abb='" + getAbb() + "'" +
            ", badge='" + getBadge() + "'" +
            ", monocBadge='" + getMonocBadge() + "'" +
            ", preferredFormation=" + getPreferredFormation() +
            ", stadiums=" + getStadiums() +
            "}";
    }
}
