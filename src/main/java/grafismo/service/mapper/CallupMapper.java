package grafismo.service.mapper;

import grafismo.domain.Callup;
import grafismo.domain.MatchPlayer;
import grafismo.domain.TeamStaffMember;
import grafismo.service.dto.CallupDTO;
import grafismo.service.dto.MatchPlayerDTO;
import grafismo.service.dto.TeamStaffMemberDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Callup} and its DTO {@link CallupDTO}.
 */
@Mapper(componentModel = "spring")
public interface CallupMapper extends EntityMapper<CallupDTO, Callup> {
    @Mapping(target = "captain", source = "captain", qualifiedByName = "matchPlayerId")
    @Mapping(target = "dt", source = "dt", qualifiedByName = "teamStaffMemberId")
    @Mapping(target = "dt2", source = "dt2", qualifiedByName = "teamStaffMemberId")
    @Mapping(target = "teamDelegate", source = "teamDelegate", qualifiedByName = "teamStaffMemberId")
    @Mapping(target = "players", source = "players", qualifiedByName = "matchPlayerIdSet")
    CallupDTO toDto(Callup s);

    @Mapping(target = "removePlayer", ignore = true)
    Callup toEntity(CallupDTO callupDTO);

    @Named("matchPlayerId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MatchPlayerDTO toDtoMatchPlayerId(MatchPlayer matchPlayer);

    @Named("matchPlayerIdSet")
    default Set<MatchPlayerDTO> toDtoMatchPlayerIdSet(Set<MatchPlayer> matchPlayer) {
        return matchPlayer.stream().map(this::toDtoMatchPlayerId).collect(Collectors.toSet());
    }

    @Named("teamStaffMemberId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TeamStaffMemberDTO toDtoTeamStaffMemberId(TeamStaffMember teamStaffMember);
}
