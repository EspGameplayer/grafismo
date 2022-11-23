package grafismo.service.mapper;

import grafismo.domain.Season;
import grafismo.domain.Shirt;
import grafismo.domain.Team;
import grafismo.service.dto.SeasonDTO;
import grafismo.service.dto.ShirtDTO;
import grafismo.service.dto.TeamDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Shirt} and its DTO {@link ShirtDTO}.
 */
@Mapper(componentModel = "spring")
public interface ShirtMapper extends EntityMapper<ShirtDTO, Shirt> {
    @Mapping(target = "team", source = "team", qualifiedByName = "teamGraphicsName")
    @Mapping(target = "season", source = "season", qualifiedByName = "seasonName")
    ShirtDTO toDto(Shirt s);

    @Named("teamGraphicsName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "graphicsName", source = "graphicsName")
    TeamDTO toDtoTeamGraphicsName(Team team);

    @Named("seasonName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    SeasonDTO toDtoSeasonName(Season season);
}
