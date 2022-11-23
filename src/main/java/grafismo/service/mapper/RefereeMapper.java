package grafismo.service.mapper;

import grafismo.domain.LocalAssociationProvince;
import grafismo.domain.Person;
import grafismo.domain.Referee;
import grafismo.service.dto.LocalAssociationProvinceDTO;
import grafismo.service.dto.PersonDTO;
import grafismo.service.dto.RefereeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Referee} and its DTO {@link RefereeDTO}.
 */
@Mapper(componentModel = "spring")
public interface RefereeMapper extends EntityMapper<RefereeDTO, Referee> {
    @Mapping(target = "person", source = "person", qualifiedByName = "personGraphicsName")
    @Mapping(target = "association", source = "association", qualifiedByName = "localAssociationProvinceName")
    RefereeDTO toDto(Referee s);

    @Named("personGraphicsName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "graphicsName", source = "graphicsName")
    PersonDTO toDtoPersonGraphicsName(Person person);

    @Named("localAssociationProvinceName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    LocalAssociationProvinceDTO toDtoLocalAssociationProvinceName(LocalAssociationProvince localAssociationProvince);
}
