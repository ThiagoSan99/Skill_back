package com.epw.skillswap.controller;

import com.epw.skillswap.dto.CategoryDTO;
import com.epw.skillswap.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService service;

    @GetMapping
    public List<CategoryDTO> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public CategoryDTO getById(@PathVariable UUID id) {
        return service.getById(id);
    }
}
