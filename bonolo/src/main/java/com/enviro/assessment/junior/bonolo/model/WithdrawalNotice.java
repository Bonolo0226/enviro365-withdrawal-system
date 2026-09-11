package com.enviro.assessment.junior.bonolo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "withdrawal_notices")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WithdrawalNotice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double amount;

    private LocalDate requestDate;

    private String status; // e.g. "APPROVED", "REJECTED"

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}