package com.epw.skillswap.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID userId;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @Email
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank
    @JsonIgnore
    private String password;
    
    @Builder.Default
    private Double currentCreditBalance = 20.0;

    @Builder.Default
    private Double reputationScore = 0.0;

    private Boolean verified = false;

    private LocalDateTime registrationDate = LocalDateTime.now();

    @Column(columnDefinition = "TEXT")
    private String bio;
}