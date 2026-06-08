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
public class CreditTransactionDTO {

    private UUID transactionId;

    @NotNull(message = "Session id is required")
    private UUID sessionId;

    @NotNull(message = "Sender user id is required")
    private UUID senderUserId;

    @NotNull(message = "Receiver user id is required")
    private UUID receiverUserId;

    @NotNull(message = "Amount is required")
    private Double amount;

    private LocalDateTime transactionDate;

    private String transactionType;
}