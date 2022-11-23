package grafismo.service.mapper;

import grafismo.domain.Season;
import grafismo.service.dto.SeasonDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Season} and its DTO {@link SeasonDTO}.
 */
@Mapper(componentModel = "spring")
public interface SeasonMapper extends EntityMapper<SeasonDTO, Season> {}
