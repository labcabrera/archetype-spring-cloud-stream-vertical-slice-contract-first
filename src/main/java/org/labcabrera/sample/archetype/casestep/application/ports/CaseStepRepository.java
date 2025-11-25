package org.labcabrera.sample.archetype.casestep.application.ports;

import java.util.List;
import java.util.Optional;

import org.labcabrera.sample.archetype.casestep.domain.CaseStep;

public interface CaseStepRepository {

    Optional<CaseStep> findById(String caseStep);

    List<CaseStep> findByCaseFolderId(String caseFolderId);

    void deleteByCaseFolderId(String caseFolderId);

    CaseStep save(CaseStep entity);

}