package com.example.qr_scanner_and_image_similarity_detection.models;

import java.util.List;

public class Reminder {
    private String reminderId;
    private String startDate;
    private String endDate;
    private List<String> itemsName;
    private String itemId;
    private String userId;


    public Reminder(String reminderId, String startDate, String endDate, List<String> itemsName, String itemId, String userId) {
        this.reminderId = reminderId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.itemsName = itemsName;
        this.itemId = itemId;
        this.userId = userId;
    }
    List<String>  getItemsName() {
        return itemsName;
    }

    public void setItemsName(List<String>  itemsName) {
        this.itemsName = itemsName;
    }

    public String getReminderId() {
        return reminderId;
    }

    public void setReminderId(String reminderId) {
        this.reminderId = reminderId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
