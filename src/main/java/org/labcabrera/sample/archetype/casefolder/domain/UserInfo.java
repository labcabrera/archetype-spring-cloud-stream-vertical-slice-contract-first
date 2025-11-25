package org.labcabrera.sample.archetype.casefolder.domain;

import java.util.Optional;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfo {

    @NotNull
    private String id;

    @NotNull
    private String name;

    @NotNull
    private String firstSurname;

    private Optional<String> lastSurname;

    @NotNull
    private IdCard idCard;

    public UserInfo normalize() {
        name = name.toUpperCase();
        firstSurname = firstSurname.toUpperCase();
        lastSurname = lastSurname.map(String::toUpperCase);
        return this;
    }

    public boolean merge(UserInfo updated) {
        boolean modified = false;
        if (updated.getName() != null && !updated.getName().toUpperCase().equals(this.name)) {
            this.name = updated.getName().toUpperCase();
            modified = true;
        }
        if (updated.getFirstSurname() != null && !updated.getFirstSurname().toUpperCase().equals(this.firstSurname)) {
            this.firstSurname = updated.getFirstSurname().toUpperCase();
            modified = true;
        }
        if (updated.getLastSurname().isPresent()
            && (this.lastSurname == null || !updated.getLastSurname().get().toUpperCase().equals(this.lastSurname.orElse("")))) {
            this.lastSurname = Optional.of(updated.getLastSurname().get().toUpperCase());
            modified = true;
        }
        if (updated.getIdCard() != null && !updated.getIdCard().equals(this.idCard)) {
            this.idCard = updated.getIdCard();
            modified = true;
        }
        return modified;
    }

}
