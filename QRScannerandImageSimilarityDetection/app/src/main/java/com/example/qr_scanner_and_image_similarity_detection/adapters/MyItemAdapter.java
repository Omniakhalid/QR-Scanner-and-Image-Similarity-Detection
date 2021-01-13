package com.example.qr_scanner_and_image_similarity_detection.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qr_scanner_and_image_similarity_detection.ItemCardClass;
import com.example.qr_scanner_and_image_similarity_detection.R;

import java.util.ArrayList;

public class MyItemAdapter extends RecyclerView.Adapter<MyItemAdapter.ExampleViewHolder> {
    private ArrayList<ItemCardClass> myExampleList;

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart,parent,false);
        ExampleViewHolder evh=new ExampleViewHolder(v);
        return evh;
    }
    public MyItemAdapter(ArrayList<ItemCardClass>exampleList){
        myExampleList=exampleList;
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {
        ItemCardClass currenProd =myExampleList.get(position);
        holder.myTextDisceaption.setText(currenProd.getDiscreaption());
        holder.myTextCategory.setText(currenProd.getmCategory());
        holder.Savebtn.setImageBitmap(currenProd.getImageSource());

    }

    @Override
    public int getItemCount() {
        return myExampleList.size();
    }

    public class ExampleViewHolder extends RecyclerView.ViewHolder {
        public TextView myTextDisceaption;
        public TextView myTextCategory;
        public ImageView Savebtn;

        public ExampleViewHolder(View itemView) {
            super(itemView);
            myTextDisceaption=itemView.findViewById(R.id.item_Descriptiontxt);
            myTextCategory=itemView.findViewById(R.id.Item_CategoryTxt);
            Savebtn=itemView.findViewById(R.id.item_itenPoto);


        }
    }
}
