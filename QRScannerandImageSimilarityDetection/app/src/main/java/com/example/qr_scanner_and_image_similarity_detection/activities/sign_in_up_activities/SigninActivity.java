package com.example.qr_scanner_and_image_similarity_detection.activities.sign_in_up_activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.app.MediaRouteButton;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.qr_scanner_and_image_similarity_detection.Activation;
import com.example.qr_scanner_and_image_similarity_detection.MainActivity;
import com.example.qr_scanner_and_image_similarity_detection.R;
import com.example.qr_scanner_and_image_similarity_detection.activities.HomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SigninActivity extends AppCompatActivity {
    TextInputLayout email,pass;
    ImageView image;
    Button JoinBtn,forgotten_pass,sign_in;


    private String Login_mail,Login_pass;

    private FirebaseAuth auth;
    private FirebaseUser current_user;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Init();


        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login_mail = email.getEditText().getText().toString().trim();
                Login_pass = pass.getEditText().getText().toString().trim();
                if(!(Login_mail.equals("") && Login_pass.equals("")))
                {
                    SignIn();
                }else {
                    ShowMessage("Please Fill Empty Fields");
                }
            }
        });

        JoinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sign_up = new Intent(SigninActivity.this,SignupActivity.class);
                Pair[] pairs=new Pair[5];
                pairs[0]=new Pair<View,String>(image,"logo_image");
                pairs[1]=new Pair<View,String>(email,"trans_email");
                pairs[2]=new Pair<View,String>(pass,"trans_password");
                pairs[3]=new Pair<View,String>(JoinBtn,"trans_sign_btn");
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
                pairs[3]=new Pair<View,String>(JoinBtn,"trans_sign_btn");
                pairs[4]=new Pair<View,String>(forgotten_pass,"trans_forget_btn");
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation(SigninActivity.this,pairs);
                    startActivity(Forget_pass,options.toBundle());
                }
            }
        });
    }
    void Init(){
        auth = FirebaseAuth.getInstance();
        current_user = auth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        email =findViewById(R.id.sig_Email_editTxt);
        pass =findViewById(R.id.sig_password_editTxt);
        image =(ImageView)findViewById(R.id.logoImage);
        JoinBtn =(Button)findViewById(R.id.sig_join_btn);
        sign_in=(Button)findViewById(R.id.sig_signIn_btn);
        forgotten_pass=(Button)findViewById(R.id.sig_forget_pass_btn);
    }
    void SignIn(){
        auth.signInWithEmailAndPassword(Login_mail,Login_pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    if(FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()){
                        UpdatePass();
                        SendToActivity(HomeActivity.class);
                    }
                    else {
                        email.getEditText().getText().clear();
                        pass.getEditText().getText().clear();
                        SendToActivity(Activation.class);
                    }
                }else
                {
                    ShowMessage("Please Write correct Email and Password");
                }
            }
        });
    }

    void UpdatePass() {
        String usrpass = pass.getEditText().getText().toString().trim();
        HashMap map = new HashMap();
        map.put("password",usrpass);
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(map).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                ShowMessage("Welcome");
            }
        });
    }

    void SendToActivity(Class c){
        Intent i = new Intent(getApplicationContext(), c);
        startActivity(i);
        finish();
    }
    void ShowMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
