
package com.bank.model;

import com.bank.exceptions.InsufficientBalanceException;
import java.time.LocalDate;

/**
 * CurrentAccount class
 * Demonstrates:
 * - Inheritance
 * - Method Overriding
 * - Overdraft feature
 */
public class CurrentAccount extends Account {

    // Overdraft limit
    private static final double OVERDRAFT_LIMIT = 5000.0;

    // ================= CONSTRUCTOR =================
    public CurrentAccount(String holderName, String username, String password,
                          double balance,
                          String aadhaarNumber, String phoneNumber,
                          String fatherName, String motherName,
                          LocalDate dateOfBirth) {

        super(holderName, username, password, balance,
              aadhaarNumber, phoneNumber,
              fatherName, motherName, dateOfBirth);
    }

    // Optional default constructor (useful for Spring/JPA later)
    public CurrentAccount() {
        super();
    }

    // ================= DEPOSIT =================
    @Override
    public void deposit(double amount) {
        if (amount <= 0) {
            System.out.println("❌ Invalid deposit amount!");
            return;
        }

        balance += amount;
        System.out.println("✅ Deposited: " + amount);
    }

    // ================= WITHDRAW =================
    @Override
    public void withdraw(double amount) throws InsufficientBalanceException {

        if (amount <= 0) {
            System.out.println("❌ Invalid withdrawal amount!");
            return;
        }

        // Overdraft logic
        if (balance - amount < -OVERDRAFT_LIMIT) {
            throw new InsufficientBalanceException(
                "Overdraft limit exceeded! Max allowed: " + OVERDRAFT_LIMIT
            );
        }

        balance -= amount;
        System.out.println("✅ Withdrawn: " + amount);
    }

    // ================= DISPLAY =================
    @Override
    public void displayAccountInfo() {
        super.displayAccountInfo();
        System.out.println("Account Type: CURRENT");
        System.out.println("Overdraft Limit: " + OVERDRAFT_LIMIT);
    }

    // ================= TO STRING =================
    @Override
    public String toString() {
        return super.toString() + " [CURRENT ACCOUNT]";
    }
}
