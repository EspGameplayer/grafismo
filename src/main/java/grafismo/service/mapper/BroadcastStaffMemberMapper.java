package grafismo.service.mapper;

import grafismo.domain.BroadcastStaffMember;
import grafismo.domain.Person;
import grafismo.service.dto.BroadcastStaffMemberDTO;
import grafismo.service.dto.PersonDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link BroadcastStaffMember} and its DTO {@link BroadcastStaffMemberDTO}.
 */
@Mapper(componentModel = "spring")
public interface BroadcastStaffMemberMapper extends EntityMapper<BroadcastStaffMemberDTO, BroadcastStaffMember> {
    @Mapping(target = "person", source = "person", qualifiedByName = "personGraphicsName")
    BroadcastStaffMemberDTO toDto(BroadcastStaffMember s);

    @Named("personGraphicsName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "graphicsName", source = "graphicsName")
    PersonDTO toDtoPersonGraphicsName(Person person);
}
