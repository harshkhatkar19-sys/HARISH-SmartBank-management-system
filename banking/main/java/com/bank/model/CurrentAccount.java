package com.bank.model;

import exceptions.InsufficientBalanceException;

/**
 * CurrentAccount class
 * Inherits from Account
 * Demonstrates:
 * - Inheritance
 * - Method Overriding
 * - Overdraft feature
 */
public class CurrentAccount extends Account {

    // Overdraft limit (you can withdraw more than balance up to this limit)
    private static final double OVERDRAFT_LIMIT = 5000.0;

    // ================= CONSTRUCTOR =================
    public CurrentAccount(String holderName, String username, String password, double balance) {
        super(holderName, username, password, balance);
    }

    // ================= DEPOSIT =================
    @Override
    public void deposit(double amount) {
        if (amount <= 0) {
            System.out.println("Invalid deposit amount!");
            return;
        }

        balance += amount;
        System.out.println("✅ Deposited: " + amount);
    }

    // ================= WITHDRAW =================
    @Override
    public void withdraw(double amount) throws InsufficientBalanceException {

        if (amount <= 0) {
            System.out.println("Invalid withdrawal amount!");
            return;
        }

        // Allow overdraft
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
}