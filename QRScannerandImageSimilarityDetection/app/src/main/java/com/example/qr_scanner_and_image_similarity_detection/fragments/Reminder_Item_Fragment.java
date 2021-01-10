package com.example.qr_scanner_and_image_similarity_detection.fragments;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.qr_scanner_and_image_similarity_detection.R;

import java.util.Calendar;


public class Reminder_Item_Fragment extends Fragment {
    int hours,minuits;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_reminder__item_, container, false);
        //************************settime********************************************
        final TextView settime=(TextView)root.findViewById(R.id.settime);
        final TextView settimetext=(TextView)root.findViewById(R.id.settimeText);
        settime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog set_timePicker=new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        hours=hourOfDay;
                        minuits=minute;
                        Calendar calendar= Calendar.getInstance();
                        calendar.set(0,0,0,hours,minuits);
                        settimetext.setText(DateFormat.format("hh:mm aa",calendar));

                    }
                },12,0,false
                );
                ///
                set_timePicker.updateTime(hours,minuits);
                set_timePicker.show();
            }
        });


        //************************addlist******************************************************
        ListView addtextlist=(ListView)root.findViewById(R.id.listview);
        final ArrayAdapter<String> myadapter=new ArrayAdapter<String>(getActivity(),R.layout.custom_text
        );
        addtextlist.setAdapter(myadapter);
        //add object button to list
        ImageView add_object= (ImageView)root.findViewById(R.id.addObject);
        add_object.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText edit_addTask= (EditText)root.findViewById(R.id.addObject_field);
                String value=edit_addTask.getText().toString();
                myadapter.add(value);
                edit_addTask.getText().clear(); }
        });

        //delete item fron listview in longpress in it and show toast confirm deletion
        addtextlist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                myadapter.remove(((TextView)view).getText().toString());
                Toast.makeText(getActivity(), "Item Deleted", Toast.LENGTH_LONG).show();
                return true;
            }
        });
        //*****************************************************************************************************

        return root;

    }
}