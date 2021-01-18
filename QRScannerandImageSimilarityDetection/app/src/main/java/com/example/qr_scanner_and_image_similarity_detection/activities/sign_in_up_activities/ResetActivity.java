package com.example.qr_scanner_and_image_similarity_detection.activities.sign_in_up_activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.qr_scanner_and_image_similarity_detection.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class ResetActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    Button Sendmail;
    TextInputLayout EmailForReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Sendmail = findViewById(R.id.forgetpass_act_Sendmail_btn);
        EmailForReset = findViewById(R.id.res_Email_editTxt);

        auth = FirebaseAuth.getInstance();

        Sendmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String InputMail = EmailForReset.getEditText().getText().toString().trim();

                if(!InputMail.equals(""))
                {
                    auth.sendPasswordResetEmail(InputMail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                ShowMessage("Reset Password Was Sent to " + InputMail);
                                SendToActivity(SigninActivity.class);
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            ShowMessage(InputMail + " Is Not Found");
                        }
                    });
                }
                else {
                    ShowMessage("Enter Your Email");
                }
            }
        });
    }
    void ShowMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
    void SendToActivity(Class c){
        Intent i = new Intent(getApplicationContext(), c);
        startActivity(i);
        finish();
    }
}
