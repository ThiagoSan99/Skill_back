package com.epw.skillswap.service;


import com.epw.skillswap.dto.SkillDTO;

import java.util.List;
import java.util.UUID;

public interface SkillService {

    SkillDTO create(SkillDTO dto);

    List<SkillDTO> getAll();

    SkillDTO getById(UUID id);

    SkillDTO update(UUID id, SkillDTO dto);

    void delete(UUID id);
}