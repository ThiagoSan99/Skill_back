package com.epw.skillswap.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "user_skills")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID userSkillId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "skill_id")
    private Skill skill;

    private String skillType;

    private String proficiencyLevel;

    private Integer yearsExperience;

    private Integer recommendedSessions;

    private Double creditsPerSession;

    @Builder.Default
    private Integer sessionsCompleted = 0;
}