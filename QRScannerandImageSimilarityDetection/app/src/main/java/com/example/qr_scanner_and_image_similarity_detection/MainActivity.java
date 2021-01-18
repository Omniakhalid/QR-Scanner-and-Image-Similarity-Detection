package com.example.qr_scanner_and_image_similarity_detection;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qr_scanner_and_image_similarity_detection.activities.HomeActivity;
import com.example.qr_scanner_and_image_similarity_detection.activities.sign_in_up_activities.SigninActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP;

public class MainActivity extends AppCompatActivity {

    private static int splash_time=4000;
    Animation top_anim,bottom_anim;
    TextView txt;
    ImageView image;
    Intent login;

    private FirebaseAuth auth;
    private FirebaseUser current_user;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Init();
        image.setAnimation(top_anim);
        txt.setAnimation(bottom_anim);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ChooseActivity();
                Pair[] pairs=new Pair[1];
                pairs[0]=new Pair<View,String>(image,"logo_image");
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation(MainActivity.this,pairs);
                    startActivity(login,options.toBundle());
                    finish();
                }
            }
        },splash_time);
    }
    void Init(){
        auth = FirebaseAuth.getInstance();
        current_user = auth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        image = findViewById(R.id.imageView);
        txt = findViewById(R.id.textView);
        top_anim= AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottom_anim= AnimationUtils.loadAnimation(this,R.anim.bottom_animation);
    }

    void ChooseActivity(){
        if (current_user != null) {
        //    if(current_user.isEmailVerified()){
                  login = new Intent(MainActivity.this, HomeActivity.class);
        //    }else {
        //        login = new Intent(MainActivity.this,Activation.class);
        //    }
        } else {
            login = new Intent(MainActivity.this, SigninActivity.class);
        }

    }


}
