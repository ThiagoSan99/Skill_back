package com.epw.skillswap.service;

import com.epw.skillswap.dto.UserSkillDTO;

import java.util.List;
import java.util.UUID;

public interface UserSkillService {

    List<UserSkillDTO> getUserSkills(UUID userId);

    UserSkillDTO addUserSkill(UUID userId, UserSkillDTO dto);

    void removeUserSkill(UUID userSkillId);
}
