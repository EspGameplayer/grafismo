package grafismo.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link grafismo.domain.GraphicElementPos} entity.
 */
public class GraphicElementPosDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private Double x;

    private Double y;

    private GraphicElementPosDTO parent;

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

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public GraphicElementPosDTO getParent() {
        return parent;
    }

    public void setParent(GraphicElementPosDTO parent) {
        this.parent = parent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GraphicElementPosDTO)) {
            return false;
        }

        GraphicElementPosDTO graphicElementPosDTO = (GraphicElementPosDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, graphicElementPosDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GraphicElementPosDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", x=" + getX() +
            ", y=" + getY() +
            ", parent=" + getParent() +
            "}";
    }
}
