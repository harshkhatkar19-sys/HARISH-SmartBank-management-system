package com.bank.service;

import com.bank.model.Loan;
import com.bank.utils.DBConnection;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class LoanService {

    public boolean applyLoan(Loan loan) {
        String query = "INSERT INTO loans (account_number, amount, interest_rate, duration_months, loan_type) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, loan.getAccountNumber());
            ps.setDouble(2, loan.getAmount());
            ps.setDouble(3, loan.getInterestRate());
            ps.setInt(4, loan.getDurationMonths());
            ps.setString(5, loan.getLoanType());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Loan> getLoansByAccount(String accNo) {
        List<Loan> loans = new ArrayList<>();
        String query = "SELECT * FROM loans WHERE account_number = ? ORDER BY applied_date DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, accNo);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Loan loan = new Loan();
                loan.setId(rs.getInt("id"));
                loan.setAccountNumber(rs.getString("account_number"));
                loan.setAmount(rs.getDouble("amount"));
                loan.setInterestRate(rs.getDouble("interest_rate"));
                loan.setDurationMonths(rs.getInt("duration_months"));
                loan.setLoanType(rs.getString("loan_type"));
                loan.setStatus(rs.getString("status"));
                loan.setAppliedDate(rs.getTimestamp("applied_date").toString());
                loans.add(loan);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return loans;
    }

    public List<Loan> getAllLoans() {
        List<Loan> loans = new ArrayList<>();
        String query = "SELECT * FROM loans ORDER BY applied_date DESC";

        try (Connection con = DBConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Loan loan = new Loan();
                loan.setId(rs.getInt("id"));
                loan.setAccountNumber(rs.getString("account_number"));
                loan.setAmount(rs.getDouble("amount"));
                loan.setInterestRate(rs.getDouble("interest_rate"));
                loan.setDurationMonths(rs.getInt("duration_months"));
                loan.setLoanType(rs.getString("loan_type"));
                loan.setStatus(rs.getString("status"));
                loan.setAppliedDate(rs.getTimestamp("applied_date").toString());
                loans.add(loan);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return loans;
    }

    public boolean updateLoanStatus(int loanId, String status) {
        String query = "UPDATE loans SET status = ? WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, status);
            ps.setInt(2, loanId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
