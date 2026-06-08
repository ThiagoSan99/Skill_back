package com.epw.skillswap.service.impl;

import com.epw.skillswap.dto.UserSkillDTO;
import com.epw.skillswap.entity.Category;
import com.epw.skillswap.entity.Skill;
import com.epw.skillswap.entity.User;
import com.epw.skillswap.entity.UserSkill;
import com.epw.skillswap.exception.ResourceNotFoundException;
import com.epw.skillswap.repository.CategoryRepository;
import com.epw.skillswap.repository.SkillRepository;
import com.epw.skillswap.repository.UserRepository;
import com.epw.skillswap.repository.UserSkillRepository;
import com.epw.skillswap.service.UserSkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UserSkillServiceImpl implements UserSkillService {

    private final UserSkillRepository userSkillRepository;
    private final UserRepository userRepository;
    private final SkillRepository skillRepository;
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional(readOnly = true)
    public List<UserSkillDTO> getUserSkills(UUID userId) {
        return userSkillRepository.findByUserUserId(userId)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public UserSkillDTO addUserSkill(UUID userId, UserSkillDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        Skill skill = Skill.builder()
                .skillName(dto.getSkillName())
                .category(category)
                .difficultyLevel(dto.getProficiencyLevel())
                .recommendedSessions(dto.getRecommendedSessions())
                .build();

        skill = skillRepository.save(skill);

        UserSkill userSkill = UserSkill.builder()
                .user(user)
                .skill(skill)
                .proficiencyLevel(dto.getProficiencyLevel())
                .recommendedSessions(dto.getRecommendedSessions())
                .creditsPerSession(dto.getCreditsPerSession())
                .build();

        userSkill = userSkillRepository.save(userSkill);

        return mapToDTO(userSkill);
    }

    @Override
    public void removeUserSkill(UUID userSkillId) {
        UserSkill userSkill = userSkillRepository.findById(userSkillId)
                .orElseThrow(() -> new ResourceNotFoundException("UserSkill not found"));
        userSkillRepository.delete(userSkill);
    }

    private UserSkillDTO mapToDTO(UserSkill userSkill) {
        Skill skill = userSkill.getSkill();
        Category category = skill.getCategory();

        return UserSkillDTO.builder()
                .userSkillId(userSkill.getUserSkillId())
                .userId(userSkill.getUser().getUserId())
                .skillId(skill.getSkillId())
                .skillName(skill.getSkillName())
                .categoryId(category != null ? category.getCategoryId() : null)
                .categoryName(category != null ? category.getName() : null)
                .proficiencyLevel(userSkill.getProficiencyLevel())
                .recommendedSessions(userSkill.getRecommendedSessions())
                .creditsPerSession(userSkill.getCreditsPerSession())
                .sessionsCompleted(userSkill.getSessionsCompleted())
                .build();
    }
}
