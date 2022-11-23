package grafismo.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ActionKey.
 */
@Entity
@Table(name = "action_key")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ActionKey implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "action")
    private String action;

    @Column(name = "jhi_keys")
    private String keys;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ActionKey id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAction() {
        return this.action;
    }

    public ActionKey action(String action) {
        this.setAction(action);
        return this;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getKeys() {
        return this.keys;
    }

    public ActionKey keys(String keys) {
        this.setKeys(keys);
        return this;
    }

    public void setKeys(String keys) {
        this.keys = keys;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ActionKey)) {
            return false;
        }
        return id != null && id.equals(((ActionKey) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ActionKey{" +
            "id=" + getId() +
            ", action='" + getAction() + "'" +
            ", keys='" + getKeys() + "'" +
            "}";
    }
}
