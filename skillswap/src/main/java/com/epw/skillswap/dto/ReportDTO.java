package com.epw.skillswap.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportDTO {

    private UUID reportId;

    @NotNull(message = "Reporter user id is required")
    private UUID reporterUserId;

    @NotNull(message = "Reported user id is required")
    private UUID reportedUserId;

    @NotNull(message = "Session id is required")
    private UUID sessionId;

    @NotBlank(message = "Reason is required")
    private String reason;

    private String status;

    private LocalDateTime createdAt;
}