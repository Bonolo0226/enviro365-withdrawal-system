package com.enviro.assessment.junior.bonolo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productName;

    private String productType; // e.g. "RETIREMENT", "SAVINGS", "UNIT_TRUST"

    private double balance;

    @ManyToOne
    @JoinColumn(name = "investor_id")
    private Investor investor;
}