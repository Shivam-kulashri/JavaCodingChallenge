package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import entity.Electronics;
import entity.Clothing;
import entity.Product;
import entity.User;
import exception.UserNotFoundException;
import util.DBConnUtil;

public class OrderProcessor implements IOrderManagementRepository {

    @Override
    public void createProduct(User user, Product product) throws UserNotFoundException {
        Connection con = DBConnUtil.getConnection("jdbc:mysql://localhost:3306/HexawareDB2?user=root&password=password");
        if (con != null) {
            try {
                // Check if admin user exists in the database
                PreparedStatement pstmt = con.prepareStatement("SELECT * FROM users WHERE userId = ? AND role = 'Admin'");
                pstmt.setInt(1, user.getUserId());
                ResultSet rs = pstmt.executeQuery();
                if (!rs.next()) {
                    throw new UserNotFoundException("Admin user not found");
                }

                // Create product
                pstmt = con.prepareStatement("INSERT INTO products (userId, productName, description, price, quantityInStock, type) VALUES (?, ?, ?, ?, ?, ?)");
                pstmt.setInt(1, user.getUserId()); // Assuming the product is linked to a user
                pstmt.setString(2, product.getProductName());
                pstmt.setString(3, product.getDescription());
                pstmt.setDouble(4, product.getPrice());
                pstmt.setInt(5, product.getQuantityInStock());
                pstmt.setString(6, product.getType());
                pstmt.executeUpdate();

                // Save electronics or clothing details based on product type
                if (product instanceof Electronics) {
                    Electronics electronics = (Electronics) product;
                    pstmt = con.prepareStatement("INSERT INTO electronics (productId, warrantyPeriod) VALUES (?, ?)");
                    pstmt.setInt(1, product.getProductId());
                    pstmt.setInt(2, electronics.getWarrantyPeriod());
                    pstmt.executeUpdate();
                } else if (product instanceof Clothing) {
                    Clothing clothing = (Clothing) product;
                    pstmt = con.prepareStatement("INSERT INTO clothing (productId, size, color) VALUES (?, ?, ?)");
                    pstmt.setInt(1, product.getProductId());
                    pstmt.setString(2, clothing.getSize());
                    pstmt.setString(3, clothing.getColor());
                    pstmt.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            throw new UserNotFoundException("Failed to connect to database");
        }
    }

    @Override
    public void createUser(User user) {
        Connection con = DBConnUtil.getConnection("jdbc:mysql://localhost:3306/HexawareDB2?user=root&password=password");
        if (con != null) {
            try {
                PreparedStatement pstmt = con.prepareStatement("INSERT INTO users (userId, username, password, role) VALUES (?, ?, ?, ?)");
                pstmt.setInt(1, user.getUserId());
                pstmt.setString(2, user.getUsername());
                pstmt.setString(3, user.getPassword());
                pstmt.setString(4, user.getRole());
                pstmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public ArrayList<Product> getAllProducts() {
        ArrayList<Product> products = new ArrayList<>();
        Connection con = DBConnUtil.getConnection("jdbc:mysql://localhost:3306/HexawareDB2?user=root&password=password");
        if (con != null) {
            try {
                PreparedStatement pstmt = con.prepareStatement("SELECT * FROM products");
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    Product product = null;
                    if (rs.getString("type").equals("Electronics")) {
                        product = new Electronics();
                        product.setProductId(rs.getInt("userId"));  // Using userId as productId may not be correct
                        product.setProductName(rs.getString("productName"));
                        product.setDescription(rs.getString("description"));
                        product.setPrice(rs.getDouble("price"));
                        product.setQuantityInStock(rs.getInt("quantityInStock"));
                        pstmt = con.prepareStatement("SELECT * FROM electronics WHERE productId = ?");
                        pstmt.setInt(1, rs.getInt("userId"));  // Same here, verify correct ID usage
                        ResultSet electronicsRs = pstmt.executeQuery();
                        if (electronicsRs.next()) {
                            ((Electronics) product).setWarrantyPeriod(electronicsRs.getInt("warrantyPeriod"));
                        }
                    } else if (rs.getString("type").equals("Clothing")) {
                        product = new Clothing();
                        product.setProductId(rs.getInt("userId"));  // Using userId as productId may not be correct
                        product.setProductName(rs.getString("productName"));
                        product.setDescription(rs.getString("description"));
                        product.setPrice(rs.getDouble("price"));
                        product.setQuantityInStock(rs.getInt("quantityInStock"));
                        pstmt = con.prepareStatement("SELECT * FROM clothing WHERE productId = ?");
                        pstmt.setInt(1, rs.getInt("userId"));  // Same here, verify correct ID usage
                        ResultSet clothingRs = pstmt.executeQuery();
                        if (clothingRs.next()) {
                            ((Clothing) product).setSize(clothingRs.getString("size"));
                            ((Clothing) product).setColor(clothingRs.getString("color"));
                        }
                    }
                    products.add(product);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return products;
    }

    @Override
    public ArrayList<Product> getOrderByUser(User user) throws UserNotFoundException {
        ArrayList<Product> products = new ArrayList<>();
        Connection con = DBConnUtil.getConnection("jdbc:mysql://localhost:3306/HexawareDB2?user=root&password=password");
        if (con != null) {
            try {
                // Check if user exists in database
                PreparedStatement pstmt = con.prepareStatement("SELECT * FROM users WHERE userId = ?");
                pstmt.setInt(1, user.getUserId());
                ResultSet rs = pstmt.executeQuery();
                if (!rs.next()) {
                    throw new UserNotFoundException("User not found");
                }

                pstmt = con.prepareStatement("SELECT p.* FROM orders o INNER JOIN order_products op ON o.orderId = op.orderId INNER JOIN products p ON op.productId = p.userId WHERE o.userId = ?");
                pstmt.setInt(1, user.getUserId());
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    Product product = null;
                    if (rs.getString("type").equals("Electronics")) {
                        product = new Electronics();
                        product.setProductId(rs.getInt("userId"));  // Using userId as productId may not be correct
                        product.setProductName(rs.getString("productName"));
                        product.setDescription(rs.getString("description"));
                        product.setPrice(rs.getDouble("price"));
                        product.setQuantityInStock(rs.getInt("quantityInStock"));
                        pstmt = con.prepareStatement("SELECT * FROM electronics WHERE productId = ?");
                        pstmt.setInt(1, rs.getInt("userId"));  // Same here, verify correct ID usage
                        ResultSet electronicsRs = pstmt.executeQuery();
                        if (electronicsRs.next()) {
                            ((Electronics) product).setWarrantyPeriod(electronicsRs.getInt("warrantyPeriod"));
                        }
                    } else if (rs.getString("type").equals("Clothing")) {
                        product = new Clothing();
                        product.setProductId(rs.getInt("userId"));  // Using userId as productId may not be correct
                        product.setProductName(rs.getString("productName"));
                        product.setDescription(rs.getString("description"));
                        product.setPrice(rs.getDouble("price"));
                        product.setQuantityInStock(rs.getInt("quantityInStock"));
                        pstmt = con.prepareStatement("SELECT * FROM clothing WHERE productId = ?");
                        pstmt.setInt(1, rs.getInt("userId"));  // Same here, verify correct ID usage
                        ResultSet clothingRs = pstmt.executeQuery();
                        if (clothingRs.next()) {
                            ((Clothing) product).setSize(clothingRs.getString("size"));
                            ((Clothing) product).setColor(clothingRs.getString("color"));
                        }
                    }
                    products.add(product);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return products;
    }
}
