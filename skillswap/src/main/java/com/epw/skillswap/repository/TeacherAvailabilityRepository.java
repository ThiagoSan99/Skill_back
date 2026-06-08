package com.epw.skillswap.repository;

import com.epw.skillswap.entity.TeacherAvailability;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.DayOfWeek;
import java.util.List;
import java.util.UUID;

public interface TeacherAvailabilityRepository extends JpaRepository<TeacherAvailability, UUID> {

    List<TeacherAvailability> findByTeacherUserId(UUID teacherUserId);

    List<TeacherAvailability> findByTeacherUserIdAndDayOfWeek(UUID teacherUserId, DayOfWeek dayOfWeek);
}
