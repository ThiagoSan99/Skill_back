package com.epw.skillswap.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "reports")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID reportId;

    @ManyToOne
    @JoinColumn(name = "reporter_user_id", nullable = false)
    private User reporter;

    @ManyToOne
    @JoinColumn(name = "reported_user_id", nullable = false)
    private User reported;

    @ManyToOne
    @JoinColumn(name = "session_id", nullable = false)
    private ExchangeSession session;

    @NotBlank(message = "Reason is required")
    @Column(nullable = false, length = 500)
    private String reason;

    @Enumerated(EnumType.STRING)
    private ReportStatus status;

    private LocalDateTime createdAt;
}