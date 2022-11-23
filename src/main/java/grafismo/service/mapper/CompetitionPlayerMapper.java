package grafismo.service.mapper;

import grafismo.domain.Competition;
import grafismo.domain.CompetitionPlayer;
import grafismo.domain.Player;
import grafismo.domain.Position;
import grafismo.service.dto.CompetitionDTO;
import grafismo.service.dto.CompetitionPlayerDTO;
import grafismo.service.dto.PlayerDTO;
import grafismo.service.dto.PositionDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CompetitionPlayer} and its DTO {@link CompetitionPlayerDTO}.
 */
@Mapper(componentModel = "spring")
public interface CompetitionPlayerMapper extends EntityMapper<CompetitionPlayerDTO, CompetitionPlayer> {
    @Mapping(target = "player", source = "player", qualifiedByName = "playerId")
    @Mapping(target = "competition", source = "competition", qualifiedByName = "competitionGraphicsName")
    @Mapping(target = "preferredPositions", source = "preferredPositions", qualifiedByName = "positionAbbSet")
    CompetitionPlayerDTO toDto(CompetitionPlayer s);

    @Mapping(target = "removePreferredPosition", ignore = true)
    CompetitionPlayer toEntity(CompetitionPlayerDTO competitionPlayerDTO);

    @Named("playerId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PlayerDTO toDtoPlayerId(Player player);

    @Named("competitionGraphicsName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "graphicsName", source = "graphicsName")
    CompetitionDTO toDtoCompetitionGraphicsName(Competition competition);

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
