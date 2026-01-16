package org.labcabrera.sample.archetype.casefolder.infrastructure.persistence.jpa.repositories;

import java.util.Optional;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

import org.apache.commons.lang3.StringUtils;
import org.labcabrera.sample.archetype.casefolder.application.ports.CaseFolderRepository;
import org.labcabrera.sample.archetype.casefolder.application.services.CaseFolderGuard;
import org.labcabrera.sample.archetype.casefolder.domain.CaseFolder;
import org.labcabrera.sample.archetype.casefolder.domain.CaseFolderStatus;
import org.labcabrera.sample.archetype.casefolder.infrastructure.persistence.jpa.entities.CaseFolderEntity;
import org.labcabrera.sample.archetype.shared.application.SecurityPort.AuthenticatedUser;
import org.labcabrera.sample.archetype.shared.domain.exceptions.BadRequestException;
import org.labcabrera.sample.archetype.shared.domain.exceptions.NotModifiedException;
import org.labcabrera.sample.archetype.shared.infrastructure.persistence.rsql.CustomRsqlVisitor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.labcabrera.sample.archetype.casefolder.infrastructure.persistence.jpa.mappers.CaseFolderEntityMapper;

import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CaseFolderRepositoryJpaAdapter implements CaseFolderRepository {

    private final CaseFolderJpaRepository jpaRepository;
    private final CaseFolderEntityMapper mapper;
    private final RSQLParser rsqlParser;

    @Override
    @Cacheable(value = "caseFolder", key = "#caseFolderId", unless = "#result == null || #result.isEmpty()")
    public Optional<CaseFolder> findById(String caseFolderId) {
        return jpaRepository.findById(caseFolderId).map(mapper::toDomain);
    }

    @Override
    public Page<CaseFolder> findByRsql(String rsql, Pageable pageable, AuthenticatedUser user) {
        Specification<CaseFolderEntity> authSpec = (root, query, cb) -> {
            if (!user.hasRole(CaseFolderGuard.ROLE_CASE_FOLDER_MANAGEMENT)) {
                return cb.equal(root.get("owner"), user.username());
            }
            return cb.conjunction();
        };
        if (StringUtils.isBlank(rsql)) {
            var page = jpaRepository.findAll(authSpec, pageable);
            return page.map(mapper::toDomain);
        }
        try {
            Node rootNode = rsqlParser.parse(rsql);
            Specification<CaseFolderEntity> spec = rootNode.accept(new CustomRsqlVisitor<CaseFolderEntity>());
            Specification<CaseFolderEntity> finalSpec = (spec == null) ? authSpec : spec.and(authSpec);
            var page = jpaRepository.findAll(finalSpec, pageable);
            return page.map(mapper::toDomain);
        }
        catch (Exception ex) {
            throw new BadRequestException("rsql.msg.err.parse", ex, rsql);
        }
    }

    @Override
    @Transactional
    @CachePut(value = "caseFolder", key = "#result.id")
    public CaseFolder save(CaseFolder caseFolder) {
        try {
            if (caseFolder.getId() != null && jpaRepository.existsById(caseFolder.getId())) {
                throw new BadRequestException("case-folder.msg.err.already-exists", caseFolder.getId());
            }
            var entity = mapper.toEntity(caseFolder);
            var savedEntity = jpaRepository.save(entity);
            return mapper.toDomain(savedEntity);
        }
        catch (DataIntegrityViolationException ex) {
            throw new BadRequestException("case-folder.msg.err.data-integrity", ex);
        }
    }

    @Override
    @Transactional
    @CachePut(value = "caseFolder", key = "#caseFolderId")
    public CaseFolder update(String caseFolderId, CaseFolder caseFolder) {
        var current = jpaRepository.findById(caseFolderId)
            .orElseThrow(() -> new BadRequestException("Case folder not found with id " + caseFolderId));
        boolean modified = current.merge(caseFolder);
        if (!modified) {
            throw new NotModifiedException("case-folder.msg.err.not-modified", caseFolderId);
        }
        var savedEntity = jpaRepository.save(current);
        return mapper.toDomain(savedEntity);
    }

    @Override
    @CachePut(value = "caseFolder", key = "#caseFolderId")
    public CaseFolder updateStatus(String caseFolderId, CaseFolderStatus status) {
        jpaRepository.updateStatus(caseFolderId, status);
        var updatedEntity = jpaRepository.findById(caseFolderId)
            .orElseThrow(() -> new BadRequestException("Case folder not found with id " + caseFolderId));
        return mapper.toDomain(updatedEntity);
    }

    @Override
    @Transactional
    @CacheEvict(value = "caseFolder", key = "#caseFolderId")
    public void deleteById(String caseFolderId) {
        jpaRepository.deleteById(caseFolderId);
    }

}
