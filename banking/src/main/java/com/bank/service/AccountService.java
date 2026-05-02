package com.bank.service;

import com.bank.model.Account;
import com.bank.model.CurrentAccount;
import com.bank.model.SavingsAccount;
import com.bank.model.TransactionModel;
import com.bank.utils.DBConnection;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class AccountService {

    // ================= CREATE ACCOUNT =================
    public boolean createAccount(Account acc, String type) throws Exception {
        String query = "INSERT INTO accounts " +
                "(account_number, holder_name, username, password, balance, account_type, " +
                "aadhaar_number, phone_number, father_name, mother_name, dob, status, role) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 'ACTIVE', 'USER')";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, acc.getAccountNumber());
            ps.setString(2, acc.getHolderName());
            ps.setString(3, acc.getUsername());
            ps.setString(4, acc.getPassword());
            ps.setDouble(5, acc.getBalance());
            ps.setString(6, type);
            ps.setString(7, acc.getAadhaarNumber());
            ps.setString(8, acc.getPhoneNumber());
            ps.setString(9, acc.getFatherName());
            ps.setString(10, acc.getMotherName());

            if (acc.getDateOfBirth() != null) {
                ps.setDate(11, Date.valueOf(acc.getDateOfBirth()));
            } else {
                ps.setNull(11, Types.DATE);
            }

            return ps.executeUpdate() > 0;

        } catch (SQLIntegrityConstraintViolationException e) {
            throw new Exception("Username or Account Number already exists");
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
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

                String status = rs.getString("status");
                if ("BLOCKED".equalsIgnoreCase(status)) {
                    return null;
                }

                String type = rs.getString("account_type");
                String role = rs.getString("role");

                Date dob = rs.getDate("dob");
                LocalDate localDob = (dob != null) ? dob.toLocalDate() : null;

                Account acc;

                if ("SAVINGS".equalsIgnoreCase(type)) {
                    acc = new SavingsAccount(
                            rs.getString("holder_name"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getDouble("balance"),
                            rs.getString("aadhaar_number"),
                            rs.getString("phone_number"),
                            rs.getString("father_name"),
                            rs.getString("mother_name"),
                            localDob
                    );
                } else {
                    acc = new CurrentAccount(
                            rs.getString("holder_name"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getDouble("balance"),
                            rs.getString("aadhaar_number"),
                            rs.getString("phone_number"),
                            rs.getString("father_name"),
                            rs.getString("mother_name"),
                            localDob
                    );
                }

                acc.setAccountNumber(rs.getString("account_number"));
                acc.setRole(role);
                acc.setStatus(status);

                return acc;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // ================= ADMIN LOGIN CHECK =================
    public boolean adminLogin(String username, String password) {

        String query = "SELECT role FROM accounts WHERE username=? AND password=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return "ADMIN".equalsIgnoreCase(rs.getString("role"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // ================= ADD TRANSACTION =================
    private void addTransaction(Connection con, String accNo, String type, double amount) {
        String query = "INSERT INTO transactions (account_number, type, amount) VALUES (?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, accNo);
            ps.setString(2, type);
            ps.setDouble(3, amount);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= DEPOSIT =================
    public boolean deposit(String accNo, double amount) {

        if (amount <= 0) throw new IllegalArgumentException("Invalid amount");

        String query = "UPDATE accounts SET balance = balance + ? WHERE TRIM(account_number)=TRIM(?) AND UPPER(status)='ACTIVE'";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setDouble(1, amount);
            ps.setString(2, accNo);

            if (ps.executeUpdate() > 0) {
                addTransaction(con, accNo, "DEPOSIT", amount);
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // ================= WITHDRAW =================
    public boolean withdraw(String accNo, double amount) {

        if (amount <= 0) throw new IllegalArgumentException("Invalid amount");

        String query = "UPDATE accounts SET balance = balance - ? WHERE TRIM(account_number)=TRIM(?) AND balance >= ? AND UPPER(status)='ACTIVE'";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setDouble(1, amount);
            ps.setString(2, accNo);
            ps.setDouble(3, amount);

            if (ps.executeUpdate() > 0) {
                addTransaction(con, accNo, "WITHDRAW", amount);
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // ================= BALANCE =================
    public double getBalance(String accNo) {

        String query = "SELECT balance FROM accounts WHERE TRIM(account_number)=TRIM(?)";

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

    // ================= TRANSFER =================
    public String transfer(String fromAcc, String toAcc, double amount) {

        if (amount <= 0) return "Invalid amount";
        if (fromAcc.trim().equalsIgnoreCase(toAcc.trim())) return "Cannot transfer to the same account";

        try (Connection con = DBConnection.getConnection()) {
            
            // 1. Check source account
            String checkSource = "SELECT balance, status FROM accounts WHERE TRIM(account_number)=TRIM(?)";
            try (PreparedStatement ps = con.prepareStatement(checkSource)) {
                ps.setString(1, fromAcc);
                ResultSet rs = ps.executeQuery();
                if (!rs.next()) return "Source account not found";
                if (!"ACTIVE".equalsIgnoreCase(rs.getString("status"))) return "Source account is blocked";
                if (rs.getDouble("balance") < amount) return "Insufficient balance";
            }

            // 2. Check destination account
            String checkDest = "SELECT status FROM accounts WHERE TRIM(account_number)=TRIM(?)";
            try (PreparedStatement ps = con.prepareStatement(checkDest)) {
                ps.setString(1, toAcc);
                ResultSet rs = ps.executeQuery();
                if (!rs.next()) return "Receiver account not found";
                if (!"ACTIVE".equalsIgnoreCase(rs.getString("status"))) return "Receiver account is blocked/inactive";
            }

            con.setAutoCommit(false);

            String withdrawQuery = "UPDATE accounts SET balance = balance - ? WHERE TRIM(account_number)=TRIM(?)";
            String depositQuery = "UPDATE accounts SET balance = balance + ? WHERE TRIM(account_number)=TRIM(?)";

            try {
                try (PreparedStatement ps1 = con.prepareStatement(withdrawQuery)) {
                    ps1.setDouble(1, amount);
                    ps1.setString(2, fromAcc);
                    ps1.executeUpdate();
                }

                try (PreparedStatement ps2 = con.prepareStatement(depositQuery)) {
                    ps2.setDouble(1, amount);
                    ps2.setString(2, toAcc);
                    ps2.executeUpdate();
                }

                addTransaction(con, fromAcc, "TRANSFER_SENT", amount);
                addTransaction(con, toAcc, "TRANSFER_RECEIVED", amount);

                con.commit();
                return "SUCCESS";

            } catch (Exception e) {
                con.rollback();
                e.printStackTrace();
                return "Transfer failed due to system error";
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "Connection error";
        }
    }

    // ================= TRANSACTIONS =================
    public List<TransactionModel> getTransactions(String accNo) {

        List<TransactionModel> list = new ArrayList<>();
        String query = "SELECT * FROM transactions WHERE TRIM(account_number)=TRIM(?) ORDER BY `date` DESC";

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

    // ================= ADMIN: TOTAL ACCOUNTS =================
    public long getTotalAccounts() {

        String query = "SELECT COUNT(*) FROM accounts";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getLong(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    // ================= ADMIN: TOTAL BANK BALANCE =================
    public double getTotalBalance() {

        String query = "SELECT SUM(balance) FROM accounts";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getDouble(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    // ================= ADMIN: DELETE ACCOUNT =================
    public boolean deleteAccount(String accNo) {

        String query = "DELETE FROM accounts WHERE TRIM(account_number)=TRIM(?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, accNo);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // ================= ADMIN: BLOCK ACCOUNT =================
    public boolean blockAccount(String accNo) {

        String query = "UPDATE accounts SET status='BLOCKED' WHERE TRIM(account_number)=TRIM(?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, accNo);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // ================= ADMIN: UNBLOCK ACCOUNT =================
    public boolean unblockAccount(String accNo) {

        String query = "UPDATE accounts SET status='ACTIVE' WHERE TRIM(account_number)=TRIM(?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, accNo);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

}