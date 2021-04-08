package com.example.spokenglovesapp;

import android.app.Application;
import android.widget.Toast;

public class finaltry extends Application {private String data;
static class DataHolder{
    private static String data;
    public static String getData() {return data;}
    public void setData(String data) {this.data = data;}

    private static final DataHolder holder = new DataHolder();
    public static DataHolder getInstance() {return holder;}
  /*  private String name; // private = restricted access

    // Getter
    public String getName() {
        return name;
    }

    // Setter
    public void setName(String newName) {
        this.name = newName;
    }*/}
}
