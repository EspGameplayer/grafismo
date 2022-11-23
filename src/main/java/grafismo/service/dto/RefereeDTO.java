package grafismo.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link grafismo.domain.Referee} entity.
 */
public class RefereeDTO implements Serializable {

    private Long id;

    private PersonDTO person;

    private LocalAssociationProvinceDTO association;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PersonDTO getPerson() {
        return person;
    }

    public void setPerson(PersonDTO person) {
        this.person = person;
    }

    public LocalAssociationProvinceDTO getAssociation() {
        return association;
    }

    public void setAssociation(LocalAssociationProvinceDTO association) {
        this.association = association;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RefereeDTO)) {
            return false;
        }

        RefereeDTO refereeDTO = (RefereeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, refereeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RefereeDTO{" +
            "id=" + getId() +
            ", person=" + getPerson() +
            ", association=" + getAssociation() +
            "}";
    }
}
