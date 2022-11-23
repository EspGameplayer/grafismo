package grafismo.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Country.
 */
@Entity
@Table(name = "country")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Country implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "abb", nullable = false)
    private String abb;

    @Lob
    @Column(name = "flag")
    private byte[] flag;

    @Column(name = "flag_content_type")
    private String flagContentType;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Country id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Country name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbb() {
        return this.abb;
    }

    public Country abb(String abb) {
        this.setAbb(abb);
        return this;
    }

    public void setAbb(String abb) {
        this.abb = abb;
    }

    public byte[] getFlag() {
        return this.flag;
    }

    public Country flag(byte[] flag) {
        this.setFlag(flag);
        return this;
    }

    public void setFlag(byte[] flag) {
        this.flag = flag;
    }

    public String getFlagContentType() {
        return this.flagContentType;
    }

    public Country flagContentType(String flagContentType) {
        this.flagContentType = flagContentType;
        return this;
    }

    public void setFlagContentType(String flagContentType) {
        this.flagContentType = flagContentType;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Country)) {
            return false;
        }
        return id != null && id.equals(((Country) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Country{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", abb='" + getAbb() + "'" +
            ", flag='" + getFlag() + "'" +
            ", flagContentType='" + getFlagContentType() + "'" +
            "}";
    }
}
