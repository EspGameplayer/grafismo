package grafismo.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link grafismo.domain.LocalAssociationRegion} entity.
 */
public class LocalAssociationRegionDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private AssociationDTO association;

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

    public AssociationDTO getAssociation() {
        return association;
    }

    public void setAssociation(AssociationDTO association) {
        this.association = association;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LocalAssociationRegionDTO)) {
            return false;
        }

        LocalAssociationRegionDTO localAssociationRegionDTO = (LocalAssociationRegionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, localAssociationRegionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LocalAssociationRegionDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", association=" + getAssociation() +
            "}";
    }
}
