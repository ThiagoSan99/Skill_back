package com.epw.skillswap.service.impl;

import com.epw.skillswap.dto.TeacherAvailabilityDTO;
import com.epw.skillswap.entity.TeacherAvailability;
import com.epw.skillswap.entity.User;
import com.epw.skillswap.exception.ResourceNotFoundException;
import com.epw.skillswap.repository.TeacherAvailabilityRepository;
import com.epw.skillswap.repository.UserRepository;
import com.epw.skillswap.service.TeacherAvailabilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class TeacherAvailabilityServiceImpl implements TeacherAvailabilityService {

    private final TeacherAvailabilityRepository repository;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<TeacherAvailabilityDTO> getMyAvailability(UUID userId) {
        return repository.findByTeacherUserId(userId)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public TeacherAvailabilityDTO addAvailability(UUID userId, TeacherAvailabilityDTO dto) {
        User teacher = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        TeacherAvailability availability = TeacherAvailability.builder()
                .teacher(teacher)
                .dayOfWeek(dto.getDayOfWeek())
                .startTime(dto.getStartTime())
                .endTime(dto.getEndTime())
                .build();

        return mapToDTO(repository.save(availability));
    }

    @Override
    public TeacherAvailabilityDTO updateAvailability(UUID availabilityId, UUID userId, TeacherAvailabilityDTO dto) {
        TeacherAvailability availability = repository.findById(availabilityId)
                .orElseThrow(() -> new ResourceNotFoundException("Availability not found"));

        if (!availability.getTeacher().getUserId().equals(userId)) {
            throw new RuntimeException("You can only edit your own availability");
        }

        if (dto.getDayOfWeek() != null) availability.setDayOfWeek(dto.getDayOfWeek());
        if (dto.getStartTime() != null) availability.setStartTime(dto.getStartTime());
        if (dto.getEndTime() != null) availability.setEndTime(dto.getEndTime());

        return mapToDTO(repository.save(availability));
    }

    @Override
    public void removeAvailability(UUID availabilityId, UUID userId) {
        TeacherAvailability availability = repository.findById(availabilityId)
                .orElseThrow(() -> new ResourceNotFoundException("Availability not found"));

        if (!availability.getTeacher().getUserId().equals(userId)) {
            throw new RuntimeException("You can only delete your own availability");
        }

        repository.delete(availability);
    }

    private TeacherAvailabilityDTO mapToDTO(TeacherAvailability availability) {
        return TeacherAvailabilityDTO.builder()
                .availabilityId(availability.getAvailabilityId())
                .teacherUserId(availability.getTeacher().getUserId())
                .dayOfWeek(availability.getDayOfWeek())
                .startTime(availability.getStartTime())
                .endTime(availability.getEndTime())
                .build();
    }
}
