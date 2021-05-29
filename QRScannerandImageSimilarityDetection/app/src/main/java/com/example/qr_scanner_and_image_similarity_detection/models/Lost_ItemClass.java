package com.example.qr_scanner_and_image_similarity_detection.models;

public class Lost_ItemClass {

    private int Item_ID;
    private String User_ID;
    private String imageLosted;

    public Lost_ItemClass(){
    }

    public String getImageLosted() {
        return imageLosted;
    }

    public void setImageLosted(String imageLosted) {
        this.imageLosted = imageLosted;
    }

    public int getItem_ID() {
        return Item_ID;
    }

    public String getUser_ID() {
        return User_ID;
    }

    public void setItem_ID(int item_ID) {
        Item_ID = item_ID;
    }

    public void setUser_ID(String user_ID) {
        User_ID = user_ID;
    }
}
