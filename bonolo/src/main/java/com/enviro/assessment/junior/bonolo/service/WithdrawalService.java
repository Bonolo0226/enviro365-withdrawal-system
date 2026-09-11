package com.enviro.assessment.junior.bonolo.service;

import com.enviro.assessment.junior.bonolo.dto.PortfolioDTO;
import com.enviro.assessment.junior.bonolo.dto.WithdrawalRequestDTO;
import com.enviro.assessment.junior.bonolo.dto.WithdrawalResponseDTO;
import com.enviro.assessment.junior.bonolo.model.Investor;
import com.enviro.assessment.junior.bonolo.model.Product;
import com.enviro.assessment.junior.bonolo.model.WithdrawalNotice;
import com.enviro.assessment.junior.bonolo.repository.InvestorRepository;
import com.enviro.assessment.junior.bonolo.repository.ProductRepository;
import com.enviro.assessment.junior.bonolo.repository.WithdrawalNoticeRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WithdrawalService {

    private final InvestorRepository investorRepository;
    private final ProductRepository productRepository;
    private final WithdrawalNoticeRepository withdrawalNoticeRepository;

    public WithdrawalService(InvestorRepository investorRepository,
                              ProductRepository productRepository,
                              WithdrawalNoticeRepository withdrawalNoticeRepository) {
        this.investorRepository = investorRepository;
        this.productRepository = productRepository;
        this.withdrawalNoticeRepository = withdrawalNoticeRepository;
    }

    // --- View portfolio ---
    public PortfolioDTO getPortfolio(Long investorId) {
        Investor investor = investorRepository.findById(investorId)
                .orElseThrow(() -> new IllegalArgumentException("Investor not found with id: " + investorId));

        List<Product> products = productRepository.findByInvestorId(investorId);

        List<PortfolioDTO.ProductDTO> productDTOs = products.stream()
                .map(p -> new PortfolioDTO.ProductDTO(p.getId(), p.getProductName(), p.getProductType(), p.getBalance()))
                .collect(Collectors.toList());

        return new PortfolioDTO(investor.getId(), investor.getName(), investor.getAge(), productDTOs);
    }

    // --- Submit withdrawal (this is where the business rules live) ---
    public WithdrawalResponseDTO submitWithdrawal(WithdrawalRequestDTO request) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + request.getProductId()));

        Investor investor = product.getInvestor();
        double amount = request.getAmount();

        // Rule 1: Retirement withdrawals only allowed if age > 65
        if ("RETIREMENT".equalsIgnoreCase(product.getProductType()) && investor.getAge() <= 65) {
            throw new IllegalStateException("Retirement withdrawals are only allowed for investors older than 65.");
        }

        // Rule 2: Withdrawal must not exceed balance
        if (amount > product.getBalance()) {
            throw new IllegalStateException("Withdrawal amount exceeds available balance.");
        }

        // Rule 3: Withdrawal must not exceed 90% of balance
        double maxAllowed = product.getBalance() * 0.9;
        if (amount > maxAllowed) {
            throw new IllegalStateException("Withdrawal amount exceeds 90% of the balance limit.");
        }

        // All rules passed — process the withdrawal
        product.setBalance(product.getBalance() - amount);
        productRepository.save(product);

        WithdrawalNotice notice = new WithdrawalNotice(null, amount, LocalDate.now(), "APPROVED", product);
        withdrawalNoticeRepository.save(notice);

        return new WithdrawalResponseDTO(
                notice.getId(),
                product.getProductName(),
                amount,
                notice.getStatus(),
                notice.getRequestDate(),
                product.getBalance()
        );
    }

    // --- Withdrawal history for a product ---
    public List<WithdrawalNotice> getWithdrawalHistory(Long productId) {
        return withdrawalNoticeRepository.findByProductId(productId);
    }

    // --- CSV export, with optional date filtering ---
    public String exportWithdrawalsAsCsv(Long investorId, LocalDate fromDate, LocalDate toDate) {
        List<Product> products = productRepository.findByInvestorId(investorId);

        StringBuilder csv = new StringBuilder();
        csv.append("Product Name,Amount,Status,Request Date,Remaining Balance\n");

        for (Product product : products) {
            List<WithdrawalNotice> notices = withdrawalNoticeRepository.findByProductId(product.getId());
            for (WithdrawalNotice notice : notices) {
                boolean afterFrom = (fromDate == null) || !notice.getRequestDate().isBefore(fromDate);
                boolean beforeTo = (toDate == null) || !notice.getRequestDate().isAfter(toDate);

                if (afterFrom && beforeTo) {
                    csv.append(product.getProductName()).append(",")
                       .append(notice.getAmount()).append(",")
                       .append(notice.getStatus()).append(",")
                       .append(notice.getRequestDate()).append(",")
                       .append(product.getBalance()).append("\n");
                }
            }
        }

        return csv.toString();
    }
}