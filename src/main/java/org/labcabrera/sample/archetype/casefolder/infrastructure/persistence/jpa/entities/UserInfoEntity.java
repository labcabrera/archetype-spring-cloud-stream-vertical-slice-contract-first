package org.labcabrera.sample.archetype.casefolder.infrastructure.persistence.jpa.entities;

import org.labcabrera.sample.archetype.casefolder.domain.UserInfo;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_info")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoEntity {

    @Id
    @Column(name = "id", length = 36)
    private String id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "first_surname", nullable = false, length = 100)
    private String firstSurname;

    @Column(name = "last_surname", nullable = true, length = 100)
    private String lastSurname;

    @Embedded
    private IdCardEntity idCard;

    public boolean merge(UserInfo userInfo) {
        boolean modified = false;
        if (userInfo.getName() != null && !userInfo.getName().equals(this.name)) {
            this.name = userInfo.getName();
            modified = true;
        }
        if (userInfo.getFirstSurname() != null && !userInfo.getFirstSurname().equals(this.firstSurname)) {
            this.firstSurname = userInfo.getFirstSurname();
            modified = true;
        }
        if (userInfo.getLastSurname().isPresent()) {
            this.lastSurname = userInfo.getLastSurname().orElse(null);
            modified = true;
        }
        return modified;
    }

}
