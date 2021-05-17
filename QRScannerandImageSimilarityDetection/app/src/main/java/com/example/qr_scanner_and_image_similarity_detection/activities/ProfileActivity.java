package com.example.qr_scanner_and_image_similarity_detection.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.qr_scanner_and_image_similarity_detection.R;
import com.example.qr_scanner_and_image_similarity_detection.adapters.UsersChatAdapter;
import com.example.qr_scanner_and_image_similarity_detection.models.User;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    EditText Name;
    EditText Pass;
    EditText Phone;
    TextView Email;
    ImageButton Qr_img;
    DatabaseReference reff;
    boolean blockUser=false;


    private TextView UserName;
    private FirebaseUser current_user;
    private List<User> currenInfo;

    String Gender="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        current_user = FirebaseAuth.getInstance().getCurrentUser();
        init();

        currenInfo=new ArrayList<>();
        sentData();

    }

    private void init(){

        TextInputLayout NameEdt = findViewById(R.id.Pro_Name_edTxt);
        TextInputLayout PassEdt = findViewById(R.id.Pro_Pass_edTxt);
        TextInputLayout NumberEdt = findViewById(R.id.Pro_Number_edTxt);

        ImageView EditNameButton = findViewById(R.id.Pro_EditNameBtn);
        ImageView EditPassButton = findViewById(R.id.Pro_EditPassBtn);
        ImageView EditNunberButton = findViewById(R.id.Pro_EditNunberBtn);
        Button SaveButton = findViewById(R.id.Pro_SaveBtn);

        Name = findViewById(R.id.NameEditText);
        Pass = findViewById(R.id.PassEditText);
        Phone = findViewById(R.id.NumberEditText);
        Email = findViewById(R.id.EmailEditText);
        Qr_img = findViewById(R.id.QR_Image);
        UserName = findViewById(R.id.txt_user_name);

        NameEdt.setEnabled(false);
        PassEdt.setEnabled(false);
        NumberEdt.setEnabled(false);


       reff=FirebaseDatabase.getInstance().getReference("Users");

        Qr_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),DownloadImageActivity.class);
                intent.putExtra("userid",current_user.getUid());
                startActivity(intent);
            }
        });


        EditNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NameEdt.setEnabled(true);
                NameEdt.setFocusableInTouchMode(true);

            }
        });

        EditPassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PassEdt.setEnabled(true);
                PassEdt.setFocusableInTouchMode(true);
            }
        });

        EditNunberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NumberEdt.setEnabled(true);
                NumberEdt.setFocusableInTouchMode(true);
            }
        });

        SaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NameEdt.setEnabled(false);
                PassEdt.setEnabled(false);
                NumberEdt.setEnabled(false);

                UserName.setText(Name.getText().toString());

                User Updateuser=new User();
                Updateuser.setEmail(Email.getText().toString());
                Updateuser.setGender(Gender);
                Updateuser.setName(Name.getText().toString());
                Updateuser.setPassword(Pass.getText().toString());
                Updateuser.setPhone(Phone.getText().toString());
                Updateuser.setUId(current_user.getUid());
                Updateuser.setBlockUser(blockUser);

                reff.child(current_user.getUid()).setValue(Updateuser);

                Toast.makeText(ProfileActivity.this, "Updated Successfully..", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void sentData() {
        final FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currenInfo.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    assert user != null;
                    assert firebaseUser != null;
                    if(user.getUId().equals(firebaseUser.getUid())) {
                        currenInfo.add(user);
                    }
                }
                String email=currenInfo.get(0).getEmail();
                String name=currenInfo.get(0).getName();
                String pass=currenInfo.get(0).getPassword();
                String phone=currenInfo.get(0).getPhone();
                Gender=currenInfo.get(0).getGender();
                blockUser=currenInfo.get(0).isBlockUser();

                Name.setText(name);
                UserName.setText(name);
                Pass.setText(pass);
                Phone.setText(phone);
                Email.setText(email);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}