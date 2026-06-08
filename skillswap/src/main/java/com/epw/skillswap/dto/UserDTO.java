package com.epw.skillswap.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

    private UUID userId;
    private String firstName;
    private String lastName;
    private String email;
    private Double currentCreditBalance;
    private Double reputationScore;
    private String bio;
}