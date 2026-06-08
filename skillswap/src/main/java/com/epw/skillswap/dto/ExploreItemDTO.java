package com.epw.skillswap.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExploreItemDTO {

    private UUID userSkillId;
    private UUID skillId;
    private String skillName;
    private UUID categoryId;
    private String categoryName;
    private String tags;
    private String difficultyLevel;
    private Double creditsPerSession;
    private Integer sessionsCompleted;
    private Double rating;
    private UUID teacherUserId;
    private String teacherFirstName;
    private String teacherLastName;
    private String teacherAvatar;
}
