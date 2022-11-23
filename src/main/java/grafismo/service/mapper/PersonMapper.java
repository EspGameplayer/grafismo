package grafismo.service.mapper;

import grafismo.domain.Country;
import grafismo.domain.Person;
import grafismo.service.dto.CountryDTO;
import grafismo.service.dto.PersonDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Person} and its DTO {@link PersonDTO}.
 */
@Mapper(componentModel = "spring")
public interface PersonMapper extends EntityMapper<PersonDTO, Person> {
    @Mapping(target = "nationality", source = "nationality", qualifiedByName = "countryName")
    PersonDTO toDto(Person s);

    @Named("countryName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    CountryDTO toDtoCountryName(Country country);
}
