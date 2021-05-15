package com.example.qr_scanner_and_image_similarity_detection.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.qr_scanner_and_image_similarity_detection.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class DownloadImageActivity extends AppCompatActivity {

    private ImageView QrImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_image);

        QrImage = findViewById(R.id.qrImage);
        String id = getIntent().getStringExtra("userid");
        GenerateQR(id);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.download_image_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_download_option:
                saveImageToGallery();
                Toast.makeText(this, "downloaded successfully in gallery ....", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }




    private void saveImageToGallery() {
        QrImage.setDrawingCacheEnabled(true);
        Bitmap b = QrImage.getDrawingCache();
        MediaStore.Images.Media.insertImage(getApplicationContext().getContentResolver(),
                b, "PersonalQr", "put it on all the special things to protect it from lost  ");
    }

    void GenerateQR(String id) {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(id, BarcodeFormat.QR_CODE, 500, 500);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            QrImage.setImageBitmap(bitmap);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}