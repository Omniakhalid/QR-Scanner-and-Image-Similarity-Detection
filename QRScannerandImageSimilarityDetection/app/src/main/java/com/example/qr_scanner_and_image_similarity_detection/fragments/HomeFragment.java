package com.example.qr_scanner_and_image_similarity_detection.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.qr_scanner_and_image_similarity_detection.R;
import com.example.qr_scanner_and_image_similarity_detection.adapters.RV_itemLost_Adapter;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    private RecyclerView frag_home_RV;
    private RV_itemLost_Adapter adapter;
    @NonNull
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup root =(ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);

        List<String> list = new ArrayList();
        for (int i=1 ;i<= 10;i++){
            list.add("Item "+i);
        }

        frag_home_RV = root.findViewById(R.id.frag_home_rv);
        adapter = new RV_itemLost_Adapter(list, getContext());
        frag_home_RV.setLayoutManager(new LinearLayoutManager(getContext()));
        frag_home_RV.setAdapter(adapter);


        return root;
    }
}