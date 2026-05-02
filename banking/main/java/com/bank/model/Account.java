package com.bank.model;

import java.io.Serializable;
import java.util.UUID;

/**
 * Abstract Account Class
 * Demonstrates:
 * - Abstraction
 * - Encapsulation
 * - Inheritance (used by child classes)
 * - Serializable (for file handling)
 */
public abstract class Account implements Serializable {

    // ================= STATIC =================
    private static int totalAccounts = 0; // count total accounts

    // ================= FINAL =================
    protected static final String BANK_NAME = "HarishSmartBank";

    // ================= PRIVATE FIELDS =================
    private  String accountNumber;   // unique ID
    private String holderName;
    private String username;
    private String password;
    protected double balance;

    // ================= CONSTRUCTORS =================

    // Default constructor
    public Account() {
        this.accountNumber = generateAccountNumber();
        this.holderName = "Unknown";
        this.username = "user";
        this.password = "1234";
        this.balance = 0.0;
        totalAccounts++;
    }

    // Parameterized constructor
    public Account(String holderName, String username, String password, double balance) {
        this.accountNumber = generateAccountNumber();
        this.holderName = holderName;
        this.username = username;
        this.password = password;
        this.balance = balance;
        totalAccounts++;
    }

    // ================= UNIQUE ACCOUNT NUMBER =================
    private String generateAccountNumber() {
        return "ACC-" + UUID.randomUUID().toString().substring(0, 8);
    }

    // ================= LOGIN METHOD =================
    public boolean login(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }

    // ================= GETTERS =================
    public String getAccountNumber() {
        return accountNumber;
    }

    public String getHolderName() {
        return holderName;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {   // needed for JDBC (can remove later for security)
        return password;
    }

    public double getBalance() {
        return balance;
    }

    // ================= SETTERS =================
    public void setHolderName(String holderName) {
        this.holderName = holderName;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public void setAccountNumber(String accountNumber) {
    this.accountNumber = accountNumber;
}

    // ================= STATIC METHOD =================
    public static int getTotalAccounts() {
        return totalAccounts;
    }

    // ================= ABSTRACT METHODS =================
    public abstract void deposit(double amount);

    public abstract void withdraw(double amount) throws Exception;

    // ================= METHOD OVERLOADING =================
    public void deposit(double amount, String note) {
        deposit(amount);
        System.out.println("Note: " + note);
    }

    // ================= SYNCHRONIZATION =================
    public synchronized void safeDeposit(double amount) {
        if (amount > 0) {
            balance += amount;
            System.out.println(Thread.currentThread().getName() +
                    " deposited: " + amount);
        }
    }

    public synchronized void safeWithdraw(double amount) throws Exception {
        if (amount > balance) {
            throw new Exception("Insufficient Balance!");
        }
        balance -= amount;
        System.out.println(Thread.currentThread().getName() +
                " withdrew: " + amount);
    }

    // ================= DISPLAY =================
    public void displayAccountInfo() {
        System.out.println("========== ACCOUNT DETAILS ==========");
        System.out.println("Bank Name: " + BANK_NAME);
        System.out.println("Account No: " + accountNumber);
        System.out.println("Holder Name: " + holderName);
        System.out.println("Username: " + username);
        System.out.println("Balance: " + balance);
        System.out.println("====================================");
    }

    // ================= STRING HANDLING =================
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Account[");
        sb.append("AccountNo=").append(accountNumber).append(", ");
        sb.append("Name=").append(holderName).append(", ");
        sb.append("Balance=").append(balance);
        sb.append("]");
        return sb.toString();
    }

    // ================= FINAL METHOD =================
    public final void showBankPolicy() {
        System.out.println("Policy: Maintain minimum balance.");
    }
}