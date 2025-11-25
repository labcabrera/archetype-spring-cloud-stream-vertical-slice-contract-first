package org.labcabrera.sample.archetype.casefolder.application.ports;

import org.labcabrera.sample.archetype.casefolder.domain.CaseFolder;
import org.labcabrera.sample.archetype.casefolder.domain.CaseFolderStatus;
import org.labcabrera.sample.archetype.shared.application.SecurityPort.AuthenticatedUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CaseFolderRepository {

    Optional<CaseFolder> findById(String caseFolder);

    Page<CaseFolder> findByRsql(String rsql, Pageable pageable, AuthenticatedUser user);

    CaseFolder save(CaseFolder entity);

    CaseFolder update(CaseFolder entity);

    CaseFolder updateStatus(String caseFolderId, CaseFolderStatus status);

    void deleteById(String caseFolderId);

}