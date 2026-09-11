package com.enviro.assessment.junior.bonolo.controller;

import com.enviro.assessment.junior.bonolo.dto.PortfolioDTO;
import com.enviro.assessment.junior.bonolo.dto.WithdrawalRequestDTO;
import com.enviro.assessment.junior.bonolo.dto.WithdrawalResponseDTO;
import com.enviro.assessment.junior.bonolo.model.WithdrawalNotice;
import com.enviro.assessment.junior.bonolo.service.WithdrawalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class WithdrawalController {

    private final WithdrawalService withdrawalService;

    public WithdrawalController(WithdrawalService withdrawalService) {
        this.withdrawalService = withdrawalService;
    }

    @GetMapping("/portfolio/{investorId}")
    public ResponseEntity<PortfolioDTO> getPortfolio(@PathVariable Long investorId) {
        return ResponseEntity.ok(withdrawalService.getPortfolio(investorId));
    }

    @PostMapping("/withdrawals")
    public ResponseEntity<WithdrawalResponseDTO> submitWithdrawal(@Valid @RequestBody WithdrawalRequestDTO request) {
        return ResponseEntity.ok(withdrawalService.submitWithdrawal(request));
    }

    @GetMapping("/withdrawals/product/{productId}")
    public ResponseEntity<List<WithdrawalNotice>> getWithdrawalHistory(@PathVariable Long productId) {
        return ResponseEntity.ok(withdrawalService.getWithdrawalHistory(productId));
    }

    @GetMapping("/withdrawals/export/{investorId}")
    public ResponseEntity<String> exportCsv(
            @PathVariable Long investorId,
            @RequestParam(required = false) LocalDate from,
            @RequestParam(required = false) LocalDate to) {

        String csv = withdrawalService.exportWithdrawalsAsCsv(investorId, from, to);

        return ResponseEntity.ok()
                .header("Content-Type", "text/csv")
                .header("Content-Disposition", "attachment; filename=\"withdrawal_statement.csv\"")
                .body(csv);
    }
}