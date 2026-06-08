package com.epw.skillswap.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "reviews")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID reviewId;

    @ManyToOne
    @JoinColumn(name = "session_id", nullable = false)
    private ExchangeSession session;

    @ManyToOne
    @JoinColumn(name = "reviewer_user_id", nullable = false)
    private User reviewer;

    @ManyToOne
    @JoinColumn(name = "reviewed_user_id", nullable = false)
    private User reviewed;

    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private Integer rating;

    @NotBlank(message = "Comment is required")
    @Column(length = 500)
    private String comment;

    private LocalDateTime createdAt;
}