package org.labcabrera.sample.archetype.casefolder.infrastructure.persistence.jpa.mappers;

import java.util.Optional;

import org.labcabrera.sample.archetype.casefolder.domain.UserInfo;
import org.labcabrera.sample.archetype.casefolder.infrastructure.persistence.jpa.entities.UserInfoEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { IdCardEntityMapper.class })
public interface UserInfoEntityMapper {

    UserInfo toDomain(UserInfoEntity entity);

    UserInfoEntity toEntity(UserInfo domain);

    /**
     * NOTA: en el modelo utilizamos Optional para lastSurname mientras que en las
     * entidades no se recomienda el uso de Optional.
     * @param value
     * @return
     */
    default String map(Optional<String> value) {
        return value.isPresent() ? value.get() : null;
    }

    default Optional<String> map(String value) {
        return Optional.ofNullable(value);
    }

}