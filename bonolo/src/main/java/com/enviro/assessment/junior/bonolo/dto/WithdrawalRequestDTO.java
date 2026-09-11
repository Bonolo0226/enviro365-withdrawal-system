package com.enviro.assessment.junior.bonolo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WithdrawalRequestDTO {

    private Long productId;

    private double amount;
}