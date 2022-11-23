package grafismo.service.mapper;

import grafismo.domain.Action;
import grafismo.domain.MatchStats;
import grafismo.domain.Player;
import grafismo.service.dto.ActionDTO;
import grafismo.service.dto.MatchStatsDTO;
import grafismo.service.dto.PlayerDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Action} and its DTO {@link ActionDTO}.
 */
@Mapper(componentModel = "spring")
public interface ActionMapper extends EntityMapper<ActionDTO, Action> {
    @Mapping(target = "matchStats", source = "matchStats", qualifiedByName = "matchStatsId")
    @Mapping(target = "players", source = "players", qualifiedByName = "playerIdSet")
    ActionDTO toDto(Action s);

    @Mapping(target = "removePlayer", ignore = true)
    Action toEntity(ActionDTO actionDTO);

    @Named("matchStatsId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MatchStatsDTO toDtoMatchStatsId(MatchStats matchStats);

    @Named("playerId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PlayerDTO toDtoPlayerId(Player player);

    @Named("playerIdSet")
    default Set<PlayerDTO> toDtoPlayerIdSet(Set<Player> player) {
        return player.stream().map(this::toDtoPlayerId).collect(Collectors.toSet());
    }
}
