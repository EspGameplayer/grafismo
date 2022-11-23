package grafismo.service.mapper;

import grafismo.domain.Formation;
import grafismo.service.dto.FormationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Formation} and its DTO {@link FormationDTO}.
 */
@Mapper(componentModel = "spring")
public interface FormationMapper extends EntityMapper<FormationDTO, Formation> {}
