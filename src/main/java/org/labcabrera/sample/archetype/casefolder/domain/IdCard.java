package org.labcabrera.sample.archetype.casefolder.domain;

import org.apache.commons.lang3.StringUtils;
import org.labcabrera.sample.archetype.shared.domain.exceptions.BadRequestException;

public record IdCard(

    String idCardNumber,

    IdCardType idCardType

) {
    public IdCard {
        if (StringUtils.isBlank(idCardNumber)) {
            throw new BadRequestException("user-info.msg.err.required.id-card-number");
        }
        else if (idCardType == null) {
            throw new BadRequestException("user-info.msg.err.required.id-card-type");
        }
        idCardNumber = idCardNumber.toUpperCase();
    }
}
