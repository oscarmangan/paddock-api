package com.paddock.api.web.controller;

import com.paddock.api.dto.marketplace.CategoryDto;
import com.paddock.api.service.CategoryService;
import com.paddock.api.web.exception.ApiError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Categories", description = "Discipline category tree")
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(
            summary = "Get category tree for a discipline",
            operationId = "getCategoriesByDiscipline"
    )
    @ApiResponse(responseCode = "200", description = "Category tree for discipline")
    @ApiResponse(
            responseCode = "400",
            description = "Invalid discipline",
            content = @Content(schema = @Schema(implementation = ApiError.class))
    )
    @GetMapping("/{discipline}")
    public ResponseEntity<List<CategoryDto>> getCategoriesByDiscipline(
            @Parameter(description = "Valid values: race, rally, drift, kart, transport, racewear")
            @PathVariable String discipline) {
        return ResponseEntity.ok(categoryService.getCategoriesByDiscipline(discipline));
    }
}