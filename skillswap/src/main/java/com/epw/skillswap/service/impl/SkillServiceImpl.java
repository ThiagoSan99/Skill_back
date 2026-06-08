package com.epw.skillswap.service.impl;


import com.epw.skillswap.dto.SkillDTO;
import com.epw.skillswap.entity.Category;
import com.epw.skillswap.entity.Skill;
import com.epw.skillswap.exception.ResourceNotFoundException;
import com.epw.skillswap.repository.CategoryRepository;
import com.epw.skillswap.repository.SkillRepository;
import com.epw.skillswap.service.SkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class SkillServiceImpl implements SkillService {

    private final SkillRepository repository;
    private final CategoryRepository categoryRepository;

    @Override
    public SkillDTO create(SkillDTO dto) {

        Category category = dto.getCategoryId() != null
                ? categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found"))
                : null;

        Skill skill = Skill.builder()
                .skillName(dto.getSkillName())
                .description(dto.getDescription())
                .category(category)
                .difficultyLevel(dto.getDifficultyLevel())
                .recommendedSessions(dto.getRecommendedSessions())
                .tags(dto.getTags())
                .creditsPerSession(dto.getCreditsPerSession())
                .build();

        return mapToDTO(repository.save(skill));
    }

    @Override
    public List<SkillDTO> getAll() {
        return repository.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public SkillDTO getById(UUID id) {

        Skill skill = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Skill not found"));

        return mapToDTO(skill);
    }

    @Override
    public SkillDTO update(UUID id, SkillDTO dto) {

        Skill skill = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Skill not found"));

        if (dto.getSkillName() != null) {
            skill.setSkillName(dto.getSkillName());
        }
        if (dto.getDescription() != null) {
            skill.setDescription(dto.getDescription());
        }
        if (dto.getCategoryId() != null) {
            Category category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
            skill.setCategory(category);
        }
        if (dto.getDifficultyLevel() != null) {
            skill.setDifficultyLevel(dto.getDifficultyLevel());
        }
        if (dto.getRecommendedSessions() != null) {
            skill.setRecommendedSessions(dto.getRecommendedSessions());
        }
        if (dto.getTags() != null) {
            skill.setTags(dto.getTags());
        }
        if (dto.getCreditsPerSession() != null) {
            skill.setCreditsPerSession(dto.getCreditsPerSession());
        }

        return mapToDTO(repository.save(skill));
    }

    @Override
    public void delete(UUID id) {
        repository.deleteById(id);
    }

    private SkillDTO mapToDTO(Skill skill){
        Category category = skill.getCategory();
        return SkillDTO.builder()
                .skillId(skill.getSkillId())
                .skillName(skill.getSkillName())
                .description(skill.getDescription())
                .categoryId(category != null ? category.getCategoryId() : null)
                .categoryName(category != null ? category.getName() : null)
                .difficultyLevel(skill.getDifficultyLevel())
                .recommendedSessions(skill.getRecommendedSessions())
                .tags(skill.getTags())
                .creditsPerSession(skill.getCreditsPerSession())
                .build();
    }
}