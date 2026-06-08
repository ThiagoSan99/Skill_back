package com.epw.skillswap.service;

import com.epw.skillswap.dto.*;

import java.util.List;
import java.util.UUID;

public interface DashboardService {
    CreditSummaryDTO getCreditSummary(UUID userId);
    List<CreditHistoryPointDTO> getCreditHistory(UUID userId);
    DashboardStatsDTO getStats(UUID userId);
    List<MatchUserDTO> getMatches(UUID userId);
    List<UserSessionDTO> getRecentSessions(UUID userId);
}
