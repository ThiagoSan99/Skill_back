package com.epw.skillswap.service.impl;

import com.epw.skillswap.dto.CategoryDTO;
import com.epw.skillswap.entity.Category;
import com.epw.skillswap.exception.ResourceNotFoundException;
import com.epw.skillswap.repository.CategoryRepository;
import com.epw.skillswap.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository repository;

    @Override
    public List<CategoryDTO> getAll() {
        return repository.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public CategoryDTO getById(UUID id) {
        Category category = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        return mapToDTO(category);
    }

    private CategoryDTO mapToDTO(Category category) {
        return CategoryDTO.builder()
                .categoryId(category.getCategoryId())
                .name(category.getName())
                .build();
    }
}
