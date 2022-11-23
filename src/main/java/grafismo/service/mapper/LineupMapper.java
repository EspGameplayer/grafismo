package grafismo.service.mapper;

import grafismo.domain.Callup;
import grafismo.domain.Formation;
import grafismo.domain.Lineup;
import grafismo.domain.MatchPlayer;
import grafismo.service.dto.CallupDTO;
import grafismo.service.dto.FormationDTO;
import grafismo.service.dto.LineupDTO;
import grafismo.service.dto.MatchPlayerDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Lineup} and its DTO {@link LineupDTO}.
 */
@Mapper(componentModel = "spring")
public interface LineupMapper extends EntityMapper<LineupDTO, Lineup> {
    @Mapping(target = "callup", source = "callup", qualifiedByName = "callupId")
    @Mapping(target = "formation", source = "formation", qualifiedByName = "formationName")
    @Mapping(target = "players", source = "players", qualifiedByName = "matchPlayerIdSet")
    LineupDTO toDto(Lineup s);

    @Mapping(target = "removePlayer", ignore = true)
    Lineup toEntity(LineupDTO lineupDTO);

    @Named("callupId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CallupDTO toDtoCallupId(Callup callup);

    @Named("formationName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    FormationDTO toDtoFormationName(Formation formation);

    @Named("matchPlayerId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MatchPlayerDTO toDtoMatchPlayerId(MatchPlayer matchPlayer);

    @Named("matchPlayerIdSet")
    default Set<MatchPlayerDTO> toDtoMatchPlayerIdSet(Set<MatchPlayer> matchPlayer) {
        return matchPlayer.stream().map(this::toDtoMatchPlayerId).collect(Collectors.toSet());
    }
}
