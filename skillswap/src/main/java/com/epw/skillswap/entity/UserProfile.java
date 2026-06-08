package com.epw.skillswap.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "user_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID profileId;

    private String bio;

    private String university;

    private String degreeProgram;

    private Integer semester;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}