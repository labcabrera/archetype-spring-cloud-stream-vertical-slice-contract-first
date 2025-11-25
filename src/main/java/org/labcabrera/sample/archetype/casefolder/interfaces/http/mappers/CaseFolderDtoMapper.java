package org.labcabrera.sample.archetype.casefolder.interfaces.http.mappers;

import org.labcabrera.sample.archetype.casefolder.domain.CaseFolder;
import org.mapstruct.Mapper;

import com.labcabrera.sample.archetype.generated.model.CaseFolderDto;

@Mapper(componentModel = "spring", uses = { IdCardDtoMapper.class })
public interface CaseFolderDtoMapper {

    CaseFolderDto toDto(CaseFolder domain);

}
