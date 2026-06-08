package com.epw.skillswap.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
public class ReviewDTO {

    private UUID reviewId;

    @NotNull(message = "Session id is required")
    private UUID sessionId;

    @NotNull(message = "Reviewer user id is required")
    private UUID reviewerUserId;

    @NotNull(message = "Reviewed user id is required")
    private UUID reviewedUserId;

    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private Integer rating;

    @NotBlank(message = "Comment is required")
    private String comment;

    private LocalDateTime createdAt;
}