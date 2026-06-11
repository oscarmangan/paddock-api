package com.paddock.api.mapper;

import com.paddock.api.dto.marketplace.CategoryDto;
import com.paddock.api.model.marketplace.Category;

public class CategoryMapper {

    private CategoryMapper() {}

    public static CategoryDto toDto(Category category) {
        return new CategoryDto(
                category.getId(),
                category.getDiscipline(),
                category.getName(),
                category.getLabel(),
                category.getSlug(),
                category.getParentId(),
                category.getDisplayOrder(),
                category.getChildren().stream()
                        .map(CategoryMapper::toDto)
                        .toList()
        );
    }
}