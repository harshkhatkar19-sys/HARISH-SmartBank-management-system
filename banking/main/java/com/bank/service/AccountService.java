package com.bank.service;

import com.bank.model.Account;
import com.bank.model.SavingsAccount;
import com.bank.model.CurrentAccount;
import com.bank.model.TransactionModel;
import com.bank.utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountService {

    // ================= CREATE ACCOUNT =================
    public void createAccount(Account acc, String type) {

        String query = "INSERT INTO accounts (account_number, holder_name, username, password, balance, account_type) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, acc.getAccountNumber());
            ps.setString(2, acc.getHolderName());
            ps.setString(3, acc.getUsername());
            ps.setString(4, acc.getPassword());
            ps.setDouble(5, acc.getBalance());
            ps.setString(6, type);

            ps.executeUpdate();
            System.out.println("✅ Account created!");

        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("❌ Username already exists!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= LOGIN =================
    public Account login(String username, String password) {

        String query = "SELECT * FROM accounts WHERE LOWER(TRIM(username))=LOWER(TRIM(?)) AND TRIM(password)=TRIM(?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                String type = rs.getString("account_type");
                Account acc;

                if (type.equalsIgnoreCase("SAVINGS")) {
                    acc = new SavingsAccount(
                            rs.getString("holder_name"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getDouble("balance")
                    );
                } else {
                    acc = new CurrentAccount(
                            rs.getString("holder_name"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getDouble("balance")
                    );
                }

                // 🔥 IMPORTANT FIX
                acc.setAccountNumber(rs.getString("account_number"));
                System.out.println("DB Account No: " + rs.getString("account_number"));

                return acc;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // ================= ADD TRANSACTION =================
    private void addTransaction(String accNo, String type, double amount) {

        String query = "INSERT INTO transactions (account_number, type, amount) VALUES (?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, accNo);
            ps.setString(2, type);
            ps.setDouble(3, amount);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= DEPOSIT =================
    public void deposit(String accNo, double amount) {

        if (amount <= 0) {
            System.out.println("❌ Invalid amount!");
            return;
        }

        String query = "UPDATE accounts SET balance = balance + ? WHERE account_number=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setDouble(1, amount);
            ps.setString(2, accNo);

            int rows = ps.executeUpdate();
System.out.println("Depositing to: " + accNo);
            if (rows > 0) {
                addTransaction(accNo, "DEPOSIT", amount);
                System.out.println("✅ Deposit successful!");
            } else {
                System.out.println("❌ Account not found!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= WITHDRAW =================
    public void withdraw(String accNo, double amount) {

        if (amount <= 0) {
            System.out.println("❌ Invalid amount!");
            return;
        }

        String query = "UPDATE accounts SET balance = balance - ? WHERE account_number=? AND balance >= ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setDouble(1, amount);
            ps.setString(2, accNo);
            ps.setDouble(3, amount);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                addTransaction(accNo, "WITHDRAW", amount);
               
                System.out.println("✅ Withdraw successful!");
            } else {
                System.out.println("❌ Insufficient balance or account not found!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= CHECK BALANCE =================
    public double getBalance(String accNo) {

        String query = "SELECT balance FROM accounts WHERE account_number=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, accNo);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getDouble("balance");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    // ================= TRANSFER MONEY =================
public void transferMoney(String fromAcc, String toAcc, double amount) {

    if (amount <= 0) {
        System.out.println("❌ Invalid amount!");
        return;
    }

    String withdrawQuery = "UPDATE accounts SET balance = balance - ? WHERE account_number=? AND balance >= ?";
    String depositQuery = "UPDATE accounts SET balance = balance + ? WHERE account_number=?";

    try (Connection con = DBConnection.getConnection()) {

        // 🔥 START TRANSACTION
        con.setAutoCommit(false);

        // Step 1: Withdraw
        try (PreparedStatement ps1 = con.prepareStatement(withdrawQuery)) {

            ps1.setDouble(1, amount);
            ps1.setString(2, fromAcc);
            ps1.setDouble(3, amount);

            int rows1 = ps1.executeUpdate();

            if (rows1 == 0) {
                System.out.println("❌ Transfer failed (insufficient balance or sender not found)");
                con.rollback();
                return;
            }
        }

        // Step 2: Deposit
        try (PreparedStatement ps2 = con.prepareStatement(depositQuery)) {

            ps2.setDouble(1, amount);
            ps2.setString(2, toAcc);

            int rows2 = ps2.executeUpdate();

            if (rows2 == 0) {
                System.out.println("❌ Receiver account not found!");
                con.rollback();
                return;
            }
        }

        // Step 3: Save transactions
        addTransaction(fromAcc, "TRANSFER_SENT", amount);
        addTransaction(toAcc, "TRANSFER_RECEIVED", amount);

        // ✅ COMMIT
        con.commit();

        // 🔥 PRINT DETAILS
        System.out.println("✅ Transfer successful!");
        System.out.println("From Account: " + fromAcc);
        System.out.println("To Account: " + toAcc);
        System.out.println("Amount: " + amount);

    } catch (Exception e) {
        e.printStackTrace();
    }
}
    // ================= CHANGE PASSWORD =================
public void changePassword(String username, String oldPassword, String newPassword) {

    // Step 1: Check if user exists with old password
    String checkQuery = "SELECT * FROM accounts WHERE username=? AND password=?";

    // Step 2: Update password
    String updateQuery = "UPDATE accounts SET password=? WHERE username=?";

    try (Connection con = DBConnection.getConnection();
         PreparedStatement checkPs = con.prepareStatement(checkQuery);
         PreparedStatement updatePs = con.prepareStatement(updateQuery)) {

        // 🔍 Verify old password
        checkPs.setString(1, username);
        checkPs.setString(2, oldPassword);

        ResultSet rs = checkPs.executeQuery();

        if (rs.next()) {

            // ✅ Update password
            updatePs.setString(1, newPassword);
            updatePs.setString(2, username);

            int rows = updatePs.executeUpdate();

            if (rows > 0) {
                System.out.println("✅ Password changed successfully!");
            } else {
                System.out.println("❌ Failed to update password!");
            }

        } else {
            System.out.println("❌ Incorrect old password!");
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
}

    // ================= GET TRANSACTION HISTORY =================
    public List<TransactionModel> getTransactions(String accNo) {

        List<TransactionModel> list = new ArrayList<>();

        String query = "SELECT * FROM transactions WHERE account_number=? ORDER BY date DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, accNo);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new TransactionModel(
                        rs.getString("account_number"),
                        rs.getString("type"),
                        rs.getDouble("amount"),
                        rs.getTimestamp("date").toString()
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}
