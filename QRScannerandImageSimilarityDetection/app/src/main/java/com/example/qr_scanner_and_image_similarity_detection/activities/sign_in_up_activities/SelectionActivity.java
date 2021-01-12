package com.example.qr_scanner_and_image_similarity_detection.activities.sign_in_up_activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import com.example.qr_scanner_and_image_similarity_detection.R;


public class SelectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Button mail=(Button)findViewById(R.id.vrify_mail);
        Button phone=(Button)findViewById(R.id.vrify_phone);
        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(SelectionActivity.this,verificationCodeActivity.class);
                startActivity(i);
            }
        });
        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ii=new Intent(SelectionActivity.this,verificationCodeActivity.class);
                startActivity(ii);
            }
        });

    }
}
