package grafismo.service.mapper;

import grafismo.domain.Competition;
import grafismo.domain.Person;
import grafismo.domain.Suspension;
import grafismo.service.dto.CompetitionDTO;
import grafismo.service.dto.PersonDTO;
import grafismo.service.dto.SuspensionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Suspension} and its DTO {@link SuspensionDTO}.
 */
@Mapper(componentModel = "spring")
public interface SuspensionMapper extends EntityMapper<SuspensionDTO, Suspension> {
    @Mapping(target = "person", source = "person", qualifiedByName = "personGraphicsName")
    @Mapping(target = "competition", source = "competition", qualifiedByName = "competitionGraphicsName")
    SuspensionDTO toDto(Suspension s);

    @Named("personGraphicsName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "graphicsName", source = "graphicsName")
    PersonDTO toDtoPersonGraphicsName(Person person);

    @Named("competitionGraphicsName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "graphicsName", source = "graphicsName")
    CompetitionDTO toDtoCompetitionGraphicsName(Competition competition);
}
