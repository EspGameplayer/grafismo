package grafismo.service.mapper;

import grafismo.domain.Stadium;
import grafismo.service.dto.StadiumDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Stadium} and its DTO {@link StadiumDTO}.
 */
@Mapper(componentModel = "spring")
public interface StadiumMapper extends EntityMapper<StadiumDTO, Stadium> {}
