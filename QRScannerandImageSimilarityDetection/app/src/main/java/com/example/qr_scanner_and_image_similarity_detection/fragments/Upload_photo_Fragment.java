package com.example.qr_scanner_and_image_similarity_detection.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.example.qr_scanner_and_image_similarity_detection.FindOwner;
import com.example.qr_scanner_and_image_similarity_detection.R;
import com.example.qr_scanner_and_image_similarity_detection.activities.CropImage.CropPhotoActivity;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import static android.provider.MediaStore.ACTION_IMAGE_CAPTURE;


public class Upload_photo_Fragment extends Fragment {


    public static final String Key_CATEGORY="CAT";
    Bitmap bitmap;
    ImageView Item_img;
    Spinner Categories_spiner;
    ListView Mylist;
    CustomAdapter customadapter = null;

    // adel work
    private Button btn_crop;

    // integrate with python

    String imageString="";
    public int i=1;
    private String category;


    ArrayList<Bitmap> Images_lst = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

            Intent CameraIntent = new Intent(ACTION_IMAGE_CAPTURE);
            startActivityForResult(CameraIntent, 1888);


        View view = inflater.inflate(R.layout.fragment_upload_photo_, container, false);
        Item_img = view.findViewById(R.id.uploadfrag_ItmImg);
        Categories_spiner = view.findViewById(R.id.UploadFrag_Categ_spinner);

        // adel work
        btn_crop = view.findViewById(R.id.btn_crop_image);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.ItemCategories, android.R.layout.simple_spinner_dropdown_item);
        Categories_spiner.setAdapter(adapter);
        Categories_spiner.setPrompt("Select Category");

        Mylist = view.findViewById(R.id.cutting_imgs_lst);
        customadapter = new CustomAdapter(getContext(),R.layout.cutimg_layout,Images_lst);
        Mylist.setAdapter(customadapter);

        btn_crop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (GetSelectedGategory()) {

                    Intent cropActivity = new Intent(getActivity(), CropPhotoActivity.class);
                    cropActivity.putExtra(Key_CATEGORY,category);
                    startActivity(cropActivity);
                    getActivity().finish();

                }
                else{
                    ShowMessage("select category");
                }
            }
        });


        Mylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(GetSelectedGategory()) {
                    Intent intent = new Intent(getActivity().getApplicationContext(), FindOwner.class);
                    intent.putExtra("SelectedImage", Images_lst.get(position));
                    intent.putExtra("r", "notcrop");
                    //intent.putExtra("index",position);
                    intent.putExtra(Key_CATEGORY, category);
                    startActivity(intent);
                }else {
                    ShowMessage("select category");
                }
            }
        });

        return view;
    }

    private boolean GetSelectedGategory() {
         category = Categories_spiner.getSelectedItem().toString();
        if (category.equals("Select Category")){
            ShowMessage("Please Choose Category");
            return false;
        }
        else {
            return true;
        }
    }

    private void ShowMessage(String s) {
        Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1888 && resultCode == getActivity().RESULT_OK){
            bitmap = (Bitmap) data.getExtras().get("data");
            Item_img.setImageBitmap(bitmap);

            ConnectPython(bitmap);

          /*  //test to show images
            for(int i=0;i<5;i++){
                Images_lst.add(bitmap);
            }
            /*
           */
        }
    }

    //custom adapter for list to contain images after segmentation
    private class CustomAdapter extends ArrayAdapter<Bitmap> {

        public CustomAdapter(Context context, int textViewResourceId, ArrayList<Bitmap> objects) {
            super(context, textViewResourceId, objects);
        }

        @Override
        public int getCount() {
            return Images_lst.size();
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = null;
            LayoutInflater inflater = getLayoutInflater();
            view = inflater.inflate(R.layout.cutimg_layout, parent,false);

            ImageView Image = view.findViewById(R.id.img_cuting);
            Image.setImageBitmap(Images_lst.get(position));

            return view;
        }
    }



    private void ConnectPython(Bitmap bitmap){
        if(!Python.isStarted())
        {Python.start(new AndroidPlatform(getContext()));}

        final Python py =Python.getInstance();
        imageString=getImageString(bitmap);
        //pass imageString to python script
        final PyObject pyobj=py.getModule("script");
        final PyObject obj=pyobj.callAttr("main",imageString);
        final String str=obj.toString();
        final String[] items = str.split(" nazmy ");

        if (items.length>0){
            btn_crop.setVisibility(View.VISIBLE);
        }

        if(i<items.length) {

            for (int j = 0; j < items.length; j++) {
                byte data[] = android.util.Base64.decode(items[j], Base64.DEFAULT);
                Bitmap btm = BitmapFactory.decodeByteArray(data, 0, data.length);
                Images_lst.add(btm);

                //  after.setImageBitmap(btm);
            }
        }
        else{
            Toast.makeText(getContext(),"Segment can't find many items",Toast.LENGTH_LONG).show();
            Images_lst.add(bitmap);
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


}