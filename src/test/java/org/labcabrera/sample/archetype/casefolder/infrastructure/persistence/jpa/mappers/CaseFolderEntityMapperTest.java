package org.labcabrera.sample.archetype.casefolder.infrastructure.persistence.jpa.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import org.mapstruct.factory.Mappers;

import org.labcabrera.sample.archetype.casefolder.domain.CaseFolder;
import org.labcabrera.sample.archetype.casefolder.domain.IdCard;
import org.labcabrera.sample.archetype.casefolder.domain.IdCardType;
import org.labcabrera.sample.archetype.casefolder.domain.UserInfo;
import org.labcabrera.sample.archetype.casefolder.infrastructure.persistence.jpa.entities.CaseFolderEntity;
import org.labcabrera.sample.archetype.casefolder.infrastructure.persistence.jpa.entities.IdCardEntity;
import org.labcabrera.sample.archetype.casefolder.infrastructure.persistence.jpa.entities.UserInfoEntity;

class CaseFolderEntityMapperTest {

    private final CaseFolderEntityMapper mapper = Mappers.getMapper(CaseFolderEntityMapper.class);

    @BeforeEach
    void setup() {
        try {
            UserInfoEntityMapper userInfoMapper = Mappers.getMapper(UserInfoEntityMapper.class);
            try {
                Field idCardField = userInfoMapper.getClass().getDeclaredField("idCardEntityMapper");
                idCardField.setAccessible(true);
                idCardField.set(userInfoMapper, Mappers.getMapper(IdCardEntityMapper.class));
            }
            catch (NoSuchFieldException ignore) {
                // Ignore exception
            }
            Field f = mapper.getClass().getDeclaredField("userInfoEntityMapper");
            f.setAccessible(true);
            f.set(mapper, userInfoMapper);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testToDomain() {
        UserInfoEntity ue = new UserInfoEntity();
        ue.setId("user-1");
        ue.setName("John");
        ue.setFirstSurname("Doe");
        ue.setLastSurname("Smith");
        ue.setIdCard(new IdCardEntity(IdCardType.NIF, "12345678A"));

        CaseFolderEntity cfe = new CaseFolderEntity();
        cfe.setId("cf-1");
        cfe.setUserInfo(ue);
        cfe.setOwner("owner1");
        cfe.setStatus(org.labcabrera.sample.archetype.casefolder.domain.CaseFolderStatus.PARTIALLY_CREATED);
        cfe.setCreatedAt(LocalDateTime.now());

        CaseFolder domain = mapper.toDomain(cfe);

        assertEquals(cfe.getId(), domain.getId());
        assertEquals("John", domain.getUserInfo().getName());
        assertEquals("Doe", domain.getUserInfo().getFirstSurname());
        assertTrue(domain.getUserInfo().getLastSurname().isPresent());
        assertEquals("Smith", domain.getUserInfo().getLastSurname().orElse(null));
    }

    @Test
    void testToEntity() {
        UserInfo userInfo = UserInfo.builder()
            .id(null)
            .name("Alice")
            .firstSurname("Wonder")
            .lastSurname(Optional.of("Land"))
            .idCard(new IdCard("87654321B", IdCardType.NIF))
            .build()
            .normalize();

        CaseFolder domain = CaseFolder.create(userInfo, "owner2");

        CaseFolderEntity entity = mapper.toEntity(domain);

        assertEquals(domain.getId(), entity.getId());
        assertEquals(domain.getUserInfo().getName(), entity.getUserInfo().getName());
        assertNotNull(entity.getUserInfo().getLastSurname());
        assertEquals(domain.getUserInfo().getLastSurname().orElse(null), entity.getUserInfo().getLastSurname());
    }
}
