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
public class ExchangeSessionDTO {

    private UUID sessionId;

    @NotNull(message = "Teacher id is required")
    private UUID teacherUserId;

    @NotNull(message = "Learner id is required")
    private UUID learnerUserId;

    @NotNull(message = "Skill id is required")
    private UUID skillId;

    @NotNull(message = "Scheduled date is required")
    private LocalDateTime scheduledDate;

    private Double durationHours;

    private Double creditsExchanged;

    private String meetingLink;

    private String sessionNotes;

    private String status;
}