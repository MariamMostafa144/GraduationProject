package com.example.spokenglovesapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    public ArrayList<Message> userMessageList;
    private DatabaseReference databaseReference;

    public MessageAdapter(ArrayList<Message> userMessageList) {
        this.userMessageList = userMessageList;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.custom_message,viewGroup,false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MessageViewHolder messageViewHolder, int i) {

        databaseReference= FirebaseDatabase.getInstance().getReference();


       // String currentChatId=databaseReference.child("UserInfo").push().getKey();
        Message message=userMessageList.get(i);

        String myMessage =message.getMessage();
        String senderId =message.getSenderId();
        String chatId =message.getChatId();
        String GlovesId="123456789";

        messageViewHolder.tvGloves.setVisibility(View.INVISIBLE);
        messageViewHolder.tvOther.setVisibility(View.INVISIBLE);
        if(senderId==GlovesId){
            messageViewHolder.tvGloves.setVisibility(View.VISIBLE);
            messageViewHolder.tvGloves.setBackgroundResource(R.drawable.gloves_message);
            messageViewHolder.tvGloves.setText(myMessage);
        }
        else {
            messageViewHolder.tvOther.setVisibility(View.VISIBLE);
            messageViewHolder.tvOther.setBackgroundResource(R.drawable.other_message);
            messageViewHolder.tvOther.setText(myMessage);

        }
    }

    @Override
    public int getItemCount() {
        return userMessageList.size();
    }


    public class MessageViewHolder extends RecyclerView.ViewHolder{

        public TextView tvOther, tvGloves;
        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOther=itemView.findViewById(R.id.tvOther);
            tvGloves=itemView.findViewById(R.id.tvGloves);
        }
    }
}
