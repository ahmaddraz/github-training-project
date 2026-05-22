package com.example.drazcarsapp;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;
    private String email;
    private String password;
    private String imagePath;
    private String phoneNumber; // حقل رقم الهاتف الذي كان يسبب الخطأ

    // 1. الكونسرتكتور الفارغ: ضروري جداً لمكتبة Room
    public User() {
    }

    // 2. كونسرتكتور شامل لجميع البيانات (نستخدم @Ignore لكي لا ترتبك Room)
    @Ignore
    public User(String name, String email, String password, String imagePath, String phoneNumber) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.imagePath = imagePath;
        this.phoneNumber = phoneNumber;
    }

    // --- Getters and Setters ---
    // هذه الدوال هي التي تسمح للبرنامج بقراءة وتعديل البيانات

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    // الدوال المطلوبة لحل خطأ ProfileActivity
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}