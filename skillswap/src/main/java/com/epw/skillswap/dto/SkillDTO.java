package com.epw.skillswap.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SkillDTO {

    private UUID skillId;
    private String skillName;
    private String description;
    private UUID categoryId;
    private String categoryName;
    private String difficultyLevel;
    private Integer recommendedSessions;
    private String tags;
    private Double creditsPerSession;
}