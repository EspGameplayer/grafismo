package grafismo.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link grafismo.domain.Stadium} entity.
 */
public class StadiumDTO implements Serializable {

    private Long id;

    private String name;

    @NotNull
    private String graphicsName;

    private String location;

    @Min(value = 0)
    private Integer capacity;

    @Min(value = 0)
    private Integer fieldLength;

    @Min(value = 0)
    private Integer fieldWidth;

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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Integer getFieldLength() {
        return fieldLength;
    }

    public void setFieldLength(Integer fieldLength) {
        this.fieldLength = fieldLength;
    }

    public Integer getFieldWidth() {
        return fieldWidth;
    }

    public void setFieldWidth(Integer fieldWidth) {
        this.fieldWidth = fieldWidth;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StadiumDTO)) {
            return false;
        }

        StadiumDTO stadiumDTO = (StadiumDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, stadiumDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StadiumDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", graphicsName='" + getGraphicsName() + "'" +
            ", location='" + getLocation() + "'" +
            ", capacity=" + getCapacity() +
            ", fieldLength=" + getFieldLength() +
            ", fieldWidth=" + getFieldWidth() +
            "}";
    }
}
