package grafismo.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link grafismo.domain.ActionKey} entity.
 */
public class ActionKeyDTO implements Serializable {

    private Long id;

    private String action;

    private String keys;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getKeys() {
        return keys;
    }

    public void setKeys(String keys) {
        this.keys = keys;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ActionKeyDTO)) {
            return false;
        }

        ActionKeyDTO actionKeyDTO = (ActionKeyDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, actionKeyDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ActionKeyDTO{" +
            "id=" + getId() +
            ", action='" + getAction() + "'" +
            ", keys='" + getKeys() + "'" +
            "}";
    }
}
