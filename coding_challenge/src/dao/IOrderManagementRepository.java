package dao;

import entity.Product;
import entity.User;
import exception.OrderNotFoundException;
import exception.UserNotFoundException;

import java.util.ArrayList;

public interface IOrderManagementRepository {
    //void createOrder(User user, ArrayList<Product> products) throws UserNotFoundException; // Ensure this is exactly as in OrderProcessor
    //void cancelOrder(int userId, int orderId) throws UserNotFoundException, OrderNotFoundException;
    ArrayList<Product> getOrderByUser(User user) throws UserNotFoundException;
    void createProduct(User user, Product product) throws UserNotFoundException;
    void createUser(User user);
    ArrayList<Product> getAllProducts();
}
