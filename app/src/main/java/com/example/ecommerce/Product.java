package com.example.ecommerce;

public class Product {
    private String name;
    private String description;
    private String img;
    private double price;

    // Empty constructor required for Firebase
    public Product() {
    }

    public Product(String name, String description, String img, double price) {
        this.name = name;
        this.description = description;
        this.img = img;
        this.price = price;
    }

    // Getters and setters
    // ...

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getImg() {
        return img;
    }

    public double getPrice() {
        return price;
    }}
