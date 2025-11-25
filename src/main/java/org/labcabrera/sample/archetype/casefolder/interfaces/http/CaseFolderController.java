package org.labcabrera.sample.archetype.casefolder.interfaces.http;

import java.net.URI;
import java.util.List;

import org.labcabrera.sample.archetype.casefolder.application.cqrs.commands.CreateCaseFolderCommand;
import org.labcabrera.sample.archetype.casefolder.application.cqrs.commands.DeleteCaseFolderCommand;
import org.labcabrera.sample.archetype.casefolder.application.cqrs.commands.UpdateCaseFolderCommand;
import org.labcabrera.sample.archetype.casefolder.application.cqrs.queries.GetCaseFolderByIdQuery;
import org.labcabrera.sample.archetype.casefolder.application.cqrs.queries.GetCaseFoldersByRsqlQuery;
import org.labcabrera.sample.archetype.casefolder.domain.CaseFolder;
import org.labcabrera.sample.archetype.casefolder.domain.IdCardType;
import org.labcabrera.sample.archetype.shared.application.CommandBus;
import org.labcabrera.sample.archetype.shared.application.QueryBus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import com.labcabrera.sample.archetype.generated.api.CaseFoldersApi;
import com.labcabrera.sample.archetype.generated.model.CaseFolderDto;
import com.labcabrera.sample.archetype.generated.model.CaseFolderPageResponse;
import com.labcabrera.sample.archetype.generated.model.CreateCaseFolderRequest;
import com.labcabrera.sample.archetype.generated.model.Pagination;
import com.labcabrera.sample.archetype.generated.model.UpdateCaseFolderRequest;

import org.labcabrera.sample.archetype.casefolder.interfaces.http.mappers.CaseFolderDtoMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@SuppressWarnings("null")
public class CaseFolderController implements CaseFoldersApi {

    private final CommandBus commandBus;
    private final QueryBus queryBus;
    private final CaseFolderDtoMapper mapper;

    @Override
    public ResponseEntity<CaseFolderDto> getCaseFolderById(String caseFolderId) {
        var query = new GetCaseFolderByIdQuery(caseFolderId);
        CaseFolder caseFolder = queryBus.dispatch(query);
        var caseFolderDto = mapper.toDto(caseFolder);
        return ResponseEntity.ok(caseFolderDto);
    }

    @Override
    public ResponseEntity<CaseFolderDto> create(@Validated CreateCaseFolderRequest request) {
        var command = new CreateCaseFolderCommand(
            request.getName(),
            request.getFirstSurname(),
            null,
            IdCardType.valueOf(request.getIdCard().getType().getValue()),
            request.getIdCard().getNumber());
        CaseFolder caseFolder = commandBus.dispatch(command);
        var caseFolderDto = mapper.toDto(caseFolder);
        return ResponseEntity.created(URI.create("/api/v1/case-folders/" + caseFolder.getId())).body(caseFolderDto);
    }

    @Override
    public ResponseEntity<CaseFolderPageResponse> getCaseFoldersByRsql(String rsql, Integer page, Integer size, List<String> sort) {
        Pageable pageable = Pageable.ofSize(size != null ? size : 20).withPage(page != null ? page : 0);
        var query = new GetCaseFoldersByRsqlQuery(rsql, pageable);
        Page<CaseFolder> resultPage = queryBus.dispatch(query);
        var pageDto = resultPage.map(mapper::toDto);
        var response = new CaseFolderPageResponse(
            pageDto.getContent(),
            new Pagination(
                pageDto.getNumber(),
                pageDto.getSize(),
                pageDto.getTotalElements(),
                pageDto.getTotalPages()));
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<CaseFolderDto> update(String caseFolderId, UpdateCaseFolderRequest request) {
        var command = new UpdateCaseFolderCommand(
            caseFolderId,
            request.getName(),
            request.getFirstSurname(),
            request.getLastSurname(),
            null);
        CaseFolder caseFolder = commandBus.dispatch(command);
        var caseFolderDto = mapper.toDto(caseFolder);
        return ResponseEntity.ok(caseFolderDto);
    }

    @Override
    public ResponseEntity<Void> delete(String caseFolderId) {
        var command = new DeleteCaseFolderCommand(caseFolderId);
        commandBus.dispatch(command);
        return ResponseEntity.noContent().build();
    }

}
