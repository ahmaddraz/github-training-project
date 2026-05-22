package com.example.drazcarsapp;

import androidx.room.Entity;
import androidx.room.Ignore; // لا تنسى هذا الـ Import
import androidx.room.PrimaryKey;

@Entity(tableName = "bookings")
public class Booking {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String carName;
    private String startDate;
    private String endDate;
    private String totalPrice;
    private String userEmail;
    private String status;

    public Booking() {
    }

    @Ignore
    public Booking(String carName, String startDate, String endDate, String totalPrice, String userEmail, String status) {
        this.carName = carName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalPrice = totalPrice;
        this.userEmail = userEmail;
        this.status = status;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getCarName() { return carName; }
    public void setCarName(String carName) { this.carName = carName; }
    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }
    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }
    public String getTotalPrice() { return totalPrice; }
    public void setTotalPrice(String totalPrice) { this.totalPrice = totalPrice; }
    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}