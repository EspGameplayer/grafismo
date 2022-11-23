package grafismo.service.mapper;

import grafismo.domain.Person;
import grafismo.domain.Player;
import grafismo.domain.Position;
import grafismo.domain.Team;
import grafismo.service.dto.PersonDTO;
import grafismo.service.dto.PlayerDTO;
import grafismo.service.dto.PositionDTO;
import grafismo.service.dto.TeamDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Player} and its DTO {@link PlayerDTO}.
 */
@Mapper(componentModel = "spring")
public interface PlayerMapper extends EntityMapper<PlayerDTO, Player> {
    @Mapping(target = "person", source = "person", qualifiedByName = "personGraphicsName")
    @Mapping(target = "team", source = "team", qualifiedByName = "teamGraphicsName")
    @Mapping(target = "positions", source = "positions", qualifiedByName = "positionAbbSet")
    PlayerDTO toDto(Player s);

    @Mapping(target = "removePosition", ignore = true)
    Player toEntity(PlayerDTO playerDTO);

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

    @Named("positionAbb")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "abb", source = "abb")
    PositionDTO toDtoPositionAbb(Position position);

    @Named("positionAbbSet")
    default Set<PositionDTO> toDtoPositionAbbSet(Set<Position> position) {
        return position.stream().map(this::toDtoPositionAbb).collect(Collectors.toSet());
    }
}
