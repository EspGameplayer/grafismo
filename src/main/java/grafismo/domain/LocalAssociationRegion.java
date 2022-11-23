package grafismo.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A LocalAssociationRegion.
 */
@Entity
@Table(name = "local_association_region")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class LocalAssociationRegion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne
    private Association association;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public LocalAssociationRegion id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public LocalAssociationRegion name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Association getAssociation() {
        return this.association;
    }

    public void setAssociation(Association association) {
        this.association = association;
    }

    public LocalAssociationRegion association(Association association) {
        this.setAssociation(association);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LocalAssociationRegion)) {
            return false;
        }
        return id != null && id.equals(((LocalAssociationRegion) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LocalAssociationRegion{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
