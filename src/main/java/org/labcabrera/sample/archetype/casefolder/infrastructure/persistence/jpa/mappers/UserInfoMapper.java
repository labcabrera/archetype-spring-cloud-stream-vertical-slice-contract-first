package org.labcabrera.sample.archetype.casefolder.infrastructure.persistence.jpa.mappers;

import java.util.Optional;

import org.labcabrera.sample.archetype.casefolder.domain.UserInfo;
import org.labcabrera.sample.archetype.casefolder.infrastructure.persistence.jpa.entities.UserInfoEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { IdCardMapper.class })
public interface UserInfoMapper {

    UserInfo toDomain(UserInfoEntity entity);

    UserInfoEntity toEntity(UserInfo domain);

    default String map(Optional<String> value) {
        return value != null && value.isPresent() ? value.get() : null;
    }

    default Optional<String> map(String value) {
        return Optional.ofNullable(value);
    }

}