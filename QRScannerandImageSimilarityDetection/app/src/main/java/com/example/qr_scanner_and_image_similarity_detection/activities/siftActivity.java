package com.example.qr_scanner_and_image_similarity_detection.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.widget.Toast;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.example.qr_scanner_and_image_similarity_detection.R;

import java.io.ByteArrayOutputStream;
import java.util.List;

import static com.example.qr_scanner_and_image_similarity_detection.activities.CropImage.CropPhotoActivity.KEY_OF_SEND_TO_FIND_OWNER;
import static com.example.qr_scanner_and_image_similarity_detection.fragments.Upload_photo_Fragment.Key_CATEGORY;

public class siftActivity extends AppCompatActivity {

    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sift);

        String cat_name = getIntent().getStringExtra(Key_CATEGORY);
        Toast.makeText(this, cat_name, Toast.LENGTH_SHORT).show();
        Uri ImageURi = getIntent().getParcelableExtra(KEY_OF_SEND_TO_FIND_OWNER);
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), ImageURi);

        }catch (Exception e) {
            Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
        }
        ConnectPython(bitmap,cat_name);
    }
    private void ConnectPython(Bitmap bitmap, String cat_Name) {
        if (!Python.isStarted()) {
            Python.start(new AndroidPlatform(getApplicationContext()));
        }

        final Python py =Python.getInstance();
       String imageString=getImageString(bitmap);
        //pass imageString to python script
        final PyObject pyobj=py.getModule("scriptSift");
        final PyObject obj=pyobj.callAttr("main",imageString,cat_Name);
        final String str=obj.toString();
        final String[] items = str.split(" nazmy ");
        Toast.makeText(this, items[0], Toast.LENGTH_SHORT).show();

        // final String[] items = s.split("with");

      //  Toast.makeText(this, items[1]+"", Toast.LENGTH_SHORT).show();

/*
        if(i<items.length) {

            for (int j = 0; j < items.length; j++) {
                byte data[] = android.util.Base64.decode(items[j], Base64.DEFAULT);
                Bitmap btm = BitmapFactory.decodeByteArray(data, 0, data.length);
                Images_lst.add(btm);

                //  after.setImageBitmap(btm);
            }
        }
        else
            Toast.makeText(getContext(),"stop",Toast.LENGTH_LONG).show();
*/
    }

    public String getImageString(Bitmap bitmap)
    {
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,baos);
        //store in byte array
        byte[] imagebytes=baos.toByteArray();
        //encode to string
        String encodedimage=android.util.Base64.encodeToString(imagebytes, Base64.DEFAULT);
        return  encodedimage;
    }
}