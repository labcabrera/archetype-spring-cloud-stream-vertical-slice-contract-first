package org.labcabrera.sample.archetype.casestep.interfaces.http;

import java.util.List;

import org.labcabrera.sample.archetype.casestep.application.cqrs.queries.GetCaseStepByIdQuery;
import org.labcabrera.sample.archetype.casestep.application.cqrs.queries.GetCaseStepsByCaseFolderIdQuery;
import org.labcabrera.sample.archetype.casestep.domain.CaseStep;
import org.labcabrera.sample.archetype.casestep.interfaces.http.mappers.CaseStepDtoMapper;
import org.labcabrera.sample.archetype.shared.application.QueryBus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.labcabrera.sample.archetype.generated.api.CaseStepsApi;
import com.labcabrera.sample.archetype.generated.model.CaseStepDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CaseStepController implements CaseStepsApi {

    private final QueryBus queryBus;
    private final CaseStepDtoMapper mapper;

    @Override
    public ResponseEntity<CaseStepDto> getCaseStepById(String caseStepId) {
        var query = new GetCaseStepByIdQuery(caseStepId);
        CaseStep caseStep = queryBus.dispatch(query);
        var dto = mapper.toDto(caseStep);
        return ResponseEntity.ok(dto);
    }

    @Override
    public ResponseEntity<List<CaseStepDto>> getCaseStepsByCaseFolderId(String caseFolderId) {
        var query = new GetCaseStepsByCaseFolderIdQuery(caseFolderId);
        List<CaseStep> caseSteps = queryBus.dispatch(query);
        var dtos = caseSteps.stream().map(mapper::toDto).toList();
        return ResponseEntity.ok(dtos);
    }

}
