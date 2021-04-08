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

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static android.widget.Toast.LENGTH_SHORT;

public class HomeFragment extends Fragment {

    private static final String TAG = "MainActivity";
    AlertDialog.Builder builder;
    static BluetoothAdapter mBluetoothAdapter ;
    public ArrayList<BluetoothDevice> mBTDevices = new ArrayList<>();
    public DeviceListAdapter mDeviceListAdapter;
    private static ProgressDialog progress;
    static BluetoothSocket btSocket = null;
    private static boolean isBtConnected = false;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    static String address;
    Button btnNewChat;
    Switch switchConnection;
    ListView lvNewDevices,userList;

    List<UploadUserInfo> listOfUsers;
    DatabaseReference databaseReference;


  //  WifiLevelReceiver receiver;
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
    View view=inflater.inflate(R.layout.fragment_home, container, false);

    databaseReference= FirebaseDatabase.getInstance().getReference("UserInfo");
    listOfUsers=new ArrayList<>();

    switchConnection=view.findViewById(R.id.switchConnection);
    btnNewChat=view.findViewById(R.id.btnNewChat);
    userList=view.findViewById(R.id.userList);

    userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent=new Intent(getActivity(),Chat.class);
            String currentChat=databaseReference.child("UserInfo").push().getKey();
            intent.putExtra("id",currentChat);
            startActivity(intent);
        }
    });

    btnNewChat.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FragmentManager manager = getFragmentManager();
            AddUserDialog addUserDialog  = new AddUserDialog();
            addUserDialog.show(manager, null);

        }
    });
    //bluetoothStatus=view.findViewById(R.id.statuse_bt);
    //readdata=view.findViewById(R.id.dataread);
    //receiver = new WifiLevelReceiver();
    mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    mBTDevices = new ArrayList<>();
    //bluetoothStatus.setVisibility(TextView.INVISIBLE);

        if (mBluetoothAdapter.isEnabled()) {
            switchConnection.setChecked(true);
            showDialog(getActivity());
        }
    switchConnection.setOnClickListener(new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onClick(View v) {
                if(switchConnection.isChecked()) {
                    if (mBluetoothAdapter.isEnabled()) {
                        showDialog(getActivity());
                    }
                if (!mBluetoothAdapter.isEnabled()) {
                    builder = new AlertDialog.Builder(getContext());
                    builder.setMessage(R.string.dialog_message)
                            .setCancelable(false)
                            .setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    mBluetoothAdapter.enable();
                                    Toast.makeText(getContext(), "bluetooth turns on", LENGTH_SHORT).show();
                                    showDialog(getActivity());
                                }
                            })
                            .setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
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

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listOfUsers.clear();
                for (DataSnapshot userDataSnapshot : snapshot.getChildren()){
                    UploadUserInfo uploadUserInfo=userDataSnapshot.getValue(UploadUserInfo.class);
                    listOfUsers.add(uploadUserInfo);
                }
                UserListAdapter userListAdapter=new UserListAdapter(listOfUsers,getContext());
                userList.setAdapter(userListAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void showDialog(Activity activity){
        if(mBluetoothAdapter.isDiscovering()){
            mBluetoothAdapter.cancelDiscovery();
            Log.d(TAG, "btnDiscover: Canceling discovery.");
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
                address = ((TextView) view.findViewById(R.id.tvDeviceAddress)).getText().toString();

                dialog.dismiss();
                new connect().execute();



            }

        });
        dialog.show();
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




    private AdapterView.OnItemClickListener myListClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView av, View v, int arg2, long arg3)
        {

            if (btSocket == null || !isBtConnected) {
                mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

                       BluetoothDevice hc = mBluetoothAdapter.getRemoteDevice(address);

                try {
                    btSocket = hc.createInsecureRfcommSocketToServiceRecord(myUUID);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                 Toast.makeText(getContext(),btSocket.toString(),Toast.LENGTH_LONG).show();;


                //btSocket.connect();
            }

        }
    };
    public  class connect extends AsyncTask<Void, Void, BluetoothSocket>  // UI thread
    {
        private boolean ConnectSuccess = true; //if it's here, it's almost connected
        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(getContext(), "Connecting...", "Please wait!!!");  //show a progress dialog
        }
        @Override
        protected BluetoothSocket doInBackground(Void... voids) {
            try {
                mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                BluetoothDevice hc05 = mBluetoothAdapter.getRemoteDevice(address);
                btSocket = hc05.createRfcommSocketToServiceRecord(myUUID);
                BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                 btSocket.connect();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return btSocket;
        }
        protected void onPostExecute(BluetoothSocket btSocket) {
            super.onPostExecute(btSocket);
            finalbtstock.DataHolder fin=new finalbtstock.DataHolder();
            fin.setData(btSocket);
            //   Toast.makeText(getActivity(),btSocket.toString(),Toast.LENGTH_LONG).show();
            BluetoothSocket  data =finalbtstock.DataHolder.getData();
            Toast.makeText(getContext(), data.toString(), Toast.LENGTH_LONG).show();
            progress.dismiss();
        }
    }
}




