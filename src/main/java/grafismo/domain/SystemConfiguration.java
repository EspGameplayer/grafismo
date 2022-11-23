package grafismo.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SystemConfiguration.
 */
@Entity
@Table(name = "system_configuration")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SystemConfiguration implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "current_period_start_moment")
    private Instant currentPeriodStartMoment;

    @OneToOne
    @JoinColumn(unique = true)
    private Season currentSeason;

    @OneToOne
    @JoinColumn(unique = true)
    private Sponsor defaultSponsorLogo;

    @OneToOne
    @JoinColumn(unique = true)
    private Sponsor defaultMotmSponsorLogo;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SystemConfiguration id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCurrentPeriodStartMoment() {
        return this.currentPeriodStartMoment;
    }

    public SystemConfiguration currentPeriodStartMoment(Instant currentPeriodStartMoment) {
        this.setCurrentPeriodStartMoment(currentPeriodStartMoment);
        return this;
    }

    public void setCurrentPeriodStartMoment(Instant currentPeriodStartMoment) {
        this.currentPeriodStartMoment = currentPeriodStartMoment;
    }

    public Season getCurrentSeason() {
        return this.currentSeason;
    }

    public void setCurrentSeason(Season season) {
        this.currentSeason = season;
    }

    public SystemConfiguration currentSeason(Season season) {
        this.setCurrentSeason(season);
        return this;
    }

    public Sponsor getDefaultSponsorLogo() {
        return this.defaultSponsorLogo;
    }

    public void setDefaultSponsorLogo(Sponsor sponsor) {
        this.defaultSponsorLogo = sponsor;
    }

    public SystemConfiguration defaultSponsorLogo(Sponsor sponsor) {
        this.setDefaultSponsorLogo(sponsor);
        return this;
    }

    public Sponsor getDefaultMotmSponsorLogo() {
        return this.defaultMotmSponsorLogo;
    }

    public void setDefaultMotmSponsorLogo(Sponsor sponsor) {
        this.defaultMotmSponsorLogo = sponsor;
    }

    public SystemConfiguration defaultMotmSponsorLogo(Sponsor sponsor) {
        this.setDefaultMotmSponsorLogo(sponsor);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SystemConfiguration)) {
            return false;
        }
        return id != null && id.equals(((SystemConfiguration) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SystemConfiguration{" +
            "id=" + getId() +
            ", currentPeriodStartMoment='" + getCurrentPeriodStartMoment() + "'" +
            "}";
    }
}
