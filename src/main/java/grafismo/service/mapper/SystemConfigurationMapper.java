package grafismo.service.mapper;

import grafismo.domain.Season;
import grafismo.domain.Sponsor;
import grafismo.domain.SystemConfiguration;
import grafismo.service.dto.SeasonDTO;
import grafismo.service.dto.SponsorDTO;
import grafismo.service.dto.SystemConfigurationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SystemConfiguration} and its DTO {@link SystemConfigurationDTO}.
 */
@Mapper(componentModel = "spring")
public interface SystemConfigurationMapper extends EntityMapper<SystemConfigurationDTO, SystemConfiguration> {
    @Mapping(target = "currentSeason", source = "currentSeason", qualifiedByName = "seasonName")
    @Mapping(target = "defaultSponsorLogo", source = "defaultSponsorLogo", qualifiedByName = "sponsorName")
    @Mapping(target = "defaultMotmSponsorLogo", source = "defaultMotmSponsorLogo", qualifiedByName = "sponsorName")
    SystemConfigurationDTO toDto(SystemConfiguration s);

    @Named("seasonName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    SeasonDTO toDtoSeasonName(Season season);

    @Named("sponsorName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    SponsorDTO toDtoSponsorName(Sponsor sponsor);
}
