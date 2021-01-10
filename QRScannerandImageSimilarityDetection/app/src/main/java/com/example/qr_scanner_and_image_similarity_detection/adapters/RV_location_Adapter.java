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

public class RV_location_Adapter extends RecyclerView.Adapter<RV_location_Adapter.MyViewHolder> {

    private List<String> locList;
    Context context;

    public RV_location_Adapter(List<String> locList,Context context) {
        this.locList = locList;
        this.context=context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView loc_frag_user_desc;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            loc_frag_user_desc=itemView.findViewById(R.id.loc_desc_txtView);
        }
    }

    @NonNull
    @Override
    public RV_location_Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View loc_view = LayoutInflater.from(context).inflate(R.layout.user_history, parent, false);
        return new MyViewHolder(loc_view);
    }

    @Override
    public void onBindViewHolder(@NonNull RV_location_Adapter.MyViewHolder holder, int position) {
        holder.loc_frag_user_desc.setText(locList.get(position));

    }

    @Override
    public int getItemCount() {
        return locList.size();
    }
}
