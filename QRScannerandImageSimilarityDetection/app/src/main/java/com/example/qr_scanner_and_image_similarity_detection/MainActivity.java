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

import com.example.qr_scanner_and_image_similarity_detection.activities.sign_in_up_activities.SigninActivity;

public class MainActivity extends AppCompatActivity {

    private static int splash_time=4000;
    Animation top_anim,bottom_anim;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ImageView image=findViewById(R.id.imageView);
        TextView txt=(TextView)findViewById(R.id.textView);
        top_anim= AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottom_anim= AnimationUtils.loadAnimation(this,R.anim.bottom_animation);
        image.setAnimation(top_anim);
        txt.setAnimation(bottom_anim);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent login=new Intent(MainActivity.this, SigninActivity.class);
                Pair[] pairs=new Pair[1];
                pairs[0]=new Pair<View,String>(image,"logo_image");
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation(MainActivity.this,pairs);
                    startActivity(login,options.toBundle());
                }
            }
        },splash_time);
    }
}
