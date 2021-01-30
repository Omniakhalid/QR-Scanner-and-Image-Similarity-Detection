package com.example.qr_scanner_and_image_similarity_detection;

import android.os.Bundle;

import com.example.qr_scanner_and_image_similarity_detection.adapters.UsersChatAdapter;
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

import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MychatsActivity extends AppCompatActivity {

    public RecyclerView recyclerView;
    private UsersChatAdapter userAdapter;
    private List<User> mUsers;

    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;

    private List<String> usersList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mychats);

        recyclerView = findViewById(R.id.recycler_view_mychats);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        usersList = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("Chat");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                   MessageChatModel chat = snapshot.getValue(MessageChatModel.class);

                    if (chat.getSender().equals(firebaseUser.getUid())) {
                        usersList.add(chat.getReciver());
                    }

                    if (chat.getReciver().equals(firebaseUser.getUid())) {
                        usersList.add(chat.getSender());

                    }

                    readChats();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void readChats() {

        mUsers = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);

                    // display 1 user from chats
                    for (String id : usersList) {

                        if (user.getUId().equals(id)) {
                            if (mUsers.size() != 0) {
                                boolean containsUser = false;

                                for (User user1 : mUsers) {
                                    if (user.getUId().equals(user1.getUId())) {
                                        containsUser = true;
                                    }
                                }
                                if (!containsUser) {
                                    mUsers.add(user);
                                }
                            } else {
                                mUsers.add(user);
                            }
                        }
                    }
                }

                userAdapter = new UsersChatAdapter(MychatsActivity.this, mUsers,true);
                recyclerView.setAdapter(userAdapter);
                ;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
