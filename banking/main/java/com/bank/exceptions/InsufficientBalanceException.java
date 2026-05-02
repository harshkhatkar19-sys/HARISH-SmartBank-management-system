package com.babk.exceptions;

/**
 * Custom Exception for insufficient balance
 * Demonstrates:
 * - Custom Exception
 * - Inheritance
 * - Constructor Overloading
 */
public class InsufficientBalanceException extends Exception {

    private double currentBalance;
    private double requestedAmount;

    // ================= DEFAULT CONSTRUCTOR =================
    public InsufficientBalanceException() {
        super("Insufficient balance in the account!");
    }

    // ================= CUSTOM MESSAGE =================
    public InsufficientBalanceException(String message) {
        super(message);
    }

    // ================= DETAILED CONSTRUCTOR =================
    public InsufficientBalanceException(double currentBalance, double requestedAmount) {
        super("Insufficient balance! Available: " + currentBalance +
              ", Requested: " + requestedAmount);
        this.currentBalance = currentBalance;
        this.requestedAmount = requestedAmount;
    }

    // ================= GETTERS =================
    public double getCurrentBalance() {
        return currentBalance;
    }

    public double getRequestedAmount() {
        return requestedAmount;
    }
}