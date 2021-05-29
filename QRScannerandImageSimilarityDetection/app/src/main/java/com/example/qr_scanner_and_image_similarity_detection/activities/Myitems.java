package com.example.qr_scanner_and_image_similarity_detection.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.qr_scanner_and_image_similarity_detection.ItemCardClass;
import com.example.qr_scanner_and_image_similarity_detection.adapters.MyItemAdapter;
import com.example.qr_scanner_and_image_similarity_detection.R;
import com.example.qr_scanner_and_image_similarity_detection.models.LostPosts;
import com.example.qr_scanner_and_image_similarity_detection.models.Lost_ItemClass;
import com.example.qr_scanner_and_image_similarity_detection.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Myitems extends AppCompatActivity {

    public RecyclerView MyRecyclerView;
    public MyItemAdapter myResAdapter;
    public RecyclerView.LayoutManager myResLaoutMan;


    ArrayList<ItemCardClass> ItemList =new ArrayList<>();

    EditText decre;
    Spinner cateSpin;
    ImageView itmImag ;
    Button addBtn;
    final static int GALLERY_REQUEST_CODE = 101;
    Bitmap bitmap;

    int item_id=1;
    private FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();

    DatabaseReference reff;
    DatabaseReference reffLOstitem;
    DatabaseReference reffPosts;

    private  DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("Items").child(current_user.getUid());
    private StorageReference mStorage;
    Uri uri;

    private ProgressDialog Loadingbar;

    List<String> LostitemImage = new ArrayList<>();
    List<String> LostitemDedcript = new ArrayList<>();
    List<String> LostitemPhoto = new ArrayList<>();
    List<String> LostitemCatId = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myitems);

        Loadingbar=new ProgressDialog(this);
        Loadingbar.setTitle("Welcome");
        Loadingbar.setMessage("Please Wait, we are Loading your items");
        Loadingbar.setCanceledOnTouchOutside(true);
        Loadingbar.show();
        reff= FirebaseDatabase.getInstance().getReference().child("Items");
        mStorage=FirebaseStorage.getInstance().getReference().child("Images");
        init();
        builtRecycler();

    }

    private String getExention(Uri uri){
        ContentResolver cr=getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }

    private void imageUpload(){
        StorageReference Ref=mStorage.child(System.currentTimeMillis()+"."+getExention(uri));

        Ref.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        // Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        Toast.makeText(Myitems.this,"Upload Done",Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                        Toast.makeText(Myitems.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }
    private void init(){

        decre=findViewById(R.id.itm_Add_descrep);
        cateSpin=findViewById(R.id.Item_Car_spinner);
        itmImag =findViewById(R.id.Itm_itemimg);
        itmImag.setTag("0");

        addBtn =findViewById(R.id.item_AddBtn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if( cateSpin.getSelectedItem().toString().equals("Select Category") )
                    Toast.makeText(Myitems.this,"Please select your Category",Toast.LENGTH_LONG).show();
                if(decre.getText().toString().equals(""))
                    Toast.makeText(Myitems.this,"Please Enter description for your Item",Toast.LENGTH_LONG).show();
                if(itmImag.getTag().toString().equalsIgnoreCase("0"))
                    Toast.makeText(Myitems.this,"Please Upload your photo",Toast.LENGTH_LONG).show();
               // if(LostitemPhoto.contains(encodeTobase64(bitmap)))
                 //   Toast.makeText(Myitems.this,"you Entered this Item before",Toast.LENGTH_LONG).show();
                else {
                    item_id=ItemList.size();
                    ItemCardClass NewItem=new ItemCardClass();
                    NewItem.setDiscreaption(decre.getText().toString());
                    NewItem.setImageSource(encodeTobase64(bitmap));
                    //NewItem.setCat_ID(String.valueOf(cateSpin.getSelectedItemPosition()));
                    NewItem.setCat_ID(cateSpin.getSelectedItem().toString());
                    NewItem.setItemLostchecked(false);
                    NewItem.setDeleteItem(R.drawable.ic_delete_icon);
                    reff.child(current_user.getUid()).child(String.valueOf(item_id)).setValue(NewItem);
                    item_id++;

                    ItemList.add(NewItem);
                    myResAdapter.notifyDataSetChanged();
                    Toast.makeText(Myitems.this,"One Item Added",Toast.LENGTH_LONG).show();
                }

            }
        });

        itmImag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

    }
    private void builtRecycler(){
        MyRecyclerView =findViewById(R.id.Recclarvie);
        MyRecyclerView.setHasFixedSize(true);
        myResLaoutMan=new LinearLayoutManager(this);
        myResAdapter=new MyItemAdapter(ItemList);
        MyRecyclerView.setLayoutManager(myResLaoutMan);
        MyRecyclerView.setAdapter(myResAdapter);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren())
                {
                    ItemCardClass data=ds.getValue(ItemCardClass.class);
                    LostitemImage.add(data.getImageSource());
                    LostitemDedcript.add(data.getDiscreaption());
                    LostitemPhoto.add(data.getImageSource());
                    LostitemCatId.add(data.getCat_ID());
                    ItemList.add(data);
                }

                myResAdapter.notifyDataSetChanged();
                Loadingbar.hide();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        myResAdapter.setOnItemClickListener(new MyItemAdapter.OnItemClickListener() {
            @Override
            public void onMarkedClick(int position) {
                AddLostItem(position);
            }

            @Override
            public void onMarkedDeleted(int position) {
                reff.child(current_user.getUid()).child(String.valueOf(position+1)).child("itemLostchecked").setValue(false);
               // reffLOstitem.child(current_user.getUid()).child(LostitemImage.get(position)).setValue(null);
                //reffPosts.child(current_user.getUid()).child(LostitemImage.get(position)).setValue(null);
            }

            @Override
            public void onItemDeleted(int position) {
                DatabaseReference reff;
                FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                reff= FirebaseDatabase.getInstance().getReference().child("Items").child(current_user.getUid());
                reff.child(String.valueOf(position+1)).setValue(null);
            }

        });
    }

    private void AddLostItem(int position) {

        String id=current_user.getUid();
        Lost_ItemClass myLostitem;
        myLostitem=new Lost_ItemClass();
        myLostitem.setItem_ID(position+1);
        myLostitem.setUser_ID(id);
        myLostitem.setImageLosted(LostitemImage.get(position));
        reffLOstitem= FirebaseDatabase.getInstance().getReference().child("LostItems");
        reffLOstitem.child(LostitemCatId.get(position)).push().setValue(myLostitem);

        reff.child(current_user.getUid()).child(String.valueOf(position+1)).child("itemLostchecked").setValue(true);

        LostPosts LostImage;
        LostImage=new LostPosts();
        LostImage.setImage_Lostim(LostitemImage.get(position));
        LostImage.setDiscreaption_LostItm(LostitemDedcript.get(position));
        LostImage.setChatIcon(R.drawable.ic_chat);
        LostImage.setUser_ID(id);
        reffPosts=FirebaseDatabase.getInstance().getReference().child("Posts");
        reffPosts.push().setValue(LostImage);

        Toast.makeText(Myitems.this,"you Mark this item as A lost item",Toast.LENGTH_LONG).show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {

            uri = data.getData();
            itmImag.setImageURI(uri);

            try {

              /*  StorageReference itemphoto=mStorage.child("itemPhoto").child(uri.getLastPathSegment());
                itemphoto.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(Myitems.this,"Upload Done",Toast.LENGTH_LONG).show();
                    }
                });*/
                imageUpload();
                InputStream inputStream = getContentResolver().openInputStream(uri);
                bitmap = BitmapFactory.decodeStream(inputStream);
                itmImag.setImageBitmap(bitmap);
                itmImag.setTag("UpdatedTag");

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

    private String encodeTobase64( Bitmap image) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return encoded;
    }


}