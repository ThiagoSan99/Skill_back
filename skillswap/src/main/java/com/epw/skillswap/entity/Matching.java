package com.epw.skillswap.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "matchings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Matching {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID matchId;

    @ManyToOne
    @JoinColumn(name = "requester_user_id", nullable = false)
    private User requesterUser;

    @ManyToOne
    @JoinColumn(name = "matched_user_id", nullable = false)
    private User matchedUser;

    private Double matchingScore;

    @Column(length = 500)
    private String matchingReason;

    @Enumerated(EnumType.STRING)
    private MatchingStatus status;

    private LocalDateTime createdAt;
}