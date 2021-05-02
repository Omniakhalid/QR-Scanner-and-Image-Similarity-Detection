package com.example.qr_scanner_and_image_similarity_detection.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.qr_scanner_and_image_similarity_detection.ItemCardClass;
import com.example.qr_scanner_and_image_similarity_detection.R;
import com.example.qr_scanner_and_image_similarity_detection.models.LostPosts;
import com.example.qr_scanner_and_image_similarity_detection.models.Lost_ItemClass;

import java.util.ArrayList;
import java.util.List;


public class RV_itemLost_Adapter extends RecyclerView.Adapter<RV_itemLost_Adapter.MyViewHolder> {

    private ArrayList<LostPosts> mList;
    private Context mContext;

    private OnItemClickListener mlistener;

    public interface OnItemClickListener {
        void onClickSendMes(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        mlistener = listener;
    }

    public RV_itemLost_Adapter(ArrayList<LostPosts>mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.item_view, parent, false);
        return new MyViewHolder(view,mlistener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        LostPosts currentitem=mList.get(position);
        holder.home_frag_lostItem_img.setImageBitmap(decodeBase64(currentitem.getImage_Lostim()));
        holder.home_frag_lostItem_dec.setText(currentitem.getDiscreaption_LostItm());
        holder.imageButton.setImageResource(currentitem.getChatIcon());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

         TextView home_frag_lostItem_dec;
         ImageView home_frag_lostItem_img,imageButton;
        public MyViewHolder(@NonNull View itemView , final OnItemClickListener listener) {
            super(itemView);
            home_frag_lostItem_dec = itemView.findViewById(R.id.home_frag_item_dec_text_view);
            home_frag_lostItem_img = itemView.findViewById(R.id.home_frag_item_img);
            imageButton = itemView.findViewById(R.id.home_frag_item_icon_chat);

            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener !=null){
                        int postion=getAdapterPosition();
                        if(postion !=RecyclerView.NO_POSITION){
                            listener.onClickSendMes(postion);
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
