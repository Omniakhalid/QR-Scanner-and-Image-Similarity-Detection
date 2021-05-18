package com.example.qr_scanner_and_image_similarity_detection.activities.CropImage;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qr_scanner_and_image_similarity_detection.R;
import com.example.qr_scanner_and_image_similarity_detection.activities.siftActivity;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import static com.example.qr_scanner_and_image_similarity_detection.fragments.Upload_photo_Fragment.Key_CATEGORY;

public class CropPhotoActivity extends AppCompatActivity {

    private ImageButton btBrowse, btReset;
    private ImageView imageView;
    private Button btn_upload_image;
    Uri resultUri, uriData;

    public static final String KEY_OF_SEND_TO_FIND_OWNER = "KEY_OWNER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_photo);

        Init();


        btBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startCrop();
            }
        });

        btReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView.setImageBitmap(null);
            }
        });

        btn_upload_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
/*
                    Intent find_owner_activity = new Intent(CropPhotoActivity.this, FindOwner.class);
                    find_owner_activity.putExtra(KEY_OF_SEND_TO_FIND_OWNER,uriData);
                    find_owner_activity.putExtra("r","crop");
                    startActivity(find_owner_activity);
                    finish();
 */
                String k = getIntent().getStringExtra(Key_CATEGORY);

                Intent intent = new Intent(CropPhotoActivity.this, siftActivity.class);
                intent.putExtra(KEY_OF_SEND_TO_FIND_OWNER,uriData);
                intent.putExtra(Key_CATEGORY,k);
                startActivity(intent);

            }
        });
    }

    private void Init() {
        btBrowse = findViewById(R.id.bt_borwse);
        btReset = findViewById(R.id.bt_reset);
        imageView = findViewById(R.id.image_view);
        btn_upload_image = findViewById(R.id.btn_upload_cropedPhoto);
    }

    private void startCrop() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(CropPhotoActivity.this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                uriData = result.getUri();
                imageView.setImageURI(result.getUri());
                btn_upload_image.setVisibility(View.VISIBLE);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}