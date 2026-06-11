package com.paddock.api.service;

import com.paddock.api.dto.marketplace.CategoryDto;
import com.paddock.api.mapper.CategoryMapper;
import com.paddock.api.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<CategoryDto> getCategoriesByDiscipline(String discipline) {
        return categoryRepository.findTopLevelWithChildrenByDiscipline(discipline)
                .stream()
                .map(CategoryMapper::toDto)
                .toList();
    }
}