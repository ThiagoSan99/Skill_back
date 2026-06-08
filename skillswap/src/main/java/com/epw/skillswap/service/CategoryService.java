package com.epw.skillswap.service;

import com.epw.skillswap.dto.CategoryDTO;

import java.util.List;
import java.util.UUID;

public interface CategoryService {

    List<CategoryDTO> getAll();

    CategoryDTO getById(UUID id);
}
