package com.bank.controller;

import com.bank.model.Loan;
import com.bank.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/loans")
@CrossOrigin(origins = "*")
public class LoanController {

    @Autowired
    private LoanService loanService;

    @PostMapping("/apply")
    public String applyLoan(@RequestBody Loan loan) {
        if (loanService.applyLoan(loan)) {
            return "SUCCESS";
        }
        return "FAILED";
    }

    @GetMapping("/{accNo}")
    public List<Loan> getLoans(@PathVariable String accNo) {
        return loanService.getLoansByAccount(accNo);
    }

    @GetMapping("/all")
    public List<Loan> getAllLoans() {
        return loanService.getAllLoans();
    }

    @PutMapping("/status")
    public String updateStatus(@RequestBody Map<String, Object> payload) {
        int loanId = (int) payload.get("loanId");
        String status = (String) payload.get("status");
        if (loanService.updateLoanStatus(loanId, status)) {
            return "SUCCESS";
        }
        return "FAILED";
    }
}
