package com.enviro.assessment.junior.bonolo.repository;

import com.enviro.assessment.junior.bonolo.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByInvestorId(Long investorId);
}