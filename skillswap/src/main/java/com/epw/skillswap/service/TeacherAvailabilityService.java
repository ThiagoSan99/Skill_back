package com.epw.skillswap.service;

import com.epw.skillswap.dto.TeacherAvailabilityDTO;

import java.util.List;
import java.util.UUID;

public interface TeacherAvailabilityService {

    List<TeacherAvailabilityDTO> getMyAvailability(UUID userId);

    TeacherAvailabilityDTO addAvailability(UUID userId, TeacherAvailabilityDTO dto);

    TeacherAvailabilityDTO updateAvailability(UUID availabilityId, UUID userId, TeacherAvailabilityDTO dto);

    void removeAvailability(UUID availabilityId, UUID userId);
}
