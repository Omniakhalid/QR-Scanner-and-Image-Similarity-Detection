package com.example.qr_scanner_and_image_similarity_detection.activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.qr_scanner_and_image_similarity_detection.R;
import com.example.qr_scanner_and_image_similarity_detection.models.User;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class ProfileActivity extends AppCompatActivity {

    EditText Name;
    EditText Pass;
    EditText Phone;
    TextView Email;
    ImageView Qr_img;
    DatabaseReference reff;
    private FirebaseUser current_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

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


        current_user = FirebaseAuth.getInstance().getCurrentUser();
        GenerateQR(current_user.getUid());

        /*reff=  FirebaseDatabase.getInstance().getReference().child("Users").child("1");
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String email=dataSnapshot.child("email").getValue().toString();
                String name=dataSnapshot.child("name").getValue().toString();
                String pass=dataSnapshot.child("password").getValue().toString();
                String phone=dataSnapshot.child("phone").getValue().toString();

                Name.setText(name);
                Pass.setText(pass);
                Phone.setText(phone);
                Email.setText(email);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/

        NameEdt.setEnabled(false);
        PassEdt.setEnabled(false);
        NumberEdt.setEnabled(false);

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

               /* reff.child("Users").child("1").child("name").setValue(Name.getText());
                reff.child("Users").child("1").child("password").setValue(Pass.getText());
                reff.child("Users").child("1").child("phone").setValue(Phone.getText());
                reff.child("Users").child("1").child("email").setValue(Email.getText());*/

            }
        });
    }

    void GenerateQR(String id) {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(id, BarcodeFormat.QR_CODE, 500, 500);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            Qr_img.setImageBitmap(bitmap);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}