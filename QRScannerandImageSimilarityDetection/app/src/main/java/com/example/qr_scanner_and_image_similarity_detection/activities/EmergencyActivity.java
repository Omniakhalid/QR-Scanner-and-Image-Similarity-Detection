package com.example.qr_scanner_and_image_similarity_detection.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qr_scanner_and_image_similarity_detection.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import static com.example.qr_scanner_and_image_similarity_detection.activities.HomeActivity.QR_SCANNING_KEY;


public class EmergencyActivity extends AppCompatActivity {

    private Spinner spinner_type, spinner_category;
    private Button sendEmergency_btn;
    private EditText TextEmergency;
    private ArrayAdapter<CharSequence> adapter;
    private String StandardMessage = "";
    private String USER_ID;
    FirebaseUser fuser;
    EditText message1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);

        Init();


        spinner_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                // position 0 refer to message to select
                if (position == 0) {
                    spinner_category.setEnabled(false);
                }
                // position 1 refer to item
                else if (position == 1) {
                    AddValueToSpinner(R.array.ItemCategories);
                }
                // position 2 refer to car
                else if (position == 2) {
                    AddValueToSpinner(R.array.CarCategories);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int type_position = spinner_type.getSelectedItemPosition();

                if (i == 0) {
                    TextEmergency.getText().clear();
                } else {
                    // item
                    if (type_position == 1) {
                        StandardMessage = "Dear,\nI Found Your Lost Item";

                    }
                    // car
                    else if (type_position == 2) {
                        String selected = spinner_category.getSelectedItem().toString();
                        if (!selected.equals("other issue")) {
                            StandardMessage = "Dear,\n" + spinner_category.getSelectedItem().toString() + "\nThanks";

                        } else {
                            TextEmergency.getText().clear();
                        }
                    }
                    setEmergencyMessage(StandardMessage);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        fuser = FirebaseAuth.getInstance().getCurrentUser();
        message1 = findViewById(R.id.txt_message_emergency);
        sendEmergency_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int position = spinner_type.getSelectedItemPosition();
                if (position != 0) {
                    String message = TextEmergency.getText().toString();
                    if (!message.isEmpty()) {
                        ShowMessage(USER_ID);
                        String messag = message1.getText().toString();
                        if (!messag.equals("")) {
                            sendMessage(fuser.getUid(), USER_ID, messag);

                        } else {
                            Toast.makeText(getApplicationContext(), "you cant send empty message", Toast.LENGTH_LONG).show();
                        }
                        message1.setText("");

                        ShowMessage("Done ");
                    } else {
                        ShowMessage("Please, write Message ");
                    }
                } else {
                    ShowMessage("Please, Select Type of issue");
                }
            }
        });


    }

    private void AddValueToSpinner(int i) {
        spinner_category.setEnabled(true);
        adapter = ArrayAdapter.createFromResource(getApplicationContext(),
                i, android.R.layout.simple_spinner_item);
        spinner_category.setAdapter(adapter);
    }

    private void setEmergencyMessage(String s) {
        TextEmergency.getText().clear();
        TextEmergency.setText(s);
    }

    private void ShowMessage(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    private void Init() {
        spinner_category = findViewById(R.id.spinner_cate);
        spinner_type = findViewById(R.id.spinner_type);
        TextEmergency = findViewById(R.id.txt_message_emergency);
        sendEmergency_btn = findViewById(R.id.send_emergency_btn);
        USER_ID = getIntent().getStringExtra(QR_SCANNING_KEY);
        spinner_category.setEnabled(false);
    }

    private void sendMessage(String sender, String reciver, String message) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("reciver", reciver);
        hashMap.put("message", message);


        reference.child("Chat").push().setValue(hashMap);

    }


}