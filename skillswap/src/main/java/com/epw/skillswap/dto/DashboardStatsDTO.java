package com.epw.skillswap.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardStatsDTO {
    private Integer totalSessions;
    private Integer skillsTeaching;
    private Integer skillsLearning;
    private Double reputationScore;
}
