package com.epw.skillswap.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "credit_transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreditTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID transactionId;

    @ManyToOne
    @JoinColumn(name = "session_id", nullable = false)
    private ExchangeSession session;

    @ManyToOne
    @JoinColumn(name = "sender_user_id", nullable = false)
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiver_user_id", nullable = false)
    private User receiver;

    @NotNull(message = "Amount is required")
    private Double amount;

    private LocalDateTime transactionDate;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;
}