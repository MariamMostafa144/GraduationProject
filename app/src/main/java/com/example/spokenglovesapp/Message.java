package com.example.spokenglovesapp;

public class Message {
    private String  senderId,message,chatId;

    public Message() {

    }

    public Message(String senderId, String message, String chatId) {
        this.senderId = senderId;
        this.message = message;
        this.chatId = chatId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }
}
