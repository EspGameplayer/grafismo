package grafismo.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link grafismo.domain.LocalAssociationProvince} entity.
 */
public class LocalAssociationProvinceDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private LocalAssociationRegionDTO association;

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

    public LocalAssociationRegionDTO getAssociation() {
        return association;
    }

    public void setAssociation(LocalAssociationRegionDTO association) {
        this.association = association;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LocalAssociationProvinceDTO)) {
            return false;
        }

        LocalAssociationProvinceDTO localAssociationProvinceDTO = (LocalAssociationProvinceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, localAssociationProvinceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LocalAssociationProvinceDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", association=" + getAssociation() +
            "}";
    }
}
