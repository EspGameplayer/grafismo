package grafismo.service.mapper;

import grafismo.domain.Association;
import grafismo.service.dto.AssociationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Association} and its DTO {@link AssociationDTO}.
 */
@Mapper(componentModel = "spring")
public interface AssociationMapper extends EntityMapper<AssociationDTO, Association> {}
