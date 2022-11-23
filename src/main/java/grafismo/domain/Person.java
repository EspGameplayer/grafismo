package grafismo.domain;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Person.
 */
@Entity
@Table(name = "person")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Person implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "surname_1")
    private String surname1;

    @Column(name = "surname_2")
    private String surname2;

    @Column(name = "nickname")
    private String nickname;

    @NotNull
    @Column(name = "graphics_name", nullable = false)
    private String graphicsName;

    @Column(name = "callname")
    private String callname;

    @Column(name = "birthdate")
    private LocalDate birthdate;

    @ManyToOne
    private Country nationality;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Person id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Person name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname1() {
        return this.surname1;
    }

    public Person surname1(String surname1) {
        this.setSurname1(surname1);
        return this;
    }

    public void setSurname1(String surname1) {
        this.surname1 = surname1;
    }

    public String getSurname2() {
        return this.surname2;
    }

    public Person surname2(String surname2) {
        this.setSurname2(surname2);
        return this;
    }

    public void setSurname2(String surname2) {
        this.surname2 = surname2;
    }

    public String getNickname() {
        return this.nickname;
    }

    public Person nickname(String nickname) {
        this.setNickname(nickname);
        return this;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getGraphicsName() {
        return this.graphicsName;
    }

    public Person graphicsName(String graphicsName) {
        this.setGraphicsName(graphicsName);
        return this;
    }

    public void setGraphicsName(String graphicsName) {
        this.graphicsName = graphicsName;
    }

    public String getCallname() {
        return this.callname;
    }

    public Person callname(String callname) {
        this.setCallname(callname);
        return this;
    }

    public void setCallname(String callname) {
        this.callname = callname;
    }

    public LocalDate getBirthdate() {
        return this.birthdate;
    }

    public Person birthdate(LocalDate birthdate) {
        this.setBirthdate(birthdate);
        return this;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public Country getNationality() {
        return this.nationality;
    }

    public void setNationality(Country country) {
        this.nationality = country;
    }

    public Person nationality(Country country) {
        this.setNationality(country);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Person)) {
            return false;
        }
        return id != null && id.equals(((Person) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Person{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", surname1='" + getSurname1() + "'" +
            ", surname2='" + getSurname2() + "'" +
            ", nickname='" + getNickname() + "'" +
            ", graphicsName='" + getGraphicsName() + "'" +
            ", callname='" + getCallname() + "'" +
            ", birthdate='" + getBirthdate() + "'" +
            "}";
    }
}
