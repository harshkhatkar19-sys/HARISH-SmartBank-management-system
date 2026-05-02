package com.bank.scratch;

import com.bank.utils.DBConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class FindAccount {
    public static void main(String[] args) {
        try (Connection con = DBConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT account_number, holder_name FROM accounts WHERE holder_name LIKE '%harish%'")) {
            
            while (rs.next()) {
                System.out.println("Found: " + rs.getString("holder_name") + " -> " + rs.getString("account_number"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
