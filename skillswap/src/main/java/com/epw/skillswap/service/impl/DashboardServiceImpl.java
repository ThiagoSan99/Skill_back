package com.epw.skillswap.service.impl;

import com.epw.skillswap.dto.*;
import com.epw.skillswap.entity.*;
import com.epw.skillswap.repository.*;
import com.epw.skillswap.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardServiceImpl implements DashboardService {

    private final UserRepository userRepository;
    private final ExchangeSessionRepository sessionRepository;
    private final UserSkillRepository userSkillRepository;
    private final CreditTransactionRepository creditTransactionRepository;
    private final ReviewRepository reviewRepository;

    @Override
    public CreditSummaryDTO getCreditSummary(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow();

        YearMonth thisMonth = YearMonth.now();
        LocalDateTime start = thisMonth.atDay(1).atStartOfDay();
        LocalDateTime end = thisMonth.atEndOfMonth().atTime(23, 59, 59);

        Double earned = creditTransactionRepository.sumEarnedByReceiverBetween(userId, start, end);
        Double spent = creditTransactionRepository.sumSpentBySenderBetween(userId, start, end);

        return CreditSummaryDTO.builder()
                .currentBalance(user.getCurrentCreditBalance())
                .monthlyGoal(30.0)
                .earnedThisMonth(earned != null ? earned : 0.0)
                .spentThisMonth(spent != null ? spent : 0.0)
                .build();
    }

    @Override
    public List<CreditHistoryPointDTO> getCreditHistory(UUID userId) {
        LocalDateTime sixMonthsAgo = LocalDateTime.now().minusMonths(6).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);

        List<CreditTransaction> transactions = creditTransactionRepository.findByUserSince(userId, sixMonthsAgo);

        Map<YearMonth, CreditHistoryPointDTO> monthlyMap = new LinkedHashMap<>();
        for (int i = 5; i >= 0; i--) {
            YearMonth ym = YearMonth.now().minusMonths(i);
            monthlyMap.put(ym, CreditHistoryPointDTO.builder()
                    .month(ym.getMonth().getDisplayName(TextStyle.SHORT, new Locale("es", "ES")))
                    .earned(0.0)
                    .spent(0.0)
                    .build());
        }

        for (CreditTransaction t : transactions) {
            if (t.getTransactionDate() == null) continue;
            YearMonth ym = YearMonth.from(t.getTransactionDate());
            CreditHistoryPointDTO point = monthlyMap.get(ym);
            if (point == null) continue;

            if (t.getReceiver().getUserId().equals(userId) && t.getTransactionType() == TransactionType.EARNED) {
                point.setEarned(point.getEarned() + (t.getAmount() != null ? t.getAmount() : 0.0));
            }
            if (t.getSender().getUserId().equals(userId) && t.getTransactionType() == TransactionType.SPENT) {
                point.setSpent(point.getSpent() + (t.getAmount() != null ? t.getAmount() : 0.0));
            }
        }

        return new ArrayList<>(monthlyMap.values());
    }

    @Override
    public DashboardStatsDTO getStats(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow();

        List<ExchangeSession> asTeacher = sessionRepository.findByTeacherUserId(userId);
        List<ExchangeSession> asLearner = sessionRepository.findByLearnerUserId(userId);

        Set<UUID> allSessionIds = new HashSet<>();
        for (ExchangeSession s : asTeacher) allSessionIds.add(s.getSessionId());
        for (ExchangeSession s : asLearner) allSessionIds.add(s.getSessionId());

        List<UserSkill> teachingSkills = userSkillRepository.findByUserUserId(userId);
        long teachingCount = teachingSkills.size();

        Set<UUID> learningSkillIds = new HashSet<>();
        for (ExchangeSession s : asLearner) {
            if (s.getSkill() != null) {
                learningSkillIds.add(s.getSkill().getSkillId());
            }
        }

        return DashboardStatsDTO.builder()
                .totalSessions(allSessionIds.size())
                .skillsTeaching((int) teachingCount)
                .skillsLearning(learningSkillIds.size())
                .reputationScore(user.getReputationScore() != null ? user.getReputationScore() : 0.0)
                .build();
    }

    @Override
    public List<MatchUserDTO> getMatches(UUID userId) {
        List<UserSkill> allSkills = userSkillRepository.findAll();
        User currentUser = userRepository.findById(userId).orElseThrow();

        Set<UUID> currentUserSkillIds = allSkills.stream()
                .filter(us -> us.getUser().getUserId().equals(userId))
                .map(us -> us.getSkill().getSkillId())
                .collect(Collectors.toSet());

        List<MatchUserDTO> matches = new ArrayList<>();
        Set<UUID> seenUsers = new HashSet<>();

        for (UserSkill us : allSkills) {
            UUID teacherId = us.getUser().getUserId();
            if (teacherId.equals(userId)) continue;
            if (seenUsers.contains(teacherId)) continue;

            boolean hasComplementary = !currentUserSkillIds.contains(us.getSkill().getSkillId());
            if (!hasComplementary) continue;

            User teacher = us.getUser();
            seenUsers.add(teacherId);

            double compatibility = 70.0 + Math.random() * 25.0;

            String avatar = (teacher.getFirstName().charAt(0) + "" + teacher.getLastName().charAt(0)).toUpperCase();

            matches.add(MatchUserDTO.builder()
                    .userId(teacherId)
                    .firstName(teacher.getFirstName())
                    .lastName(teacher.getLastName())
                    .avatar(avatar)
                    .compatibility(Math.round(compatibility * 10.0) / 10.0)
                    .skillName(us.getSkill().getSkillName())
                    .skillLevel(us.getProficiencyLevel() != null ? us.getProficiencyLevel() : "Intermedio")
                    .creditsPerSession(us.getCreditsPerSession() != null ? us.getCreditsPerSession() : 1.0)
                    .build());

            if (matches.size() >= 10) break;
        }

        matches.sort((a, b) -> Double.compare(b.getCompatibility(), a.getCompatibility()));

        return matches;
    }

    @Override
    public List<UserSessionDTO> getRecentSessions(UUID userId) {
        List<ExchangeSession> asTeacher = sessionRepository.findByTeacherUserId(userId);
        List<ExchangeSession> asLearner = sessionRepository.findByLearnerUserId(userId);

        List<UserSessionDTO> result = new ArrayList<>();

        for (ExchangeSession s : asTeacher) {
            result.add(mapToUserSessionDTO(s, "TEACHER", s.getLearner(), userId));
        }
        for (ExchangeSession s : asLearner) {
            result.add(mapToUserSessionDTO(s, "LEARNER", s.getTeacher(), userId));
        }

        result.sort(Comparator.comparing(UserSessionDTO::getScheduledDate).reversed());

        return result.stream().limit(10).collect(Collectors.toList());
    }

    private UserSessionDTO mapToUserSessionDTO(ExchangeSession session, String role, User partner, UUID currentUserId) {
        Integer rating = null;
        List<Review> reviews = reviewRepository.findBySessionSessionId(session.getSessionId());
        if (!reviews.isEmpty()) {
            rating = reviews.getFirst().getRating();
        }

        double credits = session.getCreditsExchanged() != null ? session.getCreditsExchanged() : 0.0;
        if ("LEARNER".equals(role)) {
            credits = -credits;
        }

        String avatar = (partner.getFirstName().charAt(0) + "" + partner.getLastName().charAt(0)).toUpperCase();

        return UserSessionDTO.builder()
                .sessionId(session.getSessionId())
                .skillName(session.getSkill().getSkillName())
                .partnerFirstName(partner.getFirstName())
                .partnerLastName(partner.getLastName())
                .partnerAvatar(avatar)
                .scheduledDate(session.getScheduledDate())
                .durationHours(session.getDurationHours())
                .creditsExchanged(credits)
                .status(session.getStatus().name())
                .role(role)
                .rating(rating)
                .meetingLink(session.getMeetingLink())
                .build();
    }
}
