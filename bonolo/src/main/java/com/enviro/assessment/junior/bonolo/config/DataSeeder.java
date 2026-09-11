package com.enviro.assessment.junior.bonolo.config;

import com.enviro.assessment.junior.bonolo.model.Investor;
import com.enviro.assessment.junior.bonolo.model.Product;
import com.enviro.assessment.junior.bonolo.repository.InvestorRepository;
import com.enviro.assessment.junior.bonolo.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private final InvestorRepository investorRepository;
    private final ProductRepository productRepository;

    public DataSeeder(InvestorRepository investorRepository, ProductRepository productRepository) {
        this.investorRepository = investorRepository;
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) {
        Investor investor1 = investorRepository.save(new Investor(null, "Thabo Mokoena", 70, 0));
        productRepository.save(new Product(null, "Golden Years Retirement Fund", "RETIREMENT", 100000, investor1));
        productRepository.save(new Product(null, "Flexible Savings", "SAVINGS", 50000, investor1));

        Investor investor2 = investorRepository.save(new Investor(null, "Naledi Dube", 40, 0));
        productRepository.save(new Product(null, "Unit Trust Growth Plan", "UNIT_TRUST", 30000, investor2));
    }
}