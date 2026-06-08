package com.epw.skillswap.controller;

import com.epw.skillswap.dto.*;
import com.epw.skillswap.service.DashboardService;
import com.epw.skillswap.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;
    private final UserService userService;

    @GetMapping("/credits")
    public ResponseEntity<CreditSummaryDTO> getCreditSummary(Authentication authentication) {
        UUID userId = userService.getUserIdByEmail(authentication.getName());
        return ResponseEntity.ok(dashboardService.getCreditSummary(userId));
    }

    @GetMapping("/credit-history")
    public ResponseEntity<List<CreditHistoryPointDTO>> getCreditHistory(Authentication authentication) {
        UUID userId = userService.getUserIdByEmail(authentication.getName());
        return ResponseEntity.ok(dashboardService.getCreditHistory(userId));
    }

    @GetMapping("/stats")
    public ResponseEntity<DashboardStatsDTO> getStats(Authentication authentication) {
        UUID userId = userService.getUserIdByEmail(authentication.getName());
        return ResponseEntity.ok(dashboardService.getStats(userId));
    }

    @GetMapping("/matches")
    public ResponseEntity<List<MatchUserDTO>> getMatches(Authentication authentication) {
        UUID userId = userService.getUserIdByEmail(authentication.getName());
        return ResponseEntity.ok(dashboardService.getMatches(userId));
    }

    @GetMapping("/recent-sessions")
    public ResponseEntity<List<UserSessionDTO>> getRecentSessions(Authentication authentication) {
        UUID userId = userService.getUserIdByEmail(authentication.getName());
        return ResponseEntity.ok(dashboardService.getRecentSessions(userId));
    }
}
