package org.labcabrera.sample.archetype.casefolder.infrastructure.persistence.jpa.mappers;

import org.labcabrera.sample.archetype.casefolder.domain.IdCard;
import org.labcabrera.sample.archetype.casefolder.infrastructure.persistence.jpa.entities.IdCardEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IdCardMapper {

    IdCard toDomain(IdCardEntity entity);

    IdCardEntity toEntity(IdCard domain);

}
