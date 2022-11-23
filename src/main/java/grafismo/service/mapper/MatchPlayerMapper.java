package grafismo.service.mapper;

import grafismo.domain.MatchPlayer;
import grafismo.domain.Player;
import grafismo.domain.Position;
import grafismo.service.dto.MatchPlayerDTO;
import grafismo.service.dto.PlayerDTO;
import grafismo.service.dto.PositionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MatchPlayer} and its DTO {@link MatchPlayerDTO}.
 */
@Mapper(componentModel = "spring")
public interface MatchPlayerMapper extends EntityMapper<MatchPlayerDTO, MatchPlayer> {
    @Mapping(target = "player", source = "player", qualifiedByName = "playerId")
    @Mapping(target = "position", source = "position", qualifiedByName = "positionAbb")
    MatchPlayerDTO toDto(MatchPlayer s);

    @Named("playerId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PlayerDTO toDtoPlayerId(Player player);

    @Named("positionAbb")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "abb", source = "abb")
    PositionDTO toDtoPositionAbb(Position position);
}
