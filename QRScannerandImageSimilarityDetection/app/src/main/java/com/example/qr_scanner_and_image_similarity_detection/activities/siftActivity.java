package com.example.qr_scanner_and_image_similarity_detection.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.example.qr_scanner_and_image_similarity_detection.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.util.List;

import static com.example.qr_scanner_and_image_similarity_detection.activities.CropImage.CropPhotoActivity.KEY_OF_SEND_TO_FIND_OWNER;
import static com.example.qr_scanner_and_image_similarity_detection.fragments.Upload_photo_Fragment.Key_CATEGORY;

public class siftActivity extends AppCompatActivity {

    Bitmap bitmap;
    private List<PyObject> objectList;
    private DatabaseReference mReference ;
    String cat_name;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sift);

        mReference = FirebaseDatabase.getInstance().getReference().child("LostItems").child("Mobiles");
        imageView = findViewById(R.id.siftimage);


        Button b = findViewById(R.id.button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cat_name = getIntent().getStringExtra(Key_CATEGORY);
                Uri ImageURi = getIntent().getParcelableExtra(KEY_OF_SEND_TO_FIND_OWNER);
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), ImageURi);

                }catch (Exception e) {
                 //   Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
                }
                ConnectPython(bitmap,cat_name);
            }
        });


    }
    private void ConnectPython(Bitmap bitmap, String cat_Name) {
        if (!Python.isStarted()) {
            Python.start(new AndroidPlatform(siftActivity.this));
        }


        final Python py =Python.getInstance();
        String imageString=getImageString(bitmap);
        //pass imageString to python script
        final PyObject pyobj=py.getModule("scriptSift");
        final PyObject obj=pyobj.callAttr("main",imageString,cat_Name);
        final List<PyObject> rows=obj.asList();

        for (int i=0 ;i<rows.size();i++){
            String o = rows.get(i).toString();
            String attr[] = o.split("with");

            mReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()) {

                        String id = snapshot.child("item_ID").getValue().toString();

                        if (id.equals(attr[2])) {

                           String message = snapshot.child("imageLosted").getValue().toString();
                           Bitmap c=  decodeBase64(message);
                           imageView.setImageBitmap(c);
                           Toast.makeText(siftActivity.this, message, Toast.LENGTH_SHORT).show();

                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

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


    private Bitmap decodeBase64(String input){
        byte[] decodedString = Base64.decode(input, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }

}