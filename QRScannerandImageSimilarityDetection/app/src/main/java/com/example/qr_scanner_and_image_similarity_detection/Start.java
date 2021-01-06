package com.example.qr_scanner_and_image_similarity_detection;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class Start extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);


        Button ScanQR_btn = findViewById(R.id.Main_ScanQR_btn);
        ScanQR_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Scan();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                ScanQR scanQR = new ScanQR();
                fragmentTransaction.replace(R.id.fragment_Container,scanQR);
                fragmentTransaction.commit();
            }
        });

        Button Upload_btn = findViewById(R.id.Main_Upload_btn);
        Upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Upload UploadFrag = new Upload();
                fragmentTransaction.replace(R.id.fragment_Container,UploadFrag);
                fragmentTransaction.commit();
            }
        });
    }

    private void Scan() {
        IntentIntegrator integrator = new IntentIntegrator(Start.this);
        integrator.setOrientationLocked(true);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        integrator.setPrompt("Scaning Code...");
        integrator.setCameraId(0);
        integrator.setCaptureActivity(Capture.class);
        integrator.initiateScan();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                //scan have an error
                Toast.makeText(Start.this, "Cancelled", Toast.LENGTH_SHORT).show();
            } else {
                //scan is successful
                Toast.makeText(Start.this, "Successful", Toast.LENGTH_SHORT).show();
                //send QR data to emergency call activity..
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}