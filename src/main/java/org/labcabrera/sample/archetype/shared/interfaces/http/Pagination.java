package org.labcabrera.sample.archetype.shared.interfaces.http;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;

@Schema(description = "Pagination information")
public record Pagination(

    @Schema(description = "Current page number (0-based)", requiredMode = RequiredMode.REQUIRED, example = "0") int page,

    @Schema(description = "Number of items per page", requiredMode = RequiredMode.REQUIRED, example = "20") int size,

    @Schema(description = "Total number of elements", requiredMode = RequiredMode.REQUIRED, example = "100") long totalElements,

    @Schema(description = "Total number of pages", requiredMode = RequiredMode.REQUIRED, example = "5") int totalPages

) {
}
