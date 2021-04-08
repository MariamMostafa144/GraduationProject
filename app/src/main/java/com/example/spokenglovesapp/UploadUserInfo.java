package com.example.spokenglovesapp;

public class UploadUserInfo {
    public String userName;
    public String imageURL;
    public UploadUserInfo(){}

    public UploadUserInfo(String name, String url) {
        this.userName = name;
        this.imageURL = url;
    }

    public String getUserName() {
        return userName;
    }
    public String getImageURL() {
        return imageURL;
    }
}