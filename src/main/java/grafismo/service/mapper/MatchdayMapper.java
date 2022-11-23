package grafismo.service.mapper;

import grafismo.domain.Competition;
import grafismo.domain.Matchday;
import grafismo.service.dto.CompetitionDTO;
import grafismo.service.dto.MatchdayDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Matchday} and its DTO {@link MatchdayDTO}.
 */
@Mapper(componentModel = "spring")
public interface MatchdayMapper extends EntityMapper<MatchdayDTO, Matchday> {
    @Mapping(target = "competition", source = "competition", qualifiedByName = "competitionGraphicsName")
    MatchdayDTO toDto(Matchday s);

    @Named("competitionGraphicsName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "graphicsName", source = "graphicsName")
    CompetitionDTO toDtoCompetitionGraphicsName(Competition competition);
}
