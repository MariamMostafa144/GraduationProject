package com.example.spokenglovesapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UserListAdapter extends BaseAdapter {
    List<UploadUserInfo> data;
    Context context;
    AlertDialog.Builder builder;
    StorageReference storageReference ;
    FirebaseDatabase firebaseDatabase ;
    DatabaseReference databaseReference ;


    public UserListAdapter(List<UploadUserInfo> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("ViewHolder") final View row=inflater.inflate(R.layout.user_list_item,parent,false);
        storageReference = FirebaseStorage.getInstance("gs://garduate.appspot.com").getReference().child("UserInfo/");
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("UserInfo");
        TextView tvUserName=row.findViewById(R.id.etSignName);
        final ImageView userImg=row.findViewById(R.id.userImg);
        Button btnDeleteUser=row.findViewById(R.id.btnDeleteUser);
        btnDeleteUser.setFocusable(false);
        btnDeleteUser.setTag(position);
        btnDeleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure to delete this chat ?")
                        .setCancelable(false)
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String chat_id=data.get(position).getChat_id();
                                Query deleteQuery = databaseReference.orderByChild("chat_id").equalTo(chat_id);
                                deleteQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                                            snapshot.getRef().removeValue();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                    }
                                });
                                Query messages =firebaseDatabase.getReference().child("MessagesDetails").orderByChild("chatId").equalTo(chat_id);
                                messages.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                                            snapshot.getRef().removeValue();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                    }
                                });
                                notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                //Creating dialog box
                AlertDialog alert = builder.create();
                alert.show();
            }
        });


        final UploadUserInfo content=data.get(position);
        tvUserName.setText(content.getUserName());
        Picasso.get().load(content.getImageURL()).fit().centerCrop().into(userImg);

        return row;
    }

}
