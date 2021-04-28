package com.example.qr_scanner_and_image_similarity_detection.models;

import android.graphics.Bitmap;

public class User {

    private String UId;
    private String Name;
    private String Email;
    private String Password;
    private String Phone;
    private String Gender;
    private String Status;
    private boolean BlockUser;


    public User() {
    }

    public String getUId() {
        return UId;
    }

    public void setUId(String UId) {
        this.UId = UId;
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

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public boolean isBlockUser() {
        return BlockUser;
    }

    public void setBlockUser(boolean blockUser) {
        BlockUser = blockUser;
    }
}
