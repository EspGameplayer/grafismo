package grafismo.service.mapper;

import grafismo.domain.LocalAssociationProvince;
import grafismo.domain.LocalAssociationRegion;
import grafismo.service.dto.LocalAssociationProvinceDTO;
import grafismo.service.dto.LocalAssociationRegionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link LocalAssociationProvince} and its DTO {@link LocalAssociationProvinceDTO}.
 */
@Mapper(componentModel = "spring")
public interface LocalAssociationProvinceMapper extends EntityMapper<LocalAssociationProvinceDTO, LocalAssociationProvince> {
    @Mapping(target = "association", source = "association", qualifiedByName = "localAssociationRegionName")
    LocalAssociationProvinceDTO toDto(LocalAssociationProvince s);

    @Named("localAssociationRegionName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    LocalAssociationRegionDTO toDtoLocalAssociationRegionName(LocalAssociationRegion localAssociationRegion);
}
