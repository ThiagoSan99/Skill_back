package com.epw.skillswap.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchUserDTO {
    private UUID userId;
    private String firstName;
    private String lastName;
    private String avatar;
    private Double compatibility;
    private String skillName;
    private String skillLevel;
    private Double creditsPerSession;
}
