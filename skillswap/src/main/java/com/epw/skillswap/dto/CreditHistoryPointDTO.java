package com.epw.skillswap.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreditHistoryPointDTO {
    private String month;
    private Double earned;
    private Double spent;
}
