package org.labcabrera.sample.archetype.casefolder.domain;

import java.util.Optional;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Basic user information associated with a case folder.
 */
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

    @Nullable
    private Optional<String> lastSurname;

    @NotNull
    private IdCard idCard;

    /**
     * Normalize user info fields (uppercase).
     * @return
     */
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
        if (updated.getLastSurname().isPresent()) {
            var value = updated.getLastSurname().map(String::toUpperCase).get();
            if (!value.equals(this.lastSurname.orElse(null))) {
                this.lastSurname = Optional.of(value);
                modified = true;
            }
        }
        if (updated.getIdCard() != null && !updated.getIdCard().equals(this.idCard)) {
            this.idCard = updated.getIdCard();
            modified = true;
        }
        return modified;
    }

}
