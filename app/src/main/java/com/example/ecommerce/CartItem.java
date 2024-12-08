package com.example.ecommerce;

public class CartItem {
    String name;
     String img;
     double price;
     int quantity;

    // Empty constructor for Firebase/SharedPreferences
    public CartItem() {}

    // Constructor
    public CartItem(String name, String img, double price, int quantity) {
        this.name = name;
        this.img = img;
        this.price = price;
        this.quantity = quantity;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getImg() {
        return img;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
