package com.example.qr_scanner_and_image_similarity_detection.activities.sign_in_up_activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.example.qr_scanner_and_image_similarity_detection.R;

public class verificationCodeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_code);
        Button btn =findViewById(R.id.ver_code_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent new_pass=new Intent(verificationCodeActivity.this,NewpassActivity.class);
                startActivity(new_pass);
            }
        });
    }
}
