package entity;

public class Clothing extends Product {
    private String size;
    private String color;

    // Default constructor
    public Clothing() {
        super(); // Call to the superclass constructor
    }

    // Parameterized constructor (if needed)
    public Clothing(String size, String color) {
        super(); // Call to the superclass constructor
        this.size = size;
        this.color = color;
    }

    // Getters and setters
    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
