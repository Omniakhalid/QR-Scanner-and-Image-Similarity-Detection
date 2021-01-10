package com.example.qr_scanner_and_image_similarity_detection;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ExampleViewHolder> {
    private ArrayList<CardExample> myExampleList;

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart,parent,false);
        ExampleViewHolder evh=new ExampleViewHolder(v);
        return evh;
    }
    public RecycleAdapter(ArrayList<CardExample>exampleList){
        myExampleList=exampleList;
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {
        CardExample currenProd =myExampleList.get(position);
        holder.myTextDisceaption.setText(currenProd.getMyText1());
        holder.myTextCategory.setText(currenProd.getMyText2());
        holder.Savebtn.setImageBitmap(currenProd.getmImageResource());

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
