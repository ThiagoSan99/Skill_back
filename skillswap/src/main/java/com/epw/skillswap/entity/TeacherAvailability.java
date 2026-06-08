package com.epw.skillswap.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "teacher_availabilities")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeacherAvailability {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID availabilityId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User teacher;

    @NotNull
    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;

    @NotNull
    private LocalTime startTime;

    @NotNull
    private LocalTime endTime;
}
