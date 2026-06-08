package com.epw.skillswap.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "exchange_sessions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExchangeSession {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID sessionId;

    @ManyToOne
    @JoinColumn(name = "teacher_user_id", nullable = false)
    private User teacher;

    @ManyToOne
    @JoinColumn(name = "learner_user_id", nullable = false)
    private User learner;

    @ManyToOne
    @JoinColumn(name = "skill_id", nullable = false)
    private Skill skill;

    @NotNull(message = "Scheduled date is required")
    private LocalDateTime scheduledDate;

    private Double durationHours;

    private Double creditsExchanged;

    private String meetingLink;

    private String sessionNotes;

    @Enumerated(EnumType.STRING)
    private SessionStatus status;
}