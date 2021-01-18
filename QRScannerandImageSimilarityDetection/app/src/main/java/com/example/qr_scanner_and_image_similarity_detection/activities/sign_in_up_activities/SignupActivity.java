package com.example.qr_scanner_and_image_similarity_detection.activities.sign_in_up_activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.qr_scanner_and_image_similarity_detection.R;
import com.example.qr_scanner_and_image_similarity_detection.activities.HomeActivity;
import com.example.qr_scanner_and_image_similarity_detection.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class SignupActivity extends AppCompatActivity {

    TextInputEditText UserName;
    TextInputEditText UserPass;
    TextInputEditText UserConfPass;
    TextInputEditText UserEmail;
    TextInputEditText UserPhone;
    RadioButton Male;
    RadioButton Female;
    Button signup_btn;

    private User user;
    private String name;
    private String email;
    private String password;
    private String confpass;
    private String phone;
    private String gender;

    private FirebaseAuth auth;
    private FirebaseUser current_user;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Init();
        auth = FirebaseAuth.getInstance();
        current_user = auth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = UserName.getText().toString();
                email = UserEmail.getText().toString();
                password = UserPass.getText().toString();
                confpass = UserConfPass.getText().toString();
                phone = UserPhone.getText().toString();
                if (Male.isChecked()) {
                    gender = "male";
                } else if (Female.isChecked()) {
                    gender = "female";
                }

                if (!(name.equals("") && email.equals("") && password.equals("") && confpass.equals("") && phone.equals("") && gender.equals(""))) {
                    if (password.equals(confpass)) {
                        CreateUser();
                        SignUp();
                    } else {
                        ShowMessage("Please Write same password");
                    }
                }
                else {
                        ShowMessage("Fill all Empty fields");
                }
            }
        });
    }

    void Init() {
        user = new User();

        signup_btn = findViewById(R.id.signup_btn);
        UserName = findViewById(R.id.UserName_txtview);
        UserPass = findViewById(R.id.Password_txtview);
        UserConfPass = findViewById(R.id.conf_pass_txtview);
        UserEmail = findViewById(R.id.Email_txtview);
        UserPhone = findViewById(R.id.Phone_txtview);
        Male = findViewById(R.id.male_radioBtn);
        Female = findViewById(R.id.female_radioBtn);

    }

    void SignUp() {
        auth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    auth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            ShowMessage("Confirmation Email Was Sent");
                            //GenerateQR();
                            current_user = FirebaseAuth.getInstance().getCurrentUser();
                            databaseReference.child("Users").child(current_user.getUid()).setValue(user);
                            ShowMessage("Registration Success");
                            SendToActivity(SigninActivity.class);
                        }
                    });
                } else {
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthUserCollisionException existEmail) {
                        ShowMessage("Email is Exist");
                    } catch (Exception e) {
                        ShowMessage("Unexpected Error!");
                    }
                }
            }
        });
    }

    void ShowMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    void CreateUser() {
        user.setName(name);
        user.setPassword(password);
        user.setEmail(email);
        user.setPhone(phone);
        user.setGender(gender);
    }

    void GenerateQR() {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(current_user.getUid(), BarcodeFormat.QR_CODE, 500, 500);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            user.setQR(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    void SendToActivity(Class c){
        Intent i = new Intent(getApplicationContext(), c);
        startActivity(i);
        finish();
    }
}
