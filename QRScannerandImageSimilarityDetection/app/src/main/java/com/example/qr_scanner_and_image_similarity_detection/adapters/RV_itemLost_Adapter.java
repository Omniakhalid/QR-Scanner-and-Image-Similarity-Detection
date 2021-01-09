package com.example.qr_scanner_and_image_similarity_detection.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.qr_scanner_and_image_similarity_detection.R;

import java.util.List;


public class RV_itemLost_Adapter extends RecyclerView.Adapter<RV_itemLost_Adapter.MyViewHolder> {

    private List<String> mList;
    private Context mContext;

    public RV_itemLost_Adapter(List<String> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.item_view, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.home_frag_lostItem_dec.setText(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
         TextView home_frag_lostItem_dec;
         ImageView home_frag_lostItem_img,imageButton;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            home_frag_lostItem_dec = itemView.findViewById(R.id.home_frag_item_dec_text_view);
            home_frag_lostItem_img = itemView.findViewById(R.id.home_frag_item_img);
            imageButton = itemView.findViewById(R.id.home_frag_item_icon_chat);
        }
    }
}
