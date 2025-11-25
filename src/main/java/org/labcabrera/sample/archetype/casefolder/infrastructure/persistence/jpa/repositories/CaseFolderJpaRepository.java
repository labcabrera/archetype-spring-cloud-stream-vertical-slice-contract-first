package org.labcabrera.sample.archetype.casefolder.infrastructure.persistence.jpa.repositories;

import org.labcabrera.sample.archetype.casefolder.domain.CaseFolderStatus;
import org.labcabrera.sample.archetype.casefolder.infrastructure.persistence.jpa.entities.CaseFolderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Repository;

@Repository
public interface CaseFolderJpaRepository extends
    JpaRepository<CaseFolderEntity, String>,
    JpaSpecificationExecutor<CaseFolderEntity> {

    @Modifying
    @Transactional
    @Query("UPDATE CaseFolderEntity c SET c.status = ?2 WHERE c.id = ?1")
    void updateStatus(String caseFolderId, CaseFolderStatus status);

}