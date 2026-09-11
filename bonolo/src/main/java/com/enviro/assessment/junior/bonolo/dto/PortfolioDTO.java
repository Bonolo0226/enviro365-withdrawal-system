package com.enviro.assessment.junior.bonolo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PortfolioDTO {

    private Long investorId;

    private String investorName;

    private int age;

    private List<ProductDTO> products;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductDTO {
        private Long productId;
        private String productName;
        private String productType;
        private double balance;
    }
}