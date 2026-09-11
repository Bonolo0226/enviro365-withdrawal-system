package com.enviro.assessment.junior.bonolo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WithdrawalResponseDTO {

    private Long withdrawalId;

    private String productName;

    private double amount;

    private String status;

    private LocalDate requestDate;

    private double remainingBalance;
}