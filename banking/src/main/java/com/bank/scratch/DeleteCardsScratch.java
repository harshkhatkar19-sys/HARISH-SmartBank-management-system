package com.bank.scratch;

import com.bank.utils.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class DeleteCardsScratch {
    public static void main(String[] args) {
        String holderName = "harish";
        try (Connection con = DBConnection.getConnection()) {
            // First find account numbers for holderName like 'harish'
            String query = "DELETE FROM cards WHERE account_number IN (SELECT account_number FROM accounts WHERE holder_name LIKE ?)";
            try (PreparedStatement ps = con.prepareStatement(query)) {
                ps.setString(1, "%" + holderName + "%");
                int deleted = ps.executeUpdate();
                System.out.println("Deleted " + deleted + " cards for accounts matching '" + holderName + "'");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
