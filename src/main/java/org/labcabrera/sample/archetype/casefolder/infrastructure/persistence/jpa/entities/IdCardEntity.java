package org.labcabrera.sample.archetype.casefolder.infrastructure.persistence.jpa.entities;

import org.labcabrera.sample.archetype.casefolder.domain.IdCardType;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IdCardEntity {

    @Column(name = "id_card_type", nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private IdCardType idCardType;

    @Column(name = "id_card_number", nullable = false, length = 50)
    private String idCardNumber;

}
