package com.epw.skillswap.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreditSummaryDTO {
    private Double currentBalance;
    private Double monthlyGoal;
    private Double earnedThisMonth;
    private Double spentThisMonth;
}
