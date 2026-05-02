package com.bank.service;

import com.bank.model.Card;
import com.bank.utils.DBConnection;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class CardService {

    public boolean issueCard(String accNo, String holderName, String cardType) {
        // Check if ANY card already exists for this account
        String checkQuery = "SELECT COUNT(*) FROM cards WHERE account_number = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement checkPs = con.prepareStatement(checkQuery)) {
            
            checkPs.setString(1, accNo);
            ResultSet rs = checkPs.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return false; // Already has a card (Debit or Credit)
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        String cardNumber = generateCardNumber();
        String cvv = String.format("%03d", new Random().nextInt(1000));
        String pin = "1234"; // Default PIN
        String expiry = "12/28"; // Static expiry for demo

        String query = "INSERT INTO cards (card_number, account_number, holder_name, expiry_date, cvv, pin, card_type) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, cardNumber);
            ps.setString(2, accNo);
            ps.setString(3, holderName);
            ps.setString(4, expiry);
            ps.setString(5, cvv);
            ps.setString(6, pin);
            ps.setString(7, cardType);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean isExpired(String expiryDate) {
        if (expiryDate == null || !expiryDate.contains("/")) return true;
        try {
            String[] parts = expiryDate.split("/");
            int expMonth = Integer.parseInt(parts[0]);
            int expYear = Integer.parseInt(parts[1]) + 2000;

            java.util.Calendar cal = java.util.Calendar.getInstance();
            int curMonth = cal.get(java.util.Calendar.MONTH) + 1;
            int curYear = cal.get(java.util.Calendar.YEAR);

            if (expYear < curYear) return true;
            if (expYear == curYear && expMonth < curMonth) return true;
            
            return false;
        } catch (Exception e) {
            return true; // Invalid format treated as expired
        }
    }

    public List<Card> getCardsByAccount(String accNo) {
        List<Card> cards = new ArrayList<>();
        String query = "SELECT * FROM cards WHERE account_number = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, accNo);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Card card = new Card();
                card.setId(rs.getInt("id"));
                card.setCardNumber(rs.getString("card_number"));
                card.setAccountNumber(rs.getString("account_number"));
                card.setHolderName(rs.getString("holder_name"));
                card.setExpiryDate(rs.getString("expiry_date"));
                card.setCvv(rs.getString("cvv"));
                card.setPin(rs.getString("pin"));
                card.setCardType(rs.getString("card_type"));
                card.setStatus(rs.getString("status"));
                cards.add(card);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return cards;
    }

    public boolean updateCardStatus(String cardNumber, String status) {
        String query = "UPDATE cards SET status = ? WHERE card_number = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, status);
            ps.setString(2, cardNumber);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updatePin(String cardNumber, String newPin) {
        String query = "UPDATE cards SET pin = ? WHERE card_number = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, newPin);
            ps.setString(2, cardNumber);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteCardsByAccount(String accNo) {
        String query = "DELETE FROM cards WHERE account_number = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, accNo);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private String generateCardNumber() {
        Random rand = new Random();
        StringBuilder sb = new StringBuilder("4"); // Visa starts with 4
        for (int i = 0; i < 15; i++) {
            sb.append(rand.nextInt(10));
        }
        return sb.toString();
    }
}
