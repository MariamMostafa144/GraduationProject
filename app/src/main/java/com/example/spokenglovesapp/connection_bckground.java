package com.example.spokenglovesapp;

import android.app.AlertDialog;
import android.app.IntentService;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;


public class connection_bckground extends IntentService {
    public connection_bckground() {
        super("connection_bckground");
    }

    private static final String TAG = "MainActivity";
    AlertDialog.Builder builder;
    static BluetoothAdapter mBluetoothAdapter;
    public ArrayList<BluetoothDevice> mBTDevices = new ArrayList<>();
    public DeviceListAdapter mDeviceListAdapter;
    ListView lvNewDevices;

    private static ProgressDialog progress;
    static BluetoothSocket btSocket = null;
    private static boolean isBtConnected = false;
    //SPP UUID. Look for it
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    static String address;
    private static final int REQIEST_CODE_SPEACH_INPUT = 1000;
    ////

    /*private BroadcastReceiver mBroadcastReceiver3 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            Log.d(TAG, "onReceive: ACTION FOUND.");
            if (action.equals(BluetoothDevice.ACTION_FOUND)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                mBTDevices.add(device);
                Log.d(TAG, "onReceive: " + device.getName() + ": " + device.getAddress());
                mDeviceListAdapter = new DeviceListAdapter(context, R.layout.list_item_of_device, mBTDevices);
           //     lvNewDevices.setAdapter(mDeviceListAdapter);
            }
        }
    };*/

    public void onCreate() {
        Toast.makeText(this, "The new Service was Created", Toast.LENGTH_LONG).show();

        mBTDevices = new ArrayList<>();
    }



  /*  public void onStart(@Nullable Intent intent, int startId) {

        finalconnect();

    }*/

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {

        String userID = intent.getStringExtra("UserID");
        connetfinal(userID);
        return START_STICKY ;
    }


    @Override
    public void onDestroy() {
        //super.onDestroy();
    }

    public static void connetfinal(String url){
        byte b = 0;
        try {
            if (btSocket == null || !isBtConnected) {
                BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                BluetoothDevice dispositivo = mBluetoothAdapter.getRemoteDevice(url);//connects to the device's address and checks if it's available
                btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                btSocket.connect();//start connection
                System.out.print("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                System.out.print("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                System.out.print("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                System.out.print("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                System.out.print("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
            } else {
                System.out.print("asssssssssassasadqeqggggggggggg");
                System.out.print("asssssssssassasadqeqggggggggggg");
                System.out.print("asssssssssassasadqeqggggggggggg");
                System.out.print("asssssssssassasadqeqggggggggggg");


            }
        } catch (IOException e) {
            System.out.print("assssqqqqqqqqqsssssassasadqeqggggggggggg");
            System.out.print("assqqqqqqqqqqqqsssssssassasadqeqggggggggggg");
            System.out.print("assssqqqqqqqqqqsssssassasadqeqggggggggggg");
            System.out.print("asssssqqqqqqqqqqssssassasadqeqggggggggggg");
            System.out.print("asssssqqqqqqqqqqqssssassasadqeqggggggggggg");
            //ConnectSuccess = false;//if the try failed, you can check the exception here
        }

    }

  /*  public void btnDiscover( String adress1) {
        Log.d(TAG, "btnDiscover: Looking for unpaired devices.");
        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
            Log.d(TAG, "btnDiscover: Canceling discovery.");
            //check BT permissions in manifest

            mBluetoothAdapter.startDiscovery();
            IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(mBroadcastReceiver3, discoverDevicesIntent);
        }
        if (!mBluetoothAdapter.isDiscovering()) {
            //check BT permissions in manifest

            mBluetoothAdapter.startDiscovery();
            IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);

            registerReceiver(mBroadcastReceiver3, discoverDevicesIntent);
            adress1=address;
                for (int y=0;y>=10;y++){
                    System.out.print(address);

                }
           // String adress11 =((String)( R.id.tvDeviceAddress)).getText().toString();
           // new ConnectBT().execute();
        }
    }/*
     /*   Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {

            @Override
            public void run() {
                HomeFragment mm=new HomeFragment();
                showDialog(mm.getActivity());
            }
        });*/


 /*   public void finalconnect(){
        if (mBluetoothAdapter.isEnabled()) {
            btnDiscover();


        }
        if (!mBluetoothAdapter.isEnabled()) {
            builder = new AlertDialog.Builder(getApplicationContext());
            builder.setMessage(R.string.dialog_message)
                    .setCancelable(false)
                    .setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            mBluetoothAdapter.enable();
                            Handler handler = new Handler(Looper.getMainLooper());
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "bluetooth turns on", LENGTH_SHORT).show();
                                }
                            });
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
         /*   @SuppressLint("ResourceType") AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setTitle(R.string.dialog_title)
                    .setMessage(R.drawable.ic_bluetooth_1)
                    .create();
            alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            alertDialog.show();

        }

            else{
        mBluetoothAdapter.disable();
    }
    }*/


   /* public void showDialog (Activity activity){
            final Dialog dialog = new Dialog(activity);

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
                    address = ( (TextView) view.findViewById(R.id.tvDeviceAddress) ).getText().toString();
                    new ConnectBT().execute();
                    dialog.dismiss();
                }
            });

            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);

        }*/
      /*  class  ConnectBT extends AsyncTask<Void, Void, BluetoothSocket>  // UI thread
        {
            private boolean ConnectSuccess = true; //if it's here, it's almost connected

            @Override
            protected void onPreExecute() {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        progress = ProgressDialog.show(getApplicationContext(), "Connecting...", "Please wait!!!");  //show a progress dialog
                    }
                });
            }

            @Override
            protected BluetoothSocket doInBackground(Void... devices) //while the progress dialog is shown, the connection is done in background
            {
                byte b = 0;
                try {
                    if (btSocket == null || !isBtConnected) {
                        BluetoothDevice dispositivo = mBluetoothAdapter.getRemoteDevice(address);//connects to the device's address and checks if it's available
                        btSocket = dispositivo.createRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                        BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                        btSocket.connect();//start connection
                    } else {
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {

                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),
                                        "Invalid MAC: Address",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (IOException e) {
                    ConnectSuccess = false;//if the try failed, you can check the exception here
                }
                return btSocket;
            }

            protected void onPostExecute(BluetoothSocket result) //after the doInBackground, it checks if everything went fine
            {
                super.onPostExecute(result);
                if (!ConnectSuccess) {
                    Intent sendLevel = new Intent();
                    sendLevel.setAction("GET_SIGNAL_STRENGTH");
                    sendLevel.putExtra( "LEVEL_DATA","Strength_Value");
                    sendBroadcast(sendLevel);

                  /*  Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {

                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "notconnected",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });*/
            /*    } else {
                    Intent sendLevel = new Intent();
                    sendLevel.setAction("GET_SIGNAL_STRENGTH");
                    sendLevel.putExtra( "LEVEL_DATA","connected");
                    sendBroadcast(sendLevel);

                    /*Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {

                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Connected",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });*/
/*

                    isBtConnected = true;
                }
                progress.dismiss();
           }
        }*/
    /*    private  AdapterView.OnItemClickListener myListClickListener = new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView av, View v, int arg2, long arg3) {
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
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };*/



}


