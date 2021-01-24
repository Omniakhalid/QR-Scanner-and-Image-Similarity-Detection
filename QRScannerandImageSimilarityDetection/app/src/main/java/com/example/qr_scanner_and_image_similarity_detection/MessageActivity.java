package com.example.qr_scanner_and_image_similarity_detection;

import android.content.Intent;
import android.os.Bundle;

import com.example.qr_scanner_and_image_similarity_detection.adapters.MessageChatAdapter;
import com.example.qr_scanner_and_image_similarity_detection.models.MessageChatModel;
import com.example.qr_scanner_and_image_similarity_detection.models.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {
   CircleImageView profil_image;
   TextView username;
   FirebaseUser fuser;
   DatabaseReference reference;
   Intent intent;


   ImageView btn_send;
   EditText message;

   MessageChatAdapter messageChatAdapter;
   List<MessageChatModel> mchat;
   RecyclerView recyclerView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //my code
        recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);




        profil_image =findViewById(R.id.prfile_image1);
        username=findViewById(R.id.username1);
        btn_send=findViewById(R.id.sendBtn1);
        message=findViewById(R.id.messageET1);

        intent=getIntent();
        String usrerid=intent.getStringExtra("user_id");

        fuser= FirebaseAuth.getInstance().getCurrentUser();
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String messag=message.getText().toString();
                if(!messag.equals(""))
                {
                    sendMessage(fuser.getUid(),usrerid,messag);

                }
                else
                {
                    Toast.makeText(getApplicationContext(),"you cant send empty message",Toast.LENGTH_LONG).show();
                }
                message.setText("");

            }
        });




        reference= FirebaseDatabase.getInstance().getReference("Users").child(usrerid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

              User user=dataSnapshot.getValue(User.class);
              //user.getname();
              username.setText(usrerid);
              profil_image.setImageResource(R.drawable.images);

              readMessage(fuser.getUid(),usrerid);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }



    private void sendMessage(String sender,String reciver,String message){

        DatabaseReference reference=FirebaseDatabase.getInstance().getReference();
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("reciver",reciver);
        hashMap.put("message",message);



        reference.child("Chat").push().setValue(hashMap);

    }

    private void readMessage( String userid, String recivername)
    {
      mchat=new ArrayList<>();
      reference=FirebaseDatabase.getInstance().getReference("Chat");
      reference.addValueEventListener(new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
              mchat.clear();
              for(DataSnapshot snapshot:dataSnapshot.getChildren())
              {
                  MessageChatModel chat=snapshot.getValue(MessageChatModel.class);
                  assert chat != null;
                  if(chat.getReciver().equals(userid)&& chat.getSender().equals(recivername)||
                       chat.getReciver().equals(recivername)&&chat.getSender().equals(userid)) {
                      mchat.add(chat);
                  }
                  messageChatAdapter=new MessageChatAdapter(mchat, MessageActivity.this);
                  recyclerView.setAdapter(messageChatAdapter);
              }
          }

          @Override
          public void onCancelled(@NonNull DatabaseError databaseError) {

          }
      });


    }

}
