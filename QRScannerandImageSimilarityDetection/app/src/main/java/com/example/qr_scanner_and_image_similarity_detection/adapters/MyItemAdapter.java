package com.example.qr_scanner_and_image_similarity_detection.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qr_scanner_and_image_similarity_detection.ItemCardClass;
import com.example.qr_scanner_and_image_similarity_detection.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MyItemAdapter extends RecyclerView.Adapter<MyItemAdapter.ExampleViewHolder> {
    private ArrayList<ItemCardClass> myExampleList;
    private OnItemClickListener mlist;

    public interface OnItemClickListener {
        void onMarkedClick(int position);
        void onMarkedDeleted(int position);
        void onItemDeleted(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mlist = listener;
    }

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart,parent,false);
        ExampleViewHolder evh=new ExampleViewHolder(v,mlist);
        return evh;
    }
    public MyItemAdapter(ArrayList<ItemCardClass>exampleList){
        myExampleList=exampleList;
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {
        ItemCardClass currenProd =myExampleList.get(position);
        holder.myTextDisceaption.setText(currenProd.getDiscreaption());
        holder.myTextCategory.setText("Category is : "+ currenProd.getCat_ID());
        holder.Savebtn.setImageBitmap(decodeBase64(currenProd.getImageSource()));
        holder.ItemIsLost.setChecked(currenProd.isItemLostchecked());
        holder.DeleteItem.setImageResource(currenProd.getDeleteItem());

    }

    @Override
    public int getItemCount() {
        return myExampleList.size();
    }

    public class ExampleViewHolder extends RecyclerView.ViewHolder {
        public TextView myTextDisceaption;
        public TextView myTextCategory;
        public ImageView Savebtn;
        public CheckBox ItemIsLost;
        public ImageView DeleteItem;

        int position;

        public ExampleViewHolder(View itemView , final OnItemClickListener listener) {
            super(itemView);

            myTextDisceaption=itemView.findViewById(R.id.item_Descriptiontxt);
            myTextCategory=itemView.findViewById(R.id.EmailEditText);
            Savebtn=itemView.findViewById(R.id.item_itenPoto);
            ItemIsLost=itemView.findViewById(R.id.ItemLostChBox);
            DeleteItem=itemView.findViewById(R.id.delete_item);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            DeleteItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    position = getAdapterPosition();
                    myExampleList.remove(position);
                    notifyDataSetChanged();
                    listener.onItemDeleted(position);
                }
            });

            ItemIsLost.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        if(listener !=null){
                            int postion=getAdapterPosition();
                            if(postion !=RecyclerView.NO_POSITION){
                                listener.onMarkedClick(postion);
                            }
                        }
                    }
                    else {
                        if(listener !=null){
                            int postion=getAdapterPosition();
                            if(postion !=RecyclerView.NO_POSITION){
                                listener.onMarkedDeleted(postion);
                            }
                        }
                    }
                }
            });

        }
    }
    private Bitmap decodeBase64(String input){
        byte[] decodedString = Base64.decode(input, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }
}
