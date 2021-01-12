package com.example.qr_scanner_and_image_similarity_detection.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qr_scanner_and_image_similarity_detection.R;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        EditText NameEdt =findViewById(R.id.Pro_Name_edTxt);
        EditText PassEdt =findViewById(R.id.Pro_Pass_edTxt);
        EditText NumberEdt =findViewById(R.id.Pro_Number_edTxt);

        Button EditNameButton=findViewById(R.id.Pro_EditNameBtn);
        Button EditPassButton=findViewById(R.id.Pro_EditPassBtn);
        Button EditNunberButton=findViewById(R.id.Pro_EditNunberBtn);
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