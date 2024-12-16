package dao;

import model.Product;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    // Get all products
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM Products";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Product product = new Product();
                product.setId(rs.getInt("id"));
                product.setName(rs.getString("name"));
                product.setDescription(rs.getString("description"));
                product.setPrice(rs.getDouble("price"));
                product.setStock(rs.getInt("stock"));
                product.setGenre(rs.getString("genre")); // New field
                product.setDevice(rs.getString("device")); // New field
                product.setImageUrl(rs.getString("image_url"));
                products.add(product);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;
    }

    public Product getProductById(int id) {
        Product product = null;
        String query = "SELECT * FROM Products WHERE id = ?";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {
             
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                product = new Product();
                product.setId(rs.getInt("id"));
                product.setName(rs.getString("name"));
                product.setDescription(rs.getString("description"));
                product.setPrice(rs.getDouble("price"));
                product.setStock(rs.getInt("stock"));
                product.setGenre(rs.getString("genre")); // New field
                product.setDevice(rs.getString("device")); // New field
                product.setImageUrl(rs.getString("image_url"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return product;
    }
}
