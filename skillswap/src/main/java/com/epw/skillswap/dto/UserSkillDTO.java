package com.epw.skillswap.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSkillDTO {

    private UUID userSkillId;
    private UUID userId;
    private UUID skillId;
    private String skillName;
    private UUID categoryId;
    private String categoryName;
    private String proficiencyLevel;
    private Integer recommendedSessions;
    private Double creditsPerSession;
    private Integer sessionsCompleted;
}
