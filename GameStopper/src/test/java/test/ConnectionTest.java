package test;

import java.sql.Connection;
import util.DatabaseConnection;

public class ConnectionTest {
    public static void main(String[] args) {
        try (Connection conn = DatabaseConnection.getInstance().getConnection()) {
            System.out.println("Connection Successful!");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Connection Failed!");
        }
    }
}
