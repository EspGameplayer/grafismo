package grafismo.service.mapper;

import grafismo.domain.Position;
import grafismo.service.dto.PositionDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Position} and its DTO {@link PositionDTO}.
 */
@Mapper(componentModel = "spring")
public interface PositionMapper extends EntityMapper<PositionDTO, Position> {
    @Mapping(target = "parents", source = "parents", qualifiedByName = "positionAbbSet")
    PositionDTO toDto(Position s);

    @Mapping(target = "removeParent", ignore = true)
    Position toEntity(PositionDTO positionDTO);

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
