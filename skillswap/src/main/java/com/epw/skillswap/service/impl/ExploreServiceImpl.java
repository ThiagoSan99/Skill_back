package com.epw.skillswap.service.impl;

import com.epw.skillswap.dto.ExploreItemDTO;
import com.epw.skillswap.entity.Category;
import com.epw.skillswap.entity.User;
import com.epw.skillswap.entity.UserSkill;
import com.epw.skillswap.repository.UserSkillRepository;
import com.epw.skillswap.service.ExploreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ExploreServiceImpl implements ExploreService {

    private final UserSkillRepository userSkillRepository;

    @Override
    public List<ExploreItemDTO> getExplore(UUID categoryId, String level, String search, String sort, UUID excludeUserId) {

        List<UserSkill> userSkills = userSkillRepository.findAll();

        return userSkills.stream()
                .filter(us -> excludeUserId == null || !us.getUser().getUserId().equals(excludeUserId))
                .filter(us -> categoryId == null
                        || (us.getSkill().getCategory() != null
                        && us.getSkill().getCategory().getCategoryId().equals(categoryId)))
                .filter(us -> level == null || level.isBlank()
                        || level.equalsIgnoreCase(us.getProficiencyLevel())
                        || level.equalsIgnoreCase(us.getSkill().getDifficultyLevel()))
                .filter(us -> {
                    if (search == null || search.isBlank()) return true;
                    String q = search.toLowerCase();
                    return us.getSkill().getSkillName().toLowerCase().contains(q)
                            || us.getUser().getFirstName().toLowerCase().contains(q)
                            || us.getUser().getLastName().toLowerCase().contains(q);
                })
                .sorted(getComparator(sort))
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private Comparator<? super UserSkill> getComparator(String sort) {
        if ("sessions".equalsIgnoreCase(sort)) {
            return Comparator.<UserSkill, Integer>comparing(
                    us -> us.getSessionsCompleted() != null ? us.getSessionsCompleted() : 0,
                    Comparator.reverseOrder());
        }
        if ("credits".equalsIgnoreCase(sort)) {
            return Comparator.<UserSkill, Double>comparing(
                    us -> us.getCreditsPerSession() != null ? us.getCreditsPerSession() : Double.MAX_VALUE);
        }
        return Comparator.<UserSkill, Double>comparing(
                us -> us.getUser().getReputationScore() != null ? us.getUser().getReputationScore() : 0.0,
                Comparator.reverseOrder());
    }

    private ExploreItemDTO mapToDTO(UserSkill userSkill) {
        User user = userSkill.getUser();
        Category category = userSkill.getSkill().getCategory();

        return ExploreItemDTO.builder()
                .userSkillId(userSkill.getUserSkillId())
                .skillId(userSkill.getSkill().getSkillId())
                .skillName(userSkill.getSkill().getSkillName())
                .categoryId(category != null ? category.getCategoryId() : null)
                .categoryName(category != null ? category.getName() : null)
                .tags(userSkill.getSkill().getTags())
                .difficultyLevel(userSkill.getSkill().getDifficultyLevel())
                .creditsPerSession(userSkill.getCreditsPerSession())
                .sessionsCompleted(userSkill.getSessionsCompleted())
                .rating(user.getReputationScore())
                .teacherUserId(user.getUserId())
                .teacherFirstName(user.getFirstName())
                .teacherLastName(user.getLastName())
                .teacherAvatar(user.getFirstName().substring(0, 1).toUpperCase()
                        + user.getLastName().substring(0, 1).toUpperCase())
                .build();
    }
}
