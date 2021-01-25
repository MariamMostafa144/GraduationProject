package com.example.spokenglovesapp;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Locale;
import java.util.UUID;

import static android.widget.Toast.LENGTH_SHORT;

public class HomeFragment extends Fragment {

    private static final String TAG = "MainActivity";
    Switch switchConnection;
    AlertDialog.Builder builder;
    BluetoothAdapter mBluetoothAdapter ;
    public ArrayList<BluetoothDevice> mBTDevices = new ArrayList<>();
    public DeviceListAdapter mDeviceListAdapter;
    ListView lvNewDevices;
    private ProgressDialog progress;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    //SPP UUID. Look for it
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    TextView bluetoothStatus,txvResult,readdata;
    String address;
    private static final int REQIEST_CODE_SPEACH_INPUT=1000;


    private BroadcastReceiver mBroadcastReceiver3 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            Log.d(TAG, "onReceive: ACTION FOUND.");

            if (action.equals(BluetoothDevice.ACTION_FOUND)){
                BluetoothDevice device = intent.getParcelableExtra (BluetoothDevice.EXTRA_DEVICE);
                mBTDevices.add(device);
                Log.d(TAG, "onReceive: " + device.getName() + ": " + device.getAddress());
                mDeviceListAdapter = new DeviceListAdapter(context, R.layout.list_item_of_device, mBTDevices);
                lvNewDevices.setAdapter(mDeviceListAdapter);
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
    View view=inflater.inflate(R.layout.fragment_home, container, false);
    switchConnection=view.findViewById(R.id.switchConnection);
    bluetoothStatus=view.findViewById(R.id.statuse_bt);
    readdata=view.findViewById(R.id.dataread);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mBTDevices = new ArrayList<>();
        bluetoothStatus.setVisibility(TextView.INVISIBLE);
    switchConnection.setOnClickListener(new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onClick(View v) {
            if(switchConnection.isChecked()) {
                if (mBluetoothAdapter.isEnabled()) {
                    btnDiscover();

                }
                if (!mBluetoothAdapter.isEnabled()) {
                    builder = new AlertDialog.Builder(getContext());
                    builder.setMessage(R.string.dialog_message)
                            .setCancelable(false)
                            .setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    mBluetoothAdapter.enable();
                                    Toast.makeText(getContext(), "bluetooth turns on", LENGTH_SHORT).show();
                                    btnDiscover();

                                }
                            })
                            .setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    //Creating dialog box
                    AlertDialog alert = builder.create();
                    alert.setTitle(R.string.dialog_title);
                    alert.setIcon(R.drawable.ic_bluetooth_1);
                    alert.show();

                }
            }
            else{
                mBluetoothAdapter.disable();
            }
        }
    });
        return view;

    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void checkBTPermissions() {
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
            int permissionCheck = getContext().checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
            permissionCheck += getContext().checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");
            if (permissionCheck != 0) {

                this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001); //Any number
            }
        }else{
            Log.d(TAG, "checkBTPermissions: No need to check permissions. SDK version < LOLLIPOP.");
        }
    }
    public void checkBluetooth(){
        if (!mBluetoothAdapter.isEnabled()) {
            builder = new AlertDialog.Builder(getContext());
            builder.setMessage(R.string.dialog_message)
                    .setCancelable(false)
                    .setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            mBluetoothAdapter.enable();
                            Toast.makeText(getContext(),"bluetooth turns on",
                                    LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            //Creating dialog box
            AlertDialog alert = builder.create();
            alert.setTitle(R.string.dialog_title);
            alert.setIcon(R.drawable.ic_bluetooth_1);
            alert.show();
        }
    }

    public void showDialog(Activity activity){

        final Dialog dialog = new Dialog(activity);
        // dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.discovered_devices);

        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        mBTDevices.clear();
        lvNewDevices = (ListView) dialog.findViewById(R.id.listview);
        lvNewDevices.setOnItemClickListener(myListClickListener);
        lvNewDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                address = ((TextView) view.findViewById(R.id.tvDeviceAddress)).getText().toString();
                msg("selected device is "+address);
                new ConnectBT().execute();

                dialog.dismiss();

            }
        });

        dialog.show();


    }

    private AdapterView.OnItemClickListener myListClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView av, View v, int arg2, long arg3)
        {
            // Get the device MAC address
            //String info = ((TextView)v).getText().toString();
            //String address = info.substring(info.length() - 17);
            try {
                if (btSocket == null || !isBtConnected) {
                    mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

                    // This will connect the device with address as passed
                    BluetoothDevice hc = mBluetoothAdapter.getRemoteDevice(address);
                    btSocket = hc.createInsecureRfcommSocketToServiceRecord(myUUID);
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    };
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void btnDiscover(){
        Log.d(TAG, "btnDiscover: Looking for unpaired devices.");
        if(mBluetoothAdapter.isDiscovering()){
            mBluetoothAdapter.cancelDiscovery();
            Log.d(TAG, "btnDiscover: Canceling discovery.");
            //check BT permissions in manifest
            checkBTPermissions();
            mBluetoothAdapter.startDiscovery();
            IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            requireActivity().registerReceiver(mBroadcastReceiver3, discoverDevicesIntent);
        }
        if(!mBluetoothAdapter.isDiscovering()){
            //check BT permissions in manifest
            checkBTPermissions();
            mBluetoothAdapter.startDiscovery();
            IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            requireActivity().registerReceiver(mBroadcastReceiver3, discoverDevicesIntent);
        }
        showDialog(getActivity());
    }
    private void msg(String s)
    {
        Toast.makeText(getContext(),s,Toast.LENGTH_LONG).show();
    }
    private class ConnectBT extends AsyncTask<Void, Void, BluetoothSocket>  // UI thread
    {
        private boolean ConnectSuccess = true; //if it's here, it's almost connected

        @Override
        protected void onPreExecute()
        {
            progress = ProgressDialog.show(getContext(), "Connecting...", "Please wait!!!");  //show a progress dialog
        }

        @Override
        protected BluetoothSocket doInBackground(Void... devices) //while the progress dialog is shown, the connection is done in background
        {byte b=0;
            try
            {
                if (btSocket == null || !isBtConnected)
                {
                    BluetoothDevice dispositivo = mBluetoothAdapter.getRemoteDevice(address);//connects to the device's address and checks if it's available
                    btSocket = dispositivo.createRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();//start connection


                }
                else {
                    Toast.makeText(getContext(), "Invalid MAC: Address", Toast.LENGTH_LONG).show();
                }
            }
            catch (IOException e)
            {
                ConnectSuccess = false;//if the try failed, you can check the exception here
            }
            return btSocket;
        }

        protected void onPostExecute(BluetoothSocket result) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);
            if (!ConnectSuccess)
            {
                msg("Connection Failed. Is it a SPP Bluetooth? Try again.");
                bluetoothStatus.setVisibility(TextView.VISIBLE);
                Toast.makeText(getContext(),"Not connect",Toast.LENGTH_SHORT).show();
                bluetoothStatus.setText("Not Connected");
            }
            else
            {
                msg("Connected.");
                bluetoothStatus.setVisibility(TextView.VISIBLE);
                bluetoothStatus.setText("Connected");
                InputStream inputStream = null;
                try {
                    inputStream = result.getInputStream();
                    inputStream.skip(inputStream.available());
                    byte[] buffer = new byte[1600];
                    int bytes,i;
                    i=0;
                    while (i<=5) {
                        try {
                            bytes = inputStream.read(buffer);
                            String mm = new String(buffer, 0, bytes);
                            Toast.makeText(getContext(), mm, Toast.LENGTH_LONG).show();
                            //System.out.print( mm);
                            readdata.setText(new String(buffer, 0, bytes));
                            AddDialog add=new AddDialog();
                          //  add.newdata(result);

                            i++;
                        }   catch (IOException e) {
                            e.printStackTrace();
                        }
                 }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Toast.makeText(getContext(),"connect",Toast.LENGTH_SHORT).show();
                isBtConnected = true;
            }
            progress.dismiss();
        }
    }

/*    public void connect(){
        InputStream inputStream = null;
        try {
            inputStream = btSocket.getInputStream();
            inputStream.skip(inputStream.available());
            byte b = (byte) inputStream.read();
            System.out.print((char) b);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
    public void getSpeechInput(View view) {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Hi speak something");

        try {
            startActivityForResult(intent, REQIEST_CODE_SPEACH_INPUT);
        } catch (Exception e) {
            Toast.makeText(getContext(), ""+e.getMessage(), LENGTH_SHORT).show();
        }


    }

    protected void startActivityForResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQIEST_CODE_SPEACH_INPUT:
                if (resultCode == requireActivity().RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    txvResult.setText(result.get(0));
                }
                break;
        }
    }


}
