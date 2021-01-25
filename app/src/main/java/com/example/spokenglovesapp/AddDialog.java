package com.example.spokenglovesapp;

import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.io.IOException;
import java.io.InputStream;

public class AddDialog extends DialogFragment {

   public EditText etName;
    Button btnAdd;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.add_sign_dialog,container,false);
        etName=view.findViewById(R.id.etName);
        btnAdd=view.findViewById(R.id.btnAdd);
        Toast.makeText(getContext(),"Wait secound !!",Toast.LENGTH_SHORT).show();
        BluetoothSocket btSocket = null;
        return view;
    }
 public    AddDialog  newdata(BluetoothSocket g) {
        Bundle bundle =new Bundle();
     InputStream inputStream = null;
     try {
         inputStream = g.getInputStream();
         inputStream.skip(inputStream.available());
         byte[] buffer = new byte[256];
         int bytes, i;
         i = 0;
         while (i <= 1) {
             try {
                 bytes = inputStream.read(buffer);
                 String mm = new String(buffer, 0, bytes);
                 System.out.print(mm);
              //   print(mm);
                 i++;
             } catch (IOException e) {
                 e.printStackTrace();
             }
         }
 }
    catch (IOException e) {
        e.printStackTrace();
    }return null; }

public  void print(String text){
Toast.makeText(getContext(),text,Toast.LENGTH_LONG);


    }
}
