package com.example.qr_scanner_and_image_similarity_detection.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qr_scanner_and_image_similarity_detection.R;
import com.example.qr_scanner_and_image_similarity_detection.adapters.RV_itemLost_Adapter;
import com.example.qr_scanner_and_image_similarity_detection.adapters.RV_location_Adapter;

import java.util.ArrayList;
import java.util.List;


public class History_of_Movement_Fragment extends Fragment {

    private RecyclerView recyclerView;
    private RV_location_Adapter loc_adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup root=(ViewGroup) inflater.inflate(R.layout.fragment_histoty_of__movement_, container, false);
        List<String> list = new ArrayList();
        for (int i=1 ;i<= 6;i++){
            list.add("your history in "+i+ " /2/2020");
        }
        recyclerView=root.findViewById(R.id.frag_history_rv);
        loc_adapter = new RV_location_Adapter(list,getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(loc_adapter);
        return root;
    }
}