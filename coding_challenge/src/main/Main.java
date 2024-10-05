package main;

import java.util.ArrayList;
import java.util.Scanner;

import dao.OrderProcessor;
import entity.Clothing;
import entity.Electronics;
import entity.Product;
import entity.User;
import exception.UserNotFoundException;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        OrderProcessor orderProcessor = new OrderProcessor();

        while (true) {
            // Display menu options
            System.out.println("Order Management System");
            System.out.println("1. Create User");
            System.out.println("2. Create Product");
            System.out.println("3. Get All Products");
            System.out.println("4. Get Order By User");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline left-over

            switch (choice) {
                case 1:
                    // Create User
                    System.out.print("Enter user ID: ");
                    int userId = scanner.nextInt();
                    scanner.nextLine(); // Consume newline left-over

                    System.out.print("Enter username: ");
                    String username = scanner.nextLine();

                    System.out.print("Enter password: ");
                    String password = scanner.nextLine();

                    System.out.print("Enter role (Admin/User): ");
                    String role = scanner.nextLine();

                    User newUser = new User(userId, username, password, role);
                    orderProcessor.createUser(newUser);
                    System.out.println("User created successfully!");
                    break;

                case 2:
                    // Create Product
                    System.out.print("Enter user ID (admin): ");
                    int adminId = scanner.nextInt();
                    User adminUser = new User(adminId, "admin", "adminpass", "Admin"); // Assuming dummy admin user

                    System.out.print("Enter product ID: ");
                    int productId = scanner.nextInt();
                    scanner.nextLine(); // Consume newline left-over

                    System.out.print("Enter product name: ");
                    String productName = scanner.nextLine();

                    System.out.print("Enter description: ");
                    String description = scanner.nextLine();

                    System.out.print("Enter price: ");
                    double price = scanner.nextDouble();
                    scanner.nextLine(); // Consume newline left-over

                    System.out.print("Enter quantity in stock: ");
                    int quantityInStock = scanner.nextInt();
                    scanner.nextLine(); // Consume newline left-over

                    System.out.print("Enter type (Electronics/Clothing): ");
                    String type = scanner.nextLine();

                    Product product = null;
                    if (type.equalsIgnoreCase("Electronics")) {
                        product = new Electronics();
                        product.setProductId(productId);
                        product.setProductName(productName);
                        product.setDescription(description);
                        product.setPrice(price);
                        product.setQuantityInStock(quantityInStock);

                        System.out.print("Enter brand: ");
                        String brand = scanner.nextLine();
                        ((Electronics) product).setBrand(brand);

                        System.out.print("Enter warranty period: ");
                        int warrantyPeriod = scanner.nextInt();
                        scanner.nextLine(); // Consume newline left-over
                        ((Electronics) product).setWarrantyPeriod(warrantyPeriod);
                    } else if (type.equalsIgnoreCase("Clothing")) {
                        product = new Clothing();
                        product.setProductId(productId);
                        product.setProductName(productName);
                        product.setDescription(description);
                        product.setPrice(price);
                        product.setQuantityInStock(quantityInStock);

                        System.out.print("Enter size: ");
                        String size = scanner.nextLine();
                        ((Clothing) product).setSize(size);

                        System.out.print("Enter color: ");
                        String color = scanner.nextLine();
                        ((Clothing) product).setColor(color);
                    }

                    if (product != null) {
                        try {
                            orderProcessor.createProduct(adminUser, product);
                            System.out.println("Product created successfully!");
                        } catch (UserNotFoundException e) {
                            System.out.println("Error: " + e.getMessage());
                        }
                    } else {
                        System.out.println("Invalid product type entered.");
                    }
                    break;

                case 3:
                    // Get All Products
                    ArrayList<Product> products = orderProcessor.getAllProducts();
                    System.out.println("All Products:");
                    for (Product p : products) {
                        System.out.println(p);
                    }
                    break;

                case 4:
                    // Get Order By User
                    System.out.print("Enter user ID to get orders: ");
                    int userOrderId = scanner.nextInt();
                    scanner.nextLine(); // Consume newline left-over
                    User userOrder = new User(userOrderId, "dummy", "dummy", "User"); // Assuming dummy user

                    try {
                        ArrayList<Product> userProducts = orderProcessor.getOrderByUser(userOrder);
                        System.out.println("Orders for User ID " + userOrderId + ":");
                        for (Product p : userProducts) {
                            System.out.println(p);
                        }
                    } catch (UserNotFoundException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;

                case 5:
                    // Exit
                    System.out.println("Exiting the system. Goodbye!");
                    scanner.close();
                    return; // Exit the program

                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
}
