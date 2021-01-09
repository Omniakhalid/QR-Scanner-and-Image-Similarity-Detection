package com.example.qr_scanner_and_image_similarity_detection;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

public class SigninActivity extends AppCompatActivity {
    TextInputLayout email,pass;
    ImageView image;
    Button sign,forgotten_pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        email =findViewById(R.id.sig_Email_editTxt);
        pass =findViewById(R.id.sig_password_editTxt);
        image =(ImageView)findViewById(R.id.logoImage);
        sign=(Button)findViewById(R.id.sig_join_btn);
        forgotten_pass=(Button)findViewById(R.id.sig_forget_pass_btn);

        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sign_up = new Intent(SigninActivity.this,SignupActivity.class);
                Pair[] pairs=new Pair[5];
                pairs[0]=new Pair<View,String>(image,"logo_image");
                pairs[1]=new Pair<View,String>(email,"trans_email");
                pairs[2]=new Pair<View,String>(pass,"trans_password");
                pairs[3]=new Pair<View,String>(sign,"trans_sign_btn");
                pairs[4]=new Pair<View,String>(forgotten_pass,"trans_forget_btn");
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation(SigninActivity.this,pairs);
                    startActivity(sign_up,options.toBundle());
                }

            }
        });

        forgotten_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Forget_pass = new Intent(SigninActivity.this,ResetActivity.class);
                Pair[] pairs=new Pair[5];
                pairs[0]=new Pair<View,String>(image,"logo_image");
                pairs[1]=new Pair<View,String>(email,"trans_email");
                pairs[2]=new Pair<View,String>(pass,"trans_password");
                pairs[3]=new Pair<View,String>(sign,"trans_sign_btn");
                pairs[4]=new Pair<View,String>(forgotten_pass,"trans_forget_btn");
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation(SigninActivity.this,pairs);
                    startActivity(Forget_pass,options.toBundle());
                }
            }
        });
    }
}
