package com.example.qr_scanner_and_image_similarity_detection.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.example.qr_scanner_and_image_similarity_detection.R;

import java.io.ByteArrayOutputStream;

public class TestActivity extends AppCompatActivity {
    ImageView befor;
    ImageView after;
    Button btn;
    BitmapDrawable drawable;
    Bitmap bitmap;
    String imageString="";
    public int i=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        befor=(ImageView)findViewById(R.id.imageView1);
        after=(ImageView)findViewById(R.id.imageView2);
        btn=(Button)findViewById(R.id.button);

        if(!Python.isStarted())
        {Python.start(new AndroidPlatform(this));}

        final Python py =Python.getInstance();
        drawable=(BitmapDrawable)befor.getDrawable();
        bitmap=drawable.getBitmap();
        imageString=getImageString(bitmap);
        //pass imageString to python script
        final PyObject pyobj=py.getModule("script");
        final PyObject obj=pyobj.callAttr("main",imageString);
        final String str=obj.toString();
        final String[] items = str.split(" nazmy ");

        Toast.makeText(this, items[0], Toast.LENGTH_SHORT).show();
        //byte data[]=android.util.Base64.decode(str,Base64.DEFAULT);
        /*byte data[]=android.util.Base64.decode(items[0],Base64.DEFAULT);
        Bitmap btm= BitmapFactory.decodeByteArray(data,0,data.length);
        after.setImageBitmap(btm);*/
     /*   btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(i<items.length){
                    byte data[]=android.util.Base64.decode(items[i], Base64.DEFAULT);
                    Bitmap btm= BitmapFactory.decodeByteArray(data,0,data.length);
                    after.setImageBitmap(btm);
                    i++;}
                else
                    Toast.makeText(getApplicationContext(),"stop",Toast.LENGTH_LONG).show();
            }
        });

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