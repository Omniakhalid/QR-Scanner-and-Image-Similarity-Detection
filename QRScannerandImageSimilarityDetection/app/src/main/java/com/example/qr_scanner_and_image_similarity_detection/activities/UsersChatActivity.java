package com.example.qr_scanner_and_image_similarity_detection.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qr_scanner_and_image_similarity_detection.R;
import com.example.qr_scanner_and_image_similarity_detection.adapters.UsersChatAdapter;
import com.example.qr_scanner_and_image_similarity_detection.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UsersChatActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private UsersChatAdapter usersChatAdapter;
    private List<User> muser;
    private TextView emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userschat);

        recyclerView=findViewById(R.id.userrecycler);
        emptyView =  findViewById(R.id.empty_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        muser=new ArrayList<>();
        if (usersChatAdapter.getItemCount()==0) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }
        else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
            readUsers();

        }


    }

    private void readUsers(){

        final FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                muser.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    assert user != null;
                    assert firebaseUser != null;
                   if(!user.getUId().equals(firebaseUser.getUid())) {
                       muser.add(user);
                   }
                }
                usersChatAdapter=new UsersChatAdapter(UsersChatActivity.this,muser,false);
                    recyclerView.setAdapter(usersChatAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
