package com.epw.skillswap.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookSessionRequest {

    @NotNull(message = "Skill is required")
    private UUID userSkillId;

    @NotNull(message = "Scheduled date is required")
    private LocalDateTime scheduledDate;

    @NotNull(message = "Duration is required")
    private Double durationHours;
}
