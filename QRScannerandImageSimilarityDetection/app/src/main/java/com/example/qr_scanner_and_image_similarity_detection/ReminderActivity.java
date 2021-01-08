package com.example.qr_scanner_and_image_similarity_detection;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class ReminderActivity  extends AppCompatActivity {
    int hours,minuits;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_reminder);
       //************************settime********************************************
        final TextView settime=(TextView)findViewById(R.id.settime);
        final TextView settimetext=(TextView)findViewById(R.id.settimeText);
        settime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog set_timePicker=new TimePickerDialog(ReminderActivity.this, new TimePickerDialog.OnTimeSetListener() {
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
        ListView addtextlist=(ListView)findViewById(R.id.listview);
        final ArrayAdapter<String> myadapter=new ArrayAdapter<String>(this,R.layout.custom_text
        );
        addtextlist.setAdapter(myadapter);
        //add object button to list
        ImageView add_object= (ImageView)findViewById(R.id.addObject);
        add_object.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText edit_addTask= (EditText)findViewById(R.id.addObject_field);
                String value=edit_addTask.getText().toString();
                myadapter.add(value);
                edit_addTask.getText().clear(); }
        });

        //delete item fron listview in longpress in it and show toast confirm deletion
        addtextlist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                myadapter.remove(((TextView)view).getText().toString());
                Toast.makeText(ReminderActivity.this, "Item Deleted", Toast.LENGTH_LONG).show();
                return true;
            }
        });
        //*****************************************************************************************************

    }

}
