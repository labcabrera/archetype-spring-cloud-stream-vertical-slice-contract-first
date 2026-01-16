package org.labcabrera.sample.archetype.casestep.domain;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Domain model representing a case step.
 * 
 * A CaseStep represents a procedure associated with a case file (CaseFolder).
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CaseStep {

    private String id;

    private String caseFolderId;

    private StepType stepType;

    private StepStatus status;

    private String assignedTo;

    private String owner;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
