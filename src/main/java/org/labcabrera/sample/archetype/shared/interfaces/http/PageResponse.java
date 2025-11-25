package org.labcabrera.sample.archetype.shared.interfaces.http;

import java.util.List;

import org.springframework.data.domain.Page;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import lombok.Data;

@Data
@Schema(description = "Paginated response")
public class PageResponse<T> {

    @Schema(description = "List of items in the current page", requiredMode = RequiredMode.REQUIRED)
    List<T> content;

    @Schema(description = "Pagination details", requiredMode = RequiredMode.REQUIRED)
    Pagination pagination;

    public PageResponse(Page<T> page) {
        this.content = page.getContent();
        this.pagination = new Pagination(page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages());
    }
}
