package com.example.qr_scanner_and_image_similarity_detection.fragments;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.qr_scanner_and_image_similarity_detection.R;
import com.example.qr_scanner_and_image_similarity_detection.activities.AlarmReceiver;
import com.example.qr_scanner_and_image_similarity_detection.models.Reminder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.content.Context.ALARM_SERVICE;


public class Reminder_Item_Fragment extends Fragment {
    int hours,minuits;
    DatabaseReference databaseReminder;
     List<String> itemlist=new ArrayList<>();
    private int notificationId = 1;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_reminder__item_, container, false);
       //******************************************************************************************
        databaseReminder= FirebaseDatabase.getInstance().getReference("Reminder");
        //************************settime********************************************
        final TextView settime=(TextView)root.findViewById(R.id.settime);
        final TextView settimetext=(TextView)root.findViewById(R.id.settimeText);
        settime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent
                Intent intent = new Intent(getContext(), AlarmReceiver.class);
                intent.putExtra("notificationId", notificationId);

                // PendingIntent
                PendingIntent pendingIntent = PendingIntent.getBroadcast(
                        getContext(), 0, intent, PendingIntent.FLAG_CANCEL_CURRENT
                );

                // AlarmManager
                AlarmManager alarmManager = (AlarmManager)getActivity().getSystemService(ALARM_SERVICE);
                TimePickerDialog set_timePicker=new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Calendar calendar= Calendar.getInstance();
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        calendar.set(Calendar.SECOND, 0);
                        long alarmStartTime = calendar.getTimeInMillis();
                        settimetext.setText(DateFormat.format("hh:mm aa",calendar));
                        alarmManager.set(AlarmManager.RTC_WAKEUP, alarmStartTime, pendingIntent);

                        // Intent
                        //Intent intent = new Intent(getContext(), AlarmReceiver.class);
                       // intent.putExtra("notificationId", notificationId);

                        // PendingIntent
                       // PendingIntent pendingIntent = PendingIntent.getBroadcast(
                       //         getContext(), 0, intent, PendingIntent.FLAG_CANCEL_CURRENT
                      //  );

                        // AlarmManager
                       // AlarmManager alarmManager = (AlarmManager)getActivity().getSystemService(ALARM_SERVICE);

                        // Set Alarm
                      //  alarmManager.set(AlarmManager.RTC_WAKEUP,alarmStartTime , pendingIntent);

                        //
                        Toast.makeText(getContext(), "Alarm done!", Toast.LENGTH_SHORT).show();


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
                if(edit_addTask.getText().toString().equals(""))
                {Toast.makeText(getActivity(), "you cant add empty field", Toast.LENGTH_LONG).show();}
                else {
                    String value = edit_addTask.getText().toString();
                    myadapter.add(value);
                    itemlist.add(value);
                    edit_addTask.getText().clear();
                }
            }
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

       Button save=(Button)root.findViewById(R.id.save);
       save.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               //********************///
               Calendar calendar=Calendar.getInstance();
               SimpleDateFormat simpleDateFormat=new SimpleDateFormat("HH:mm:ss");
               String currentTime=simpleDateFormat.format(calendar.getTime());
              //*****************///
               String endTime=settimetext.getText().toString();

               String remId=databaseReminder.push().getKey();
               final TextView settimetext=(TextView)root.findViewById(R.id.settimeText);
               final FirebaseUser fuser= FirebaseAuth.getInstance().getCurrentUser();

                if(settimetext.getText().toString().equals("Set your return time.")||myadapter.isEmpty()){
                    Toast.makeText(getActivity(), "please set your return time or add your items", Toast.LENGTH_LONG).show();
                }
                else{

                    Reminder reminder=new Reminder(remId,currentTime,endTime,itemlist,"0",fuser.getUid());
                    databaseReminder.child(remId).setValue(reminder);
                    myadapter.clear();
                    itemlist.clear();
                    settimetext.setText("Set your return time.");
                    Toast.makeText(getContext(), "save!", Toast.LENGTH_SHORT).show();
                }

           }
       });

        return root;

    }
}