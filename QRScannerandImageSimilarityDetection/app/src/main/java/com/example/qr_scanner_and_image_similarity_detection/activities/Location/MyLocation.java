package com.example.qr_scanner_and_image_similarity_detection.activities.Location;

public class MyLocation {
    private double latitude;
    private double longitude;

    public MyLocation(double latitude, double longitude){
        this.latitude=latitude;
        this.longitude=longitude;
    }
    public MyLocation(){}
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
