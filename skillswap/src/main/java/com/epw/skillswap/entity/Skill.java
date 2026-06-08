package com.epw.skillswap.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "skills")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID skillId;

    @NotBlank
    private String skillName;

    private String description;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    private String difficultyLevel;

    private Integer recommendedSessions;

    private String tags;

    private Double creditsPerSession;
}