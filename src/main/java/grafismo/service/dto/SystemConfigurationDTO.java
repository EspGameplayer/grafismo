package grafismo.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link grafismo.domain.SystemConfiguration} entity.
 */
public class SystemConfigurationDTO implements Serializable {

    private Long id;

    private Instant currentPeriodStartMoment;

    private SeasonDTO currentSeason;

    private SponsorDTO defaultSponsorLogo;

    private SponsorDTO defaultMotmSponsorLogo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCurrentPeriodStartMoment() {
        return currentPeriodStartMoment;
    }

    public void setCurrentPeriodStartMoment(Instant currentPeriodStartMoment) {
        this.currentPeriodStartMoment = currentPeriodStartMoment;
    }

    public SeasonDTO getCurrentSeason() {
        return currentSeason;
    }

    public void setCurrentSeason(SeasonDTO currentSeason) {
        this.currentSeason = currentSeason;
    }

    public SponsorDTO getDefaultSponsorLogo() {
        return defaultSponsorLogo;
    }

    public void setDefaultSponsorLogo(SponsorDTO defaultSponsorLogo) {
        this.defaultSponsorLogo = defaultSponsorLogo;
    }

    public SponsorDTO getDefaultMotmSponsorLogo() {
        return defaultMotmSponsorLogo;
    }

    public void setDefaultMotmSponsorLogo(SponsorDTO defaultMotmSponsorLogo) {
        this.defaultMotmSponsorLogo = defaultMotmSponsorLogo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SystemConfigurationDTO)) {
            return false;
        }

        SystemConfigurationDTO systemConfigurationDTO = (SystemConfigurationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, systemConfigurationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SystemConfigurationDTO{" +
            "id=" + getId() +
            ", currentPeriodStartMoment='" + getCurrentPeriodStartMoment() + "'" +
            ", currentSeason=" + getCurrentSeason() +
            ", defaultSponsorLogo=" + getDefaultSponsorLogo() +
            ", defaultMotmSponsorLogo=" + getDefaultMotmSponsorLogo() +
            "}";
    }
}
