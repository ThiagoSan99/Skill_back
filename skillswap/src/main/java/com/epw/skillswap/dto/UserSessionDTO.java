package com.epw.skillswap.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSessionDTO {
    private UUID sessionId;
    private String skillName;
    private String partnerFirstName;
    private String partnerLastName;
    private String partnerAvatar;
    private LocalDateTime scheduledDate;
    private Double durationHours;
    private Double creditsExchanged;
    private String status;
    private String role;
    private Integer rating;
    private String meetingLink;
}
