package com.example.qr_scanner_and_image_similarity_detection;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class Myitems extends AppCompatActivity {

    public RecyclerView MyRecyclerView;
    public RecyclerView.Adapter myResAdapter;
    public RecyclerView.LayoutManager myResLaoutMan;


    ImageView itmImag ;
    final static int GALLERY_REQUEST_CODE = 101;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myitems);

        EditText decre=findViewById(R.id.itm_Add_descrep);
        Spinner cateSpin=findViewById(R.id.Item_Car_spinner);
        itmImag =findViewById(R.id.Itm_itemimg);


        ArrayList<ItemCardClass> ItemList =new ArrayList<>();


        MyRecyclerView =findViewById(R.id.Recclarvie);
        myResLaoutMan=new LinearLayoutManager(this);
        myResAdapter=new com.example.qr_scanner_and_image_similarity_detection.MyItemAdapter(ItemList);
        MyRecyclerView.setLayoutManager(myResLaoutMan);
        MyRecyclerView.setAdapter(myResAdapter);


        Button addBtn =findViewById(R.id.item_AddBtn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemList.add(new ItemCardClass(decre.getText().toString(),"Category is : "+ cateSpin.getSelectedItem().toString(), bitmap));
                myResAdapter.notifyDataSetChanged();
            }
        });


        itmImag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {

            Uri uri = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                bitmap = BitmapFactory.decodeStream(inputStream);
                itmImag.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    protected void chooseImage() {
        //Create an Intent with action as ACTION_PICK
        Intent intent = new Intent(Intent.ACTION_PICK);
        // Sets the type as image/*. This ensures only components of type image are selected
        intent.setType("image/*");
        // Launching the Intent
        startActivityForResult(intent, GALLERY_REQUEST_CODE);

    }


}