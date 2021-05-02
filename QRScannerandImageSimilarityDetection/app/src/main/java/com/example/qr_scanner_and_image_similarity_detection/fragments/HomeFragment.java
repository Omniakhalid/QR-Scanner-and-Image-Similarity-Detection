package com.example.qr_scanner_and_image_similarity_detection.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.qr_scanner_and_image_similarity_detection.R;
import com.example.qr_scanner_and_image_similarity_detection.adapters.RV_itemLost_Adapter;
import com.example.qr_scanner_and_image_similarity_detection.models.LostPosts;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    private RecyclerView frag_home_RV;
    private RV_itemLost_Adapter adapter;

    private ArrayList<LostPosts>list;
    //private List<String> usersID;

    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Posts");
    @NonNull
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup root =(ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);
        frag_home_RV = root.findViewById(R.id.frag_home_rv);

        list = new ArrayList<>();

        adapter = new RV_itemLost_Adapter(list, getContext());
        frag_home_RV.setLayoutManager(new LinearLayoutManager(getContext()));
        frag_home_RV.setAdapter(adapter);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    LostPosts lostsItem = dataSnapshot.getValue(LostPosts.class);
                        list.add(lostsItem);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        adapter.setOnItemClickListener(new RV_itemLost_Adapter.OnItemClickListener() {
            @Override
            public void onClickSendMes(int position) {
                OpenChatWithThisUser(position);
            }
        });

        return root;
    }

    private void OpenChatWithThisUser(int position) {
        String reciveUserId= list.get(position).getUser_ID();
    }
}