package com.epw.skillswap.controller;

import com.epw.skillswap.dto.BookedSlotDTO;
import com.epw.skillswap.dto.BookSessionRequest;
import com.epw.skillswap.dto.ExchangeSessionDTO;
import com.epw.skillswap.dto.TeacherAvailabilityDTO;
import com.epw.skillswap.dto.UserSessionDTO;
import com.epw.skillswap.service.ExchangeSessionService;
import com.epw.skillswap.service.TeacherAvailabilityService;
import com.epw.skillswap.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/sessions")
@RequiredArgsConstructor
public class ExchangeSessionController {

    private final ExchangeSessionService sessionService;
    private final TeacherAvailabilityService availabilityService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<ExchangeSessionDTO> createSession(
            @Valid @RequestBody ExchangeSessionDTO dto) {

        return new ResponseEntity<>(
                sessionService.createSession(dto),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExchangeSessionDTO> getSessionById(
            @PathVariable UUID id) {

        return ResponseEntity.ok(
                sessionService.getSessionById(id)
        );
    }

    @GetMapping
    public ResponseEntity<List<ExchangeSessionDTO>> getAllSessions(
            @RequestParam(required = false) UUID teacherUserId,
            @RequestParam(required = false) UUID learnerUserId) {

        if (teacherUserId != null) {
            return ResponseEntity.ok(
                    sessionService.getSessionsByTeacherUserId(teacherUserId)
            );
        }
        if (learnerUserId != null) {
            return ResponseEntity.ok(
                    sessionService.getSessionsByLearnerUserId(learnerUserId)
            );
        }

        return ResponseEntity.ok(
                sessionService.getAllSessions()
        );
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ExchangeSessionDTO> updateStatus(
            @PathVariable UUID id,
            @RequestParam String status) {

        return ResponseEntity.ok(
                sessionService.updateSessionStatus(id, status)
        );
    }

    @PostMapping("/book")
    public ResponseEntity<ExchangeSessionDTO> bookSession(
            @Valid @RequestBody BookSessionRequest request,
            Authentication authentication) {
        UUID learnerId = userService.getUserIdByEmail(authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(sessionService.bookSession(learnerId, request));
    }

    @GetMapping("/me")
    public ResponseEntity<List<UserSessionDTO>> getMySessions(
            Authentication authentication) {
        UUID userId = userService.getUserIdByEmail(authentication.getName());
        return ResponseEntity.ok(sessionService.getMySessions(userId));
    }

    @GetMapping("/teacher-availability")
    public ResponseEntity<List<TeacherAvailabilityDTO>> getTeacherAvailability(
            @RequestParam UUID teacherUserId) {
        return ResponseEntity.ok(availabilityService.getMyAvailability(teacherUserId));
    }

    @GetMapping("/booked-slots")
    public ResponseEntity<List<BookedSlotDTO>> getBookedSlots(
            @RequestParam UUID teacherUserId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(sessionService.getBookedSlotsByTeacherAndDate(teacherUserId, date));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSession(
            @PathVariable UUID id) {

        sessionService.deleteSession(id);

        return ResponseEntity.noContent().build();
    }
}