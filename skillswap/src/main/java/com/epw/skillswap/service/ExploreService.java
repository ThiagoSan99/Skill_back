package com.epw.skillswap.service;

import com.epw.skillswap.dto.ExploreItemDTO;

import java.util.List;
import java.util.UUID;

public interface ExploreService {

    List<ExploreItemDTO> getExplore(UUID categoryId, String level, String search, String sort, UUID excludeUserId);
}
