package com.example.qr_scanner_and_image_similarity_detection.models;

public class LocationModel {
    private String date;
    private String path;
    private String userId;
    //private boolean status=false;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public LocationModel(){}

    public LocationModel(String date, String path, String userId){
        this.path=path;
        this.userId=userId;
        this.date=date;
        //this.status=status;
    }

    /*public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }*/

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public String getUserId() {
        return userId;
    }
}
