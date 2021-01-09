package com.example.qr_scanner_and_image_similarity_detection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SuccessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);

        Button sucess=(Button)findViewById(R.id.success_message_btn);
        sucess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(SuccessActivity.this,SigninActivity.class);
                startActivity(i);
            }
        });
    }
}
