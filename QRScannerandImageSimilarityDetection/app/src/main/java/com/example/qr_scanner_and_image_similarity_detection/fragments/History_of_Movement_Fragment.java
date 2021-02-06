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
import com.example.qr_scanner_and_image_similarity_detection.adapters.RV_location_Adapter;
import com.example.qr_scanner_and_image_similarity_detection.models.LocationModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class History_of_Movement_Fragment extends Fragment {

    private RecyclerView recyclerView;
    private RV_location_Adapter loc_adapter;
    private ArrayList<LocationModel>list;

    private FirebaseUser current_user;
    private String user_id;

    private FirebaseDatabase db =FirebaseDatabase.getInstance();
    private DatabaseReference reference = db.getReference().child("location");
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup root=(ViewGroup) inflater.inflate(R.layout.fragment_histoty_of__movement_, container, false);
        recyclerView=root.findViewById(R.id.frag_history_rv);

        list = new ArrayList<>();
        loc_adapter = new RV_location_Adapter(list,getContext());

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(loc_adapter);

        //retrieve specific data from firbase to the recyclerview
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    LocationModel locationModel = dataSnapshot.getValue(LocationModel.class);
                    current_user = FirebaseAuth.getInstance().getCurrentUser();
                    user_id = current_user.getUid();
                    if(locationModel.getUserId().equals(user_id))
                        list.add(locationModel);
                    else
                        continue;
                }
                loc_adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return root;
    }
}