
package com.bank.model;

import java.io.Serializable;
import java.util.UUID;
import java.time.LocalDate;

/**
 * Abstract Account Class
 * Demonstrates:
 * - Abstraction
 * - Encapsulation
 * - Inheritance
 * - Serializable
 */
public abstract class Account implements Serializable {

    // ================= STATIC =================
    private static int totalAccounts = 0;

    // ================= FINAL =================
    protected static final String BANK_NAME = "HarishSmartBank";

    // ================= PRIVATE FIELDS =================
    private String accountNumber;
    private String holderName;
    private String username;
    private String password;
    protected double balance;

    // 🔥 NEW FIELDS
    private String aadhaarNumber;
    private String phoneNumber;
    private String fatherName;
    private String motherName;
    private LocalDate dateOfBirth;

    private String role;   // USER / ADMIN
private String status; // ACTIVE / BLOCKED

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

    // Parameterized constructor (updated)
    public Account(String holderName, String username, String password,
                   double balance, String aadhaarNumber, String phoneNumber,
                   String fatherName, String motherName, LocalDate dateOfBirth) {

        this.accountNumber = generateAccountNumber();
        this.holderName = holderName;
        this.username = username;
        this.password = password;
        this.balance = balance;
        this.aadhaarNumber = aadhaarNumber;
        this.phoneNumber = phoneNumber;
        this.fatherName = fatherName;
        this.motherName = motherName;
        this.dateOfBirth = dateOfBirth;

        totalAccounts++;
    }

    // ================= ACCOUNT NUMBER =================
    private String generateAccountNumber() {
        return "ACC-" + UUID.randomUUID().toString().substring(0, 8);
    }

    // ================= LOGIN =================
    public boolean login(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }

    // ================= GETTERS =================
    public String getAccountNumber() { return accountNumber; }
    public String getHolderName() { return holderName; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public double getBalance() { return balance; }

    public String getAadhaarNumber() { return aadhaarNumber; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getFatherName() { return fatherName; }
    public String getMotherName() { return motherName; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }

    public String getRole() { return role; }
public void setRole(String role) { this.role = role; }

public String getStatus() { return status; }
public void setStatus(String status) { this.status = status; }

    // ================= SETTERS =================
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    public void setHolderName(String holderName) { this.holderName = holderName; }
    public void setPassword(String password) { this.password = password; }

    public void setAadhaarNumber(String aadhaarNumber) {
        if (aadhaarNumber != null && aadhaarNumber.length() == 12) {
            this.aadhaarNumber = aadhaarNumber;
        } else {
            throw new IllegalArgumentException("Aadhaar must be 12 digits");
        }
    }

    public void setPhoneNumber(String phoneNumber) {
        if (phoneNumber != null && phoneNumber.length() == 10) {
            this.phoneNumber = phoneNumber;
        } else {
            throw new IllegalArgumentException("Phone must be 10 digits");
        }
    }

    public void setFatherName(String fatherName) { this.fatherName = fatherName; }
    public void setMotherName(String motherName) { this.motherName = motherName; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    // ================= STATIC =================
    public static int getTotalAccounts() {
        return totalAccounts;
    }

    // ================= ABSTRACT METHODS =================
    public abstract void deposit(double amount);
    public abstract void withdraw(double amount) throws Exception;

    // ================= OVERLOADING =================
    public void deposit(double amount, String note) {
        deposit(amount);
        System.out.println("Note: " + note);
    }

    // ================= THREAD SAFE =================
    public synchronized void safeDeposit(double amount) {
        if (amount > 0) {
            balance += amount;
            System.out.println(Thread.currentThread().getName() + " deposited: " + amount);
        }
    }

    public synchronized void safeWithdraw(double amount) throws Exception {
        if (amount > balance) {
            throw new Exception("Insufficient Balance!");
        }
        balance -= amount;
        System.out.println(Thread.currentThread().getName() + " withdrew: " + amount);
    }

    // ================= DISPLAY =================
    public void displayAccountInfo() {
        System.out.println("========== ACCOUNT DETAILS ==========");
        System.out.println("Bank Name: " + BANK_NAME);
        System.out.println("Account No: " + accountNumber);
        System.out.println("Holder Name: " + holderName);
        System.out.println("Username: " + username);
        System.out.println("Balance: " + balance);
        System.out.println("Phone: " + phoneNumber);
        System.out.println("Aadhaar: " + aadhaarNumber);
        System.out.println("Father Name: " + fatherName);
        System.out.println("Mother Name: " + motherName);
        System.out.println("DOB: " + dateOfBirth);
        System.out.println("====================================");
    }

    // ================= TO STRING =================
    @Override
    public String toString() {
        return "Account[" +
                "AccountNo=" + accountNumber +
                ", Name=" + holderName +
                ", Balance=" + balance +
                ", Phone=" + phoneNumber +
                "]";
    }

    // ================= FINAL METHOD =================
    public final void showBankPolicy() {
        System.out.println("Policy: Maintain minimum balance.");
    }
}

