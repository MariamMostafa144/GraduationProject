package com.example.spokenglovesapp;

public class SignsListContent {

    String signName;
    int img;

    public SignsListContent(String signName, int img) {
        this.signName = signName;
        this.img = img;
    }

    public String tostring(){return this.signName+"\n"+this.img;}

}

