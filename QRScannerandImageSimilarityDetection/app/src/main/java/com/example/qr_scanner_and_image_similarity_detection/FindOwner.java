package com.example.qr_scanner_and_image_similarity_detection;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.example.qr_scanner_and_image_similarity_detection.models.LostImageModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static com.example.qr_scanner_and_image_similarity_detection.activities.CropImage.CropPhotoActivity.KEY_OF_SEND_TO_FIND_OWNER;
import static com.example.qr_scanner_and_image_similarity_detection.fragments.Upload_photo_Fragment.Key_CATEGORY;

public class FindOwner extends AppCompatActivity {
    FirebaseUser fuser;
    ArrayList<Integer> ownersimg = new ArrayList<>();
    ArrayList<Integer> owners = new ArrayList<>();

    List<Bitmap> list = new ArrayList<>();
    CustomAdapter customadapter;
    Button search_btn;
    ImageView Image, selecteditem_img;
    boolean searched;

    private Uri ImageURi;
    private Bitmap selectedimg;

    // work with sift
    private String Cat_name, checkIntent;
    private Bitmap bitmap;
    private List<PyObject> objectList;
    private DatabaseReference mReference;


    private ProgressDialog progressDialog;
    Map <String,String[]> SuggestUser;

    List<LostImageModel> lostImageModels;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_owner);

        INIT();
        GetImage_Cropped();


        customadapter = new CustomAdapter(this, R.layout.ownerlst_layout, lostImageModels);
        ListView ownerslst = findViewById(R.id.owners_lst);
        ownerslst.setAdapter(customadapter);

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ConnectPython();
                Collections.sort(lostImageModels);
                customadapter.notifyDataSetChanged();
                searched = true;

               // progressDialog.dismiss();
            }
        });
    }


    public void setProgress() {

        progressDialog = new ProgressDialog(FindOwner.this);
        progressDialog.setMessage("Please, wait to Find similar Image ..."); // Setting Message
        progressDialog.setTitle("Find Lost Item "); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.setCancelable(false);
        progressDialog.show(); // Display Progress Dialog

    }


    //here replace integer with owners data list
    private class CustomAdapter extends ArrayAdapter<LostImageModel> {

        List<LostImageModel> modelList;
        public CustomAdapter(@NonNull Context context, int resource,List<LostImageModel> models) {
            super(context, resource,models);
            modelList =models;
        }

        @Override
        public int getCount() {
            //owners recomended counts
            return modelList.size();
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = null;
            LayoutInflater inflater = getLayoutInflater();
            view = inflater.inflate(R.layout.ownerlst_layout, parent, false);

            TextView similarity_res = view.findViewById(R.id.similarityRes_txt);
            TextView similaritytxt = view.findViewById(R.id.similarity_txt);
            TextView recomendtxt = view.findViewById(R.id.recomendowner_txt);
            TextView chathinttxt = view.findViewById(R.id.chathint_txt);
            Image = view.findViewById(R.id.Owner_img);

            //set data after searching
            if (searched) {
                Image.setImageBitmap(modelList.get(position).getImg_bitmap());
                recomendtxt.setText("Recommended Owner");
                chathinttxt.setText("Chat To Contact with him");
                similaritytxt.setText("Similarity:");
                similarity_res.setText(modelList.get(position).getSimilarity()+"% ");
            }
            fuser = FirebaseAuth.getInstance().getCurrentUser();
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String USER_ID= modelList.get(position).getUserId();
                    sendMessage(fuser.getUid(), USER_ID, " ");
                }
            });

            return view;
        }
    }

    private void GetImage_Cropped() {


        if (checkIntent.equals("crop")) {
            // get image from crop activity
            ImageURi = getIntent().getParcelableExtra(KEY_OF_SEND_TO_FIND_OWNER);
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), ImageURi);
                selecteditem_img.setImageBitmap(bitmap);
            } catch (Exception e) {
            }
        } else {
            selectedimg = getIntent().getParcelableExtra("SelectedImage");
            selecteditem_img.setImageBitmap(selectedimg);
            bitmap = selectedimg;
        }

    }

    private void INIT() {
        SuggestUser =new HashMap<>();
        lostImageModels =new ArrayList<>();

        checkIntent = getIntent().getStringExtra("r");
        Cat_name = getIntent().getStringExtra(Key_CATEGORY);

        ownersimg.add(R.drawable.user);
        search_btn = findViewById(R.id.findowner_search_btn);
        selecteditem_img = findViewById(R.id.selecteditem_img);

        mReference = FirebaseDatabase.getInstance().getReference().child("LostItems").child(Cat_name);


    }


    public String getImageString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        //store in byte array
        byte[] imagebytes = baos.toByteArray();
        //encode to string
        String encodedimage = android.util.Base64.encodeToString(imagebytes, Base64.DEFAULT);
        return encodedimage;
    }


    private Bitmap decodeBase64(String input) {
        byte[] decodedString = Base64.decode(input, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }

    private void ConnectPython() {

        if (!Python.isStarted()) {
            Python.start(new AndroidPlatform(FindOwner.this));
        }

        final Python py = Python.getInstance();
        String imageString = getImageString(bitmap);
        //pass imageString to python script
        final PyObject pyobj = py.getModule("scriptSift");
        final PyObject obj = pyobj.callAttr("main", imageString, Cat_name);
        final List<PyObject> rows = obj.asList();

        for (int i = 0; i < rows.size(); i++) {
            String o = rows.get(i).toString();
            String attr[] = o.split("with");
            mReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String id = snapshot.child("item_ID").getValue().toString();
                        if (id.equals(attr[2])) {
                            String img = snapshot.child("imageLosted").getValue().toString();
                            System.out.println("doneeeeeeeeeeeeeeeeeeeee+" +attr[1]);
                            Bitmap bit = decodeBase64(img);
                            LostImageModel model = new LostImageModel();
                            model.setUserId(attr[0]);
                            model.setSimilarity(Integer.parseInt(attr[1]));
                            model.setImg_bitmap(bit);

                            lostImageModels.add(model);
                            Collections.sort(lostImageModels);
                            customadapter.notifyDataSetChanged();
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

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