package grafismo.service.mapper;

import grafismo.domain.Competition;
import grafismo.domain.Sponsor;
import grafismo.domain.Team;
import grafismo.service.dto.CompetitionDTO;
import grafismo.service.dto.SponsorDTO;
import grafismo.service.dto.TeamDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Competition} and its DTO {@link CompetitionDTO}.
 */
@Mapper(componentModel = "spring")
public interface CompetitionMapper extends EntityMapper<CompetitionDTO, Competition> {
    @Mapping(target = "sponsor", source = "sponsor", qualifiedByName = "sponsorName")
    @Mapping(target = "motmSponsor", source = "motmSponsor", qualifiedByName = "sponsorName")
    @Mapping(target = "parent", source = "parent", qualifiedByName = "competitionGraphicsName")
    @Mapping(target = "teams", source = "teams", qualifiedByName = "teamGraphicsNameSet")
    CompetitionDTO toDto(Competition s);

    @Mapping(target = "removeTeam", ignore = true)
    Competition toEntity(CompetitionDTO competitionDTO);

    @Named("sponsorName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    SponsorDTO toDtoSponsorName(Sponsor sponsor);

    @Named("competitionGraphicsName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "graphicsName", source = "graphicsName")
    CompetitionDTO toDtoCompetitionGraphicsName(Competition competition);

    @Named("teamGraphicsName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "graphicsName", source = "graphicsName")
    TeamDTO toDtoTeamGraphicsName(Team team);

    @Named("teamGraphicsNameSet")
    default Set<TeamDTO> toDtoTeamGraphicsNameSet(Set<Team> team) {
        return team.stream().map(this::toDtoTeamGraphicsName).collect(Collectors.toSet());
    }
}
