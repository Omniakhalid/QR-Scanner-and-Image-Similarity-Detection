package com.example.qr_scanner_and_image_similarity_detection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.ArrayList;

import static com.example.qr_scanner_and_image_similarity_detection.activities.CropImage.CropPhotoActivity.KEY_OF_SEND_TO_FIND_OWNER;

public class FindOwner extends AppCompatActivity {

    ArrayList<Integer> ownersimg = new ArrayList<>();
    ArrayList<Integer> owners = new ArrayList<>();
    CustomAdapter customadapter;
    Button search_btn;
    ImageView Image, selecteditem_img;
    boolean searched;

    private   Uri ImageURi;
    private     Bitmap selectedimg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_owner);
        INIT();
        GetImage_Cropped();


        int indx = getIntent().getIntExtra("index",-1);

        Toast.makeText(this, "Here " + indx, Toast.LENGTH_SHORT).show();

        customadapter = new CustomAdapter(this,R.layout.ownerlst_layout,owners);
        ListView ownerslst = findViewById(R.id.owners_lst);
        ownerslst.setAdapter(customadapter);


        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FindOwner.this, "Searching..", Toast.LENGTH_SHORT).show();
                searched = true;
                owners.addAll(ownersimg);
                customadapter.notifyDataSetChanged();
            }
        });
    }

    //here replace integer with owners data list
    private class CustomAdapter extends ArrayAdapter<Integer> {

        public CustomAdapter(Context context, int textViewResourceId, ArrayList<Integer> objects) {
            super(context, textViewResourceId, objects);
        }

        @Override
        public int getCount() {
            //owners recomended counts
            return 5;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = null;
            LayoutInflater inflater = getLayoutInflater();
            view = inflater.inflate(R.layout.ownerlst_layout, parent,false);

            TextView similarity_res = view.findViewById(R.id.similarityRes_txt);
            TextView similaritytxt = view.findViewById(R.id.similarity_txt);
            TextView recomendtxt = view.findViewById(R.id.recomendowner_txt);
            TextView chathinttxt = view.findViewById(R.id.chathint_txt);
            Image = view.findViewById(R.id.Owner_img);

            //set data after searching
            if(searched) {
                Image.setImageResource(ownersimg.get(0));
                recomendtxt.setText("Recomended Owner");
                chathinttxt.setText("Chat To Contact with him");
                similaritytxt.setText("Similarity:");
                similarity_res.setText("100%");
            }
            return view;
        }
    }

    private void GetImage_Cropped(){

        String checkIntent = getIntent().getStringExtra("r");

        if (checkIntent.equals("crop")){
            // get image from crop activity
            ImageURi = getIntent().getParcelableExtra(KEY_OF_SEND_TO_FIND_OWNER);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), ImageURi);
                selecteditem_img.setImageBitmap(bitmap);


            }catch (Exception e) {
            }

           // selecteditem_img.setImageURI(ImageURi);
        }
        else {
            selectedimg = getIntent().getParcelableExtra("SelectedImage");
            selecteditem_img.setImageBitmap(selectedimg);
        }

    }

    private void INIT(){
        ownersimg.add(R.drawable.user);
        search_btn = findViewById(R.id.findowner_search_btn);
        selecteditem_img = findViewById(R.id.selecteditem_img);

    }

}