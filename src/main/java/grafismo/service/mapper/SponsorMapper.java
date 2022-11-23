package grafismo.service.mapper;

import grafismo.domain.Sponsor;
import grafismo.service.dto.SponsorDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Sponsor} and its DTO {@link SponsorDTO}.
 */
@Mapper(componentModel = "spring")
public interface SponsorMapper extends EntityMapper<SponsorDTO, Sponsor> {}
