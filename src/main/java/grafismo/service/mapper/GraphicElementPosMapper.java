package grafismo.service.mapper;

import grafismo.domain.GraphicElementPos;
import grafismo.service.dto.GraphicElementPosDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link GraphicElementPos} and its DTO {@link GraphicElementPosDTO}.
 */
@Mapper(componentModel = "spring")
public interface GraphicElementPosMapper extends EntityMapper<GraphicElementPosDTO, GraphicElementPos> {
    @Mapping(target = "parent", source = "parent", qualifiedByName = "graphicElementPosName")
    GraphicElementPosDTO toDto(GraphicElementPos s);

    @Named("graphicElementPosName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    GraphicElementPosDTO toDtoGraphicElementPosName(GraphicElementPos graphicElementPos);
}
