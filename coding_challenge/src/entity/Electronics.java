package entity;

public class Electronics extends Product {
    private String brand;
    private int warrantyPeriod;

    // Default constructor
    public Electronics() {
        super(); // Call to the superclass constructor
    }

    // Parameterized constructor (if needed)
    public Electronics(String brand, int warrantyPeriod) {
        super(); // Call to the superclass constructor
        this.brand = brand;
        this.warrantyPeriod = warrantyPeriod;
    }

    // Getters and setters
    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getWarrantyPeriod() {
        return warrantyPeriod;
    }

    public void setWarrantyPeriod(int warrantyPeriod) {
        this.warrantyPeriod = warrantyPeriod;
    }
}
