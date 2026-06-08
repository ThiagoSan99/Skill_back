package com.epw.skillswap.service.impl;

import com.epw.skillswap.dto.BookedSlotDTO;
import com.epw.skillswap.dto.BookSessionRequest;
import com.epw.skillswap.dto.ExchangeSessionDTO;
import com.epw.skillswap.dto.UserSessionDTO;
import com.epw.skillswap.entity.*;
import com.epw.skillswap.exception.ResourceNotFoundException;
import com.epw.skillswap.repository.*;
import com.epw.skillswap.service.ExchangeSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ExchangeSessionServiceImpl
        implements ExchangeSessionService {

    private final ExchangeSessionRepository sessionRepository;
    private final UserRepository userRepository;
    private final SkillRepository skillRepository;
    private final UserSkillRepository userSkillRepository;
    private final TeacherAvailabilityRepository availabilityRepository;
    private final ReviewRepository reviewRepository;

    @Override
    public ExchangeSessionDTO createSession(
            ExchangeSessionDTO dto) {

        User teacher = userRepository.findById(dto.getTeacherUserId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Teacher not found"));

        User learner = userRepository.findById(dto.getLearnerUserId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Learner not found"));

        Skill skill = skillRepository.findById(dto.getSkillId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Skill not found"));

        ExchangeSession session = ExchangeSession.builder()
                .teacher(teacher)
                .learner(learner)
                .skill(skill)
                .scheduledDate(dto.getScheduledDate())
                .durationHours(dto.getDurationHours())
                .creditsExchanged(dto.getCreditsExchanged())
                .meetingLink(dto.getMeetingLink())
                .sessionNotes(dto.getSessionNotes())
                .status(SessionStatus.PENDING)
                .build();

        return mapToDTO(sessionRepository.save(session));
    }

    @Override
    public ExchangeSessionDTO bookSession(UUID learnerId, BookSessionRequest request) {
        UserSkill userSkill = userSkillRepository.findById(request.getUserSkillId())
                .orElseThrow(() -> new ResourceNotFoundException("Skill not found"));

        User teacher = userSkill.getUser();
        Skill skill = userSkill.getSkill();

        if (teacher.getUserId().equals(learnerId)) {
            throw new RuntimeException("You cannot book your own skill");
        }

        User learner = userRepository.findById(learnerId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        LocalDateTime scheduledDate = request.getScheduledDate();
        LocalTime startTime = scheduledDate.toLocalTime();
        LocalTime endTime = startTime.plusMinutes(Math.round(request.getDurationHours() * 60));

        if (scheduledDate.isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Scheduled date must be in the future");
        }

        LocalTime EARLIEST = LocalTime.of(8, 0);
        LocalTime LATEST = LocalTime.of(19, 0);
        if (startTime.isBefore(EARLIEST) || endTime.isAfter(LATEST)) {
            throw new RuntimeException("Sessions must be between 08:00 and 19:00");
        }

        DayOfWeek dayOfWeek = scheduledDate.getDayOfWeek();
        List<TeacherAvailability> availabilities = availabilityRepository
                .findByTeacherUserIdAndDayOfWeek(teacher.getUserId(), dayOfWeek);

        boolean hasSlot = availabilities.stream()
                .anyMatch(a -> !startTime.isBefore(a.getStartTime()) && !endTime.isAfter(a.getEndTime()));
        if (!hasSlot) {
            throw new RuntimeException("Teacher is not available at this time");
        }

        List<ExchangeSession> overlapping = sessionRepository
                .findByTeacherUserIdAndScheduledDateBetween(
                        teacher.getUserId(),
                        scheduledDate.minusMinutes(1),
                        scheduledDate.plusMinutes(Math.round(request.getDurationHours() * 60) + 1));
        overlapping.removeIf(s -> s.getStatus() == SessionStatus.CANCELLED);
        if (!overlapping.isEmpty()) {
            throw new RuntimeException("Teacher already has a session at this time");
        }

        Double creditsExchanged = userSkill.getCreditsPerSession() * request.getDurationHours();

        if (learner.getCurrentCreditBalance() < creditsExchanged) {
            throw new RuntimeException("Insufficient credits");
        }

        learner.setCurrentCreditBalance(learner.getCurrentCreditBalance() - creditsExchanged);
        userRepository.save(learner);

        ExchangeSession session = ExchangeSession.builder()
                .teacher(teacher)
                .learner(learner)
                .skill(skill)
                .scheduledDate(scheduledDate)
                .durationHours(request.getDurationHours())
                .creditsExchanged(creditsExchanged)
                .status(SessionStatus.PENDING)
                .build();

        return mapToDTO(sessionRepository.save(session));
    }

    @Override
    @Transactional(readOnly = true)
    public ExchangeSessionDTO getSessionById(UUID sessionId) {

        ExchangeSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Session not found"));

        return mapToDTO(session);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExchangeSessionDTO> getAllSessions() {

        return sessionRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExchangeSessionDTO> getSessionsByTeacherUserId(UUID teacherUserId) {
        return sessionRepository.findByTeacherUserId(teacherUserId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExchangeSessionDTO> getSessionsByLearnerUserId(UUID learnerUserId) {
        return sessionRepository.findByLearnerUserId(learnerUserId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ExchangeSessionDTO updateSessionStatus(
            UUID sessionId,
            String status) {

        ExchangeSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Session not found"));

        session.setStatus(SessionStatus.valueOf(status.toUpperCase()));

        return mapToDTO(sessionRepository.save(session));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserSessionDTO> getMySessions(UUID userId) {
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
        return result;
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

    @Override
    @Transactional(readOnly = true)
    public List<BookedSlotDTO> getBookedSlotsByTeacherAndDate(UUID teacherUserId, LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(LocalTime.MAX);
        return sessionRepository.findByTeacherUserIdAndScheduledDateBetween(teacherUserId, start, end)
                .stream()
                .filter(s -> s.getStatus() != SessionStatus.CANCELLED)
                .map(s -> {
                    LocalTime st = s.getScheduledDate().toLocalTime();
                    LocalTime et = st.plusMinutes(Math.round(s.getDurationHours() * 60));
                    return BookedSlotDTO.builder()
                            .startTime(st.toString())
                            .endTime(et.toString())
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Override
    public void deleteSession(UUID sessionId) {

        ExchangeSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Session not found"));

        sessionRepository.delete(session);
    }

    private ExchangeSessionDTO mapToDTO(
            ExchangeSession session) {

        return ExchangeSessionDTO.builder()
                .sessionId(session.getSessionId())
                .teacherUserId(session.getTeacher().getUserId())
                .learnerUserId(session.getLearner().getUserId())
                .skillId(session.getSkill().getSkillId())
                .scheduledDate(session.getScheduledDate())
                .durationHours(session.getDurationHours())
                .creditsExchanged(session.getCreditsExchanged())
                .meetingLink(session.getMeetingLink())
                .sessionNotes(session.getSessionNotes())
                .status(session.getStatus().name())
                .build();
    }
}
