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
public class MatchingDTO {

    private UUID matchId;

    @NotNull(message = "Requester user id is required")
    private UUID requesterUserId;

    @NotNull(message = "Matched user id is required")
    private UUID matchedUserId;

    private Double matchingScore;

    private String matchingReason;

    private String status;

    private LocalDateTime createdAt;
}