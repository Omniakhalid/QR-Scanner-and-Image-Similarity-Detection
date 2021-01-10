package com.example.qr_scanner_and_image_similarity_detection.sign_in_up_activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import com.example.qr_scanner_and_image_similarity_detection.R;
import com.example.qr_scanner_and_image_similarity_detection.activities.HomeActivity;
import com.google.android.material.textfield.TextInputLayout;

public class SigninActivity extends AppCompatActivity {
    TextInputLayout email,pass;
    ImageView image;
    Button signup,forgotten_pass,sign_in;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        email =findViewById(R.id.sig_Email_editTxt);
        pass =findViewById(R.id.sig_password_editTxt);
        image =(ImageView)findViewById(R.id.logoImage);
        signup=(Button)findViewById(R.id.sig_join_btn);
        sign_in=(Button)findViewById(R.id.sig_signIn_btn);
        forgotten_pass=(Button)findViewById(R.id.sig_forget_pass_btn);

        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sign_in_intent = new Intent(SigninActivity.this, HomeActivity.class);
                startActivity(sign_in_intent);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sign_up = new Intent(SigninActivity.this,SignupActivity.class);
                Pair[] pairs=new Pair[5];
                pairs[0]=new Pair<View,String>(image,"logo_image");
                pairs[1]=new Pair<View,String>(email,"trans_email");
                pairs[2]=new Pair<View,String>(pass,"trans_password");
                pairs[3]=new Pair<View,String>(signup,"trans_sign_btn");
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
                pairs[3]=new Pair<View,String>(signup,"trans_sign_btn");
                pairs[4]=new Pair<View,String>(forgotten_pass,"trans_forget_btn");
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation(SigninActivity.this,pairs);
                    startActivity(Forget_pass,options.toBundle());
                }
            }
        });
    }
}
