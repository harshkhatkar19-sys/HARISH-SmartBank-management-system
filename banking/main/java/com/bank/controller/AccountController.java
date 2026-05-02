package com.bank.controller;

import com.bank.model.Account;
import com.bank.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class AccountController {

    @Autowired
    private AccountService service;

    static class LoginRequest {
        public String username;
        public String password;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        Account account = service.login(req.username, req.password);
        if (account == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
        return ResponseEntity.ok(account);
    }

    @PostMapping("/deposit")
    public void deposit(@RequestParam String accNo, @RequestParam double amount) {
        service.deposit(accNo, amount);
    }

    @PostMapping("/withdraw")
    public void withdraw(@RequestParam String accNo, @RequestParam double amount) {
        service.withdraw(accNo, amount);
    }

    @GetMapping("/balance")
    public double getBalance(@RequestParam String accNo) {
        return service.getBalance(accNo);
    }

    @PostMapping("/transfer")
    public void transfer(@RequestParam String fromAcc,
                         @RequestParam String toAcc,
                         @RequestParam double amount) {
        service.transfer(fromAcc, toAcc, amount);
    }
}
