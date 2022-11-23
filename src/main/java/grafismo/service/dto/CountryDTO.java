package grafismo.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link grafismo.domain.Country} entity.
 */
public class CountryDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String abb;

    @Lob
    private byte[] flag;

    private String flagContentType;

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

    public String getAbb() {
        return abb;
    }

    public void setAbb(String abb) {
        this.abb = abb;
    }

    public byte[] getFlag() {
        return flag;
    }

    public void setFlag(byte[] flag) {
        this.flag = flag;
    }

    public String getFlagContentType() {
        return flagContentType;
    }

    public void setFlagContentType(String flagContentType) {
        this.flagContentType = flagContentType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CountryDTO)) {
            return false;
        }

        CountryDTO countryDTO = (CountryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, countryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CountryDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", abb='" + getAbb() + "'" +
            ", flag='" + getFlag() + "'" +
            "}";
    }
}
