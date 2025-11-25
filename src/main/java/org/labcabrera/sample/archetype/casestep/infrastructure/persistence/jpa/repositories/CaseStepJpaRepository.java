package org.labcabrera.sample.archetype.casestep.infrastructure.persistence.jpa.repositories;

import java.util.List;

import org.labcabrera.sample.archetype.casestep.infrastructure.persistence.jpa.entities.CaseStepEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CaseStepJpaRepository extends JpaRepository<CaseStepEntity, String> {

    List<CaseStepEntity> findByCaseFolderId(String caseFolderId);

    @Query("DELETE FROM CaseStepEntity c WHERE c.caseFolderId = :caseFolderId")
    void deleteByCaseFolderId(String caseFolderId);

}
