package grafismo.service.mapper;

import grafismo.domain.Person;
import grafismo.domain.Team;
import grafismo.domain.TeamStaffMember;
import grafismo.service.dto.PersonDTO;
import grafismo.service.dto.TeamDTO;
import grafismo.service.dto.TeamStaffMemberDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TeamStaffMember} and its DTO {@link TeamStaffMemberDTO}.
 */
@Mapper(componentModel = "spring")
public interface TeamStaffMemberMapper extends EntityMapper<TeamStaffMemberDTO, TeamStaffMember> {
    @Mapping(target = "person", source = "person", qualifiedByName = "personGraphicsName")
    @Mapping(target = "team", source = "team", qualifiedByName = "teamGraphicsName")
    TeamStaffMemberDTO toDto(TeamStaffMember s);

    @Named("personGraphicsName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "graphicsName", source = "graphicsName")
    PersonDTO toDtoPersonGraphicsName(Person person);

    @Named("teamGraphicsName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "graphicsName", source = "graphicsName")
    TeamDTO toDtoTeamGraphicsName(Team team);
}
