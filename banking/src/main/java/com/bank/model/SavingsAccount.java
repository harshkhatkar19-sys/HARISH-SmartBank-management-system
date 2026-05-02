
package com.bank.model;

import com.bank.exceptions.InsufficientBalanceException;
import java.time.LocalDate;

/**
 * SavingsAccount class
 * Demonstrates:
 * - Inheritance
 * - Method Overriding (Polymorphism)
 */
public class SavingsAccount extends Account {

    // Minimum balance rule
    private static final double MIN_BALANCE = 1000.0;

    // ================= CONSTRUCTOR =================
    public SavingsAccount(String holderName, String username, String password,
                          double balance,
                          String aadhaarNumber, String phoneNumber,
                          String fatherName, String motherName,
                          LocalDate dateOfBirth) {

        super(holderName, username, password, balance,
              aadhaarNumber, phoneNumber,
              fatherName, motherName, dateOfBirth);
    }

    // Optional default constructor (important for Spring/JPA)
    public SavingsAccount() {
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

        if (balance - amount < MIN_BALANCE) {
            throw new InsufficientBalanceException(
                "Cannot withdraw! Minimum balance of " + MIN_BALANCE + " must be maintained."
            );
        }

        balance -= amount;
        System.out.println("✅ Withdrawn: " + amount);
    }

    // ================= DISPLAY =================
    @Override
    public void displayAccountInfo() {
        super.displayAccountInfo();
        System.out.println("Account Type: SAVINGS");
        System.out.println("Minimum Balance: " + MIN_BALANCE);
    }

    // ================= TO STRING =================
    @Override
    public String toString() {
        return super.toString() + " [SAVINGS ACCOUNT]";
    }
}

