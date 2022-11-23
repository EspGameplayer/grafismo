package grafismo.service.mapper;

import grafismo.domain.Match;
import grafismo.domain.MatchStats;
import grafismo.service.dto.MatchDTO;
import grafismo.service.dto.MatchStatsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MatchStats} and its DTO {@link MatchStatsDTO}.
 */
@Mapper(componentModel = "spring")
public interface MatchStatsMapper extends EntityMapper<MatchStatsDTO, MatchStats> {
    @Mapping(target = "match", source = "match", qualifiedByName = "matchId")
    MatchStatsDTO toDto(MatchStats s);

    @Named("matchId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MatchDTO toDtoMatchId(Match match);
}
