package com.example.spokenglovesapp;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Locale;

public class Chat extends AppCompatActivity {

    EditText etMessage;
    FloatingActionButton fab;
    public static String message;
    private static final int REQIEST_CODE_SPEACH_INPUT = 1000;
    ImageView imgBack;

    private  ArrayList<Message>messagesList=new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private MessageAdapter messageAdapter;
    private RecyclerView userMessagesList;
    private DatabaseReference databaseReference;
    String currentChatId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        fab = findViewById(R.id.fab);
        etMessage = findViewById(R.id.etMessage);
        imgBack=findViewById(R.id.imgBack);

        messageAdapter=new MessageAdapter(messagesList);
        userMessagesList=findViewById(R.id.messageRecycle);
        linearLayoutManager=new LinearLayoutManager(this);
        messagesList=new ArrayList<>();
        userMessagesList.setLayoutManager(linearLayoutManager);
        userMessagesList.setAdapter(messageAdapter);

        currentChatId=getIntent().getExtras().get("id").toString();
        databaseReference= FirebaseDatabase.getInstance().getReference();

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Chat.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                Chat.this.finish();
            }
        });
        etMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                checkEditText();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkEditText();
            }

            @Override
            public void afterTextChanged(Editable s) {
                checkEditText();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message = etMessage.getText().toString().trim();
                if (TextUtils.isEmpty(message)) {
                    getSpeechInput(getCurrentFocus());
                } else {
                    sendMessage();
                    etMessage.setText("");
                }
            }
        });

    }

    public void checkEditText() {
        message = etMessage.getText().toString().trim();
        if (TextUtils.isEmpty(message)) {
            fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_keyboard_voice_24));
        } else {
            fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_send_24));
        }
    }

    public void getSpeechInput(View view) {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hi speak something");
        try {
            startActivityForResult(intent, REQIEST_CODE_SPEACH_INPUT);
        } catch (Exception e) {
            Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQIEST_CODE_SPEACH_INPUT:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    //tvTest.setText(result.get(0));
                }
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseReference.child("Messages").child("senderGlovesId").child("fromUserId").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Message message=snapshot.getValue(Message.class);
                messagesList.add(message);
                messageAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void sendMessage(){
        Message message=new Message();
        String messageText= etMessage.getText().toString();
        message.setMessage(messageText);
        message.setSenderId(currentChatId);
        databaseReference.child("MessagesDetails").push().setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                    Toast.makeText(Chat.this,"Message sent sucessfully",Toast.LENGTH_LONG).show();
                }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Chat.this,"Error..!",Toast.LENGTH_LONG).show();
            }
        });
    }
}