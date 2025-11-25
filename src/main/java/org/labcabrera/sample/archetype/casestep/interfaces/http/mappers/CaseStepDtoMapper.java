package org.labcabrera.sample.archetype.casestep.interfaces.http.mappers;

import org.labcabrera.sample.archetype.casestep.domain.CaseStep;
import org.mapstruct.Mapper;

import com.labcabrera.sample.archetype.generated.model.CaseStepDto;

@Mapper(componentModel = "spring")
public interface CaseStepDtoMapper {

    CaseStepDto toDto(CaseStep domain);

}
