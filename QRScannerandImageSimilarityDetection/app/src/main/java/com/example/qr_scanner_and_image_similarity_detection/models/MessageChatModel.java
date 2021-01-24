package com.example.qr_scanner_and_image_similarity_detection.models;

public class MessageChatModel {
    private String sender;
    private String reciver;
    private String message;

    public MessageChatModel(String sender, String reciver, String message) {
        this.sender = sender;
        this.reciver = reciver;
        this.message = message;
    }
    public MessageChatModel(String sender, String reciever) {
        this.sender = sender;
        this.reciver = reciever;

    }
    public MessageChatModel() {

    }



    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReciver() {
        return reciver;
    }

    public void setReciver(String reciver) {
        this.reciver = reciver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}