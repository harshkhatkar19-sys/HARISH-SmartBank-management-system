package com.bank.controller;

import com.bank.model.Account;
import com.bank.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class AccountController {

    @Autowired
    private AccountService service;

    // ================= DTOs =================
    static class TransactionRequest {
        public String accNo;
        public double amount;
    }

    static class TransferRequest {
        public String fromAcc;
        public String toAcc;
        public double amount;
    }

    static class LoginRequest {
        public String username;
        public String password;
    }

    static class CreateAccountRequest {
        public String holderName;
        public String username;
        public String password;
        public double balance;
        public String accountType; // SAVINGS / CURRENT
        public String aadhaarNumber;
        public String phoneNumber;
        public String fatherName;
        public String motherName;
        public String dob;
    }

    // ================= CREATE ACCOUNT (ADDED) =================
    @PostMapping("/create-account")
    public ResponseEntity<String> createAccount(@RequestBody CreateAccountRequest req) {
        try {
            java.time.LocalDate birthDate = null;
            if (req.dob != null && !req.dob.isEmpty()) {
                birthDate = java.time.LocalDate.parse(req.dob);
            }

            Account account;
            if ("CURRENT".equalsIgnoreCase(req.accountType)) {
                account = new com.bank.model.CurrentAccount(
                        req.holderName, req.username, req.password, req.balance,
                        req.aadhaarNumber, req.phoneNumber, req.fatherName, req.motherName, birthDate
                );
            } else {
                account = new com.bank.model.SavingsAccount(
                        req.holderName, req.username, req.password, req.balance,
                        req.aadhaarNumber, req.phoneNumber, req.fatherName, req.motherName, birthDate
                );
            }

            service.createAccount(account, req.accountType);
            return ResponseEntity.ok("Account created successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Account creation failed: " + e.getMessage());
        }
    }

    // ================= LOGIN =================
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        Account account = service.login(req.username, req.password);

        if (account == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid username or password");
        }

        return ResponseEntity.ok(account);
    }

    // ================= ADMIN LOGIN (ADDED) =================
    @PostMapping("/admin/login")
    public ResponseEntity<String> adminLogin(@RequestBody LoginRequest req) {

        boolean isAdmin = service.adminLogin(req.username, req.password);

        if (isAdmin) {
            return ResponseEntity.ok("ADMIN LOGIN SUCCESS");
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Invalid admin credentials");
    }

    // ================= DEPOSIT =================
    @PostMapping("/deposit")
    public ResponseEntity<String> deposit(@RequestBody TransactionRequest req) {
        try {
            if (service.deposit(req.accNo, req.amount)) {
                return ResponseEntity.ok("Deposit successful");
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ================= WITHDRAW =================
    @PostMapping("/withdraw")
    public ResponseEntity<String> withdraw(@RequestBody TransactionRequest req) {
        try {
            if (service.withdraw(req.accNo, req.amount)) {
                return ResponseEntity.ok("Withdraw successful");
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Insufficient balance or account not found");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ================= BALANCE =================
    @GetMapping("/balance")
    public double getBalance(@RequestParam String accNo) {
        return service.getBalance(accNo);
    }

    // ================= TRANSFER =================
    @PostMapping("/transfer")
    public ResponseEntity<String> transfer(@RequestBody TransferRequest req) {
        String result = service.transfer(req.fromAcc, req.toAcc, req.amount);

        if ("SUCCESS".equalsIgnoreCase(result)) {
            return ResponseEntity.ok("Transfer successful");
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    // ================= ADMIN APIs =================
    @GetMapping("/admin/total-accounts")
    public long totalAccounts() {
        return service.getTotalAccounts();
    }

    @GetMapping("/admin/total-balance")
    public double totalBalance() {
        return service.getTotalBalance();
    }

    @DeleteMapping("/admin/delete/{accNo}")
    public ResponseEntity<String> delete(@PathVariable String accNo) {
        if (service.deleteAccount(accNo)) {
            return ResponseEntity.ok("Deleted");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found");
    }

    @PostMapping("/admin/block/{accNo}")
    public ResponseEntity<String> block(@PathVariable String accNo) {
        if (service.blockAccount(accNo)) {
            return ResponseEntity.ok("Blocked");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found");
    }

    @PostMapping("/admin/unblock/{accNo}")
    public ResponseEntity<String> unblock(@PathVariable String accNo) {
        if (service.unblockAccount(accNo)) {
            return ResponseEntity.ok("Unblocked");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found");
    }

    @GetMapping("/transactions")
    public ResponseEntity<?> getTransactions(@RequestParam String accNo) {
        return ResponseEntity.ok(service.getTransactions(accNo));
    }

    @GetMapping("/admin/search")
    public ResponseEntity<?> searchAccounts(@RequestParam String name) {
        List<java.util.Map<String, String>> results = new java.util.ArrayList<>();
        String query = "SELECT account_number, holder_name FROM accounts WHERE holder_name LIKE ?";
        try (java.sql.Connection con = com.bank.utils.DBConnection.getConnection();
             java.sql.PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, "%" + name + "%");
            java.sql.ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                java.util.Map<String, String> map = new java.util.HashMap<>();
                map.put("accountNumber", rs.getString("account_number"));
                map.put("holderName", rs.getString("holder_name"));
                results.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
        return ResponseEntity.ok(results);
    }
}