package com.example.drazcarsapp;

public class Car {
    private int id; // المعرف الفريد
    private String name;
    private String price;
    private int imageResId;
    private String status;
    private String category;

    public Car(int id, String name, String price, int imageResId, String status, String category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageResId = imageResId;
        this.status = status;
        this.category = category;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getPrice() { return price; }
    public int getImageResId() { return imageResId; }
    public String getStatus() { return status; }
    public String getCategory() { return category; }
}