package grafismo.service.mapper;

import grafismo.domain.Formation;
import grafismo.domain.Stadium;
import grafismo.domain.Team;
import grafismo.service.dto.FormationDTO;
import grafismo.service.dto.StadiumDTO;
import grafismo.service.dto.TeamDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Team} and its DTO {@link TeamDTO}.
 */
@Mapper(componentModel = "spring")
public interface TeamMapper extends EntityMapper<TeamDTO, Team> {
    @Mapping(target = "preferredFormation", source = "preferredFormation", qualifiedByName = "formationName")
    @Mapping(target = "stadiums", source = "stadiums", qualifiedByName = "stadiumGraphicsNameSet")
    TeamDTO toDto(Team s);

    @Mapping(target = "removeStadium", ignore = true)
    Team toEntity(TeamDTO teamDTO);

    @Named("formationName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    FormationDTO toDtoFormationName(Formation formation);

    @Named("stadiumGraphicsName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "graphicsName", source = "graphicsName")
    StadiumDTO toDtoStadiumGraphicsName(Stadium stadium);

    @Named("stadiumGraphicsNameSet")
    default Set<StadiumDTO> toDtoStadiumGraphicsNameSet(Set<Stadium> stadium) {
        return stadium.stream().map(this::toDtoStadiumGraphicsName).collect(Collectors.toSet());
    }
}
