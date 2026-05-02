package com.bank.model;

import java.io.Serializable;

/**
 * TransactionModel Class
 * Represents a transaction record
 * Demonstrates:
 * - Encapsulation
 * - Serializable (for file handling / future use)
 */
public class TransactionModel implements Serializable {

    // ================= VARIABLES =================
    private int id;
    private String accountNumber;
    private String type;       // DEPOSIT / WITHDRAW
    private double amount;
    private String date;

    // ================= CONSTRUCTORS =================

    // Default constructor
    public TransactionModel() {}

    // Parameterized constructor
    public TransactionModel(String accountNumber, String type, double amount, String date) {
        this.accountNumber = accountNumber;
        this.type = type;
        this.amount = amount;
        this.date = date;
    }

    // ================= GETTERS =================

    public int getId() {
        return id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }

    // ================= SETTERS =================

    public void setId(int id) {
        this.id = id;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setDate(String date) {
        this.date = date;
    }

    // ================= TO STRING =================

    @Override
    public String toString() {
        return "Transaction{" +
                "Account='" + accountNumber + '\'' +
                ", Type='" + type + '\'' +
                ", Amount=" + amount +
                ", Date='" + date + '\'' +
                '}';
    }
}
