package org.labcabrera.sample.archetype.casefolder.interfaces.http.mappers;

import java.util.Optional;

import org.labcabrera.sample.archetype.casefolder.domain.UserInfo;
import org.mapstruct.Mapper;

import com.labcabrera.sample.archetype.generated.model.UserInfoDto;

@Mapper(componentModel = "spring", uses = { IdCardDtoMapper.class })
public interface UserInfoDtoMapper {

    UserInfoDto toDto(UserInfo domain);

    default String map(Optional<String> value) {
        return value.isPresent() ? value.get() : null;
    }

}
