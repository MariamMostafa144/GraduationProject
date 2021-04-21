package com.example.spokenglovesapp;

public class UploadUserInfo {
    public String userName;
    public String imageURL;
    public String macAddress;
    public String chat_id;

    public UploadUserInfo(){}

    public UploadUserInfo(String userName, String imageURL,String macAddress,String chat_id ) {
        this.userName = userName;
        this.imageURL = imageURL;
        this.macAddress=macAddress;
        this.chat_id=chat_id;
    }

    public String getUserName() {
        return userName;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public String getChat_id() {
        return chat_id;
    }
}