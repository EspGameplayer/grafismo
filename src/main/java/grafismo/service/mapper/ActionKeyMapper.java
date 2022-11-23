package grafismo.service.mapper;

import grafismo.domain.ActionKey;
import grafismo.service.dto.ActionKeyDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ActionKey} and its DTO {@link ActionKeyDTO}.
 */
@Mapper(componentModel = "spring")
public interface ActionKeyMapper extends EntityMapper<ActionKeyDTO, ActionKey> {}
