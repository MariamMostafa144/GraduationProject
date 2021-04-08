package com.example.spokenglovesapp;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

public class AddDialog extends DialogFragment {
    public static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    public static ProgressDialog progress;
    public EditText etName, etValue;
    Button getdata, saveindatabtn,addimagebtn;
    ImageView imageView;
    String mm;
    static boolean isBtConnected = false;
    static BluetoothAdapter btAdapter;
    int REQUEST_IMAGE_CAPTURE = 1;
    private String pictureFilePath;
    Bitmap imageBitmap;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_sign_dialog, container, false);
        etName = view.findViewById(R.id.etSignName);
        etValue = view.findViewById(R.id.etValue);
        getdata = (Button) view.findViewById(R.id.btn2);
        addimagebtn = (Button) view.findViewById(R.id.btnAddSignImg);
        saveindatabtn = view.findViewById(R.id.btnAddSign);
        imageView = view.findViewById(R.id.imageView);
       btAdapter = BluetoothAdapter.getDefaultAdapter();
        //databasecreator
        final DatabaseReference datasave = FirebaseDatabase.getInstance().getReference("databaase").child("data");
    // BluetoothSocket btSocket = null;
        getdata.setOnClickListener(new View.OnClickListener() {
                                       @RequiresApi(api = Build.VERSION_CODES.O)
                                       @Override
                                       public void onClick(View v) {
                                           if (btAdapter.enable()) {
                                               new connect().execute();
                                           } else {
                                               Toast.makeText(getContext(), "dia", Toast.LENGTH_LONG).show();
                                               btAdapter.enable();
                                               new connect().execute();
                                           }
                                       }
                                   }
        );
        saveindatabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
  /*              DatabaseReference data= FirebaseDatabase.getInstance().getReference("signs").child("values");
                data.push().setValue("");
*/
                if (TextUtils.isEmpty(etValue.getText().toString()) ||TextUtils.isEmpty(etName.getText().toString()) ||imageBitmap==null) {
                    Toast.makeText(getActivity(), "enter all data", Toast.LENGTH_LONG).show();
                } else {
                    valuesave value = new valuesave(etValue.getText().toString(), etName.getText().toString());
                    datasave.push().setValue(value);
                    uploade();
                    imageView.setImageResource(0);
                    etName.setText("");
                    etValue.setText("");
                }
            }
        });
        addimagebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePictureIntent,REQUEST_IMAGE_CAPTURE );
            }
        });
        return view;
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
          //  Bundle extras = data.getExtras();
             imageBitmap = (Bitmap)data.getExtras().get("data");
            imageView.setImageBitmap(imageBitmap);
        }
    }
    //yploadimage
    private void uploade( ) {
        FirebaseStorage storageReference;
        storageReference = FirebaseStorage.getInstance("gs://garduate.appspot.com");
        String path="data"+UUID.randomUUID()+"PNG";

        StorageReference storag=storageReference.getReference().child(etName.getText().toString());
        ByteArrayOutputStream stream=new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
        byte [] b=stream.toByteArray();
        storag.putBytes(b)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                    }
                });

    }
        public class connect extends AsyncTask<Void, Void, BluetoothSocket>  // UI thread
    {
        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(getContext(), "Connecting...", "Please wait!!!");  //show a progress dialog
        }
        @Override
        protected BluetoothSocket doInBackground(Void... voids) {
            BluetoothSocket btSocket = finalbtstock.DataHolder.getData();
        /*   try {
                btSocket.connect();
            } catch (IOException e) {
                e.printStackTrace();
            }*/
            return btSocket;
        }
        protected void onPostExecute(BluetoothSocket btSocket) {
            super.onPostExecute(btSocket);
            InputStream inputStream = null;
            try {
                inputStream = btSocket.getInputStream();
                inputStream.skip(inputStream.available());
                byte[] buffer = new byte[1024];
                int bytes, i;
                i = 0;
                String mm;

                while (i<=1) {

                    etValue.setText("");
                    bytes = inputStream.read(buffer);
                    mm = new String(buffer,0,bytes);
                    System.out.print(mm);
                    etValue.append(mm);
                    i++;
             /*       while (i <= 1) {
                        String mm;
                     //   bytes = inputStream.read(buffer);
                        BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
                        //r.ready();
                        r.skip(20);
                        mm =r.readLine();
                        etValue.append(mm);
                        i++;
                        r.close();
                    }*/
                }

            }
                    catch( IOException e){
                        e.printStackTrace();
                    }
            progress.dismiss();
        }
    }
}
/*  public void connetfinal(BluetoothSocket data1) {

        /* Toast.makeText(getContext(), "ompre", Toast.LENGTH_LONG).show();

          SystemClock.sleep(1000);
          InputStream inputStream = null;
          try {
              inputStream = btSocket.getInputStream();
              inputStream.skip(inputStream.available());
              int i = 0;
              byte[] buffer = new byte[1024];
              int bytes;
              while (i <= 10) {
                  bytes = inputStream.read(buffer);
                  //  String mm = new String(buffer,0,bytes);
                  String mm = new String(String.valueOf(bytes));
                  System.out.print(mm);
                  Toast.makeText(getContext(), mm, Toast.LENGTH_LONG).show();


              }
          } catch (IOException e) {
              e.printStackTrace();
          }*/
//new connect().execute();
/*     SystemClock.sleep(1000);
        InputStream inputStream = null;
        try {
            inputStream = btSocket.getInputStream();
            inputStream.skip(inputStream.available());
            int i=0;
            byte[] buffer = new byte[1024];
            int bytes;
            while (i<=10) {
                bytes  =  inputStream.read(buffer);
             //  String mm = new String(buffer,0,bytes);
               String mm = new String(String.valueOf(bytes));
                System.out.print(mm);
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }*///}

