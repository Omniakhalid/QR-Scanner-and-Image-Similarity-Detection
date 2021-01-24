package com.example.qr_scanner_and_image_similarity_detection.models;

import android.graphics.Bitmap;

public class User {
    
    private String Name;
    private String Email;
    private String Password;
    private String Phone;
    private String Gender;
    private Bitmap QR;

    public User() {
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public Bitmap getQR() {
        return QR;
    }

    public void setQR(Bitmap QR) {
        this.QR = QR;
    }
}
