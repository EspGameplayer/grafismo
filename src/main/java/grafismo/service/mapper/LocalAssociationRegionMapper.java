package grafismo.service.mapper;

import grafismo.domain.Association;
import grafismo.domain.LocalAssociationRegion;
import grafismo.service.dto.AssociationDTO;
import grafismo.service.dto.LocalAssociationRegionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link LocalAssociationRegion} and its DTO {@link LocalAssociationRegionDTO}.
 */
@Mapper(componentModel = "spring")
public interface LocalAssociationRegionMapper extends EntityMapper<LocalAssociationRegionDTO, LocalAssociationRegion> {
    @Mapping(target = "association", source = "association", qualifiedByName = "associationName")
    LocalAssociationRegionDTO toDto(LocalAssociationRegion s);

    @Named("associationName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    AssociationDTO toDtoAssociationName(Association association);
}
