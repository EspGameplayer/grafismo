package grafismo.service.dto;

import grafismo.domain.enumeration.CompetitionType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link grafismo.domain.Competition} entity.
 */
public class CompetitionDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String graphicsName;

    @NotNull
    private CompetitionType type;

    @Pattern(regexp = "^[0-9a-fA-F]{6}$|^$")
    private String colour;

    @Min(value = 0)
    private Integer suspensionYcMatches;

    private SponsorDTO sponsor;

    private SponsorDTO motmSponsor;

    private CompetitionDTO parent;

    private Set<TeamDTO> teams = new HashSet<>();

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

    public CompetitionType getType() {
        return type;
    }

    public void setType(CompetitionType type) {
        this.type = type;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public Integer getSuspensionYcMatches() {
        return suspensionYcMatches;
    }

    public void setSuspensionYcMatches(Integer suspensionYcMatches) {
        this.suspensionYcMatches = suspensionYcMatches;
    }

    public SponsorDTO getSponsor() {
        return sponsor;
    }

    public void setSponsor(SponsorDTO sponsor) {
        this.sponsor = sponsor;
    }

    public SponsorDTO getMotmSponsor() {
        return motmSponsor;
    }

    public void setMotmSponsor(SponsorDTO motmSponsor) {
        this.motmSponsor = motmSponsor;
    }

    public CompetitionDTO getParent() {
        return parent;
    }

    public void setParent(CompetitionDTO parent) {
        this.parent = parent;
    }

    public Set<TeamDTO> getTeams() {
        return teams;
    }

    public void setTeams(Set<TeamDTO> teams) {
        this.teams = teams;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CompetitionDTO)) {
            return false;
        }

        CompetitionDTO competitionDTO = (CompetitionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, competitionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CompetitionDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", graphicsName='" + getGraphicsName() + "'" +
            ", type='" + getType() + "'" +
            ", colour='" + getColour() + "'" +
            ", suspensionYcMatches=" + getSuspensionYcMatches() +
            ", sponsor=" + getSponsor() +
            ", motmSponsor=" + getMotmSponsor() +
            ", parent=" + getParent() +
            ", teams=" + getTeams() +
            "}";
    }
}
