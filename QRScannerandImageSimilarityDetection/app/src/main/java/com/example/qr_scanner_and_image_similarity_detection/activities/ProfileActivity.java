package com.example.qr_scanner_and_image_similarity_detection.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qr_scanner_and_image_similarity_detection.R;
import com.google.android.material.textfield.TextInputLayout;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        TextInputLayout NameEdt =findViewById(R.id.Pro_Name_edTxt);
        TextInputLayout PassEdt =findViewById(R.id.Pro_Pass_edTxt);
        TextInputLayout NumberEdt =findViewById(R.id.Pro_Number_edTxt);

        ImageView EditNameButton=findViewById(R.id.Pro_EditNameBtn);
        ImageView EditPassButton=findViewById(R.id.Pro_EditPassBtn);
        ImageView EditNunberButton=findViewById(R.id.Pro_EditNunberBtn);
        Button SaveButton=findViewById(R.id.Pro_SaveBtn);

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
            }
        });
    }
}