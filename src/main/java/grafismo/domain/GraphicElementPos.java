package grafismo.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A GraphicElementPos.
 */
@Entity
@Table(name = "graphic_element_pos")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class GraphicElementPos implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "x")
    private Double x;

    @Column(name = "y")
    private Double y;

    @ManyToOne
    @JsonIgnoreProperties(value = { "parent", "children" }, allowSetters = true)
    private GraphicElementPos parent;

    @OneToMany(mappedBy = "parent")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "parent", "children" }, allowSetters = true)
    private Set<GraphicElementPos> children = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public GraphicElementPos id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public GraphicElementPos name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getX() {
        return this.x;
    }

    public GraphicElementPos x(Double x) {
        this.setX(x);
        return this;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return this.y;
    }

    public GraphicElementPos y(Double y) {
        this.setY(y);
        return this;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public GraphicElementPos getParent() {
        return this.parent;
    }

    public void setParent(GraphicElementPos graphicElementPos) {
        this.parent = graphicElementPos;
    }

    public GraphicElementPos parent(GraphicElementPos graphicElementPos) {
        this.setParent(graphicElementPos);
        return this;
    }

    public Set<GraphicElementPos> getChildren() {
        return this.children;
    }

    public void setChildren(Set<GraphicElementPos> graphicElementPos) {
        if (this.children != null) {
            this.children.forEach(i -> i.setParent(null));
        }
        if (graphicElementPos != null) {
            graphicElementPos.forEach(i -> i.setParent(this));
        }
        this.children = graphicElementPos;
    }

    public GraphicElementPos children(Set<GraphicElementPos> graphicElementPos) {
        this.setChildren(graphicElementPos);
        return this;
    }

    public GraphicElementPos addChild(GraphicElementPos graphicElementPos) {
        this.children.add(graphicElementPos);
        graphicElementPos.setParent(this);
        return this;
    }

    public GraphicElementPos removeChild(GraphicElementPos graphicElementPos) {
        this.children.remove(graphicElementPos);
        graphicElementPos.setParent(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GraphicElementPos)) {
            return false;
        }
        return id != null && id.equals(((GraphicElementPos) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GraphicElementPos{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", x=" + getX() +
            ", y=" + getY() +
            "}";
    }
}
