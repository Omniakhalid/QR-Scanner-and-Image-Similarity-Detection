package com.example.qr_scanner_and_image_similarity_detection.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.qr_scanner_and_image_similarity_detection.Capture;
import com.example.qr_scanner_and_image_similarity_detection.MychatsActivity;
import com.example.qr_scanner_and_image_similarity_detection.activities.Myitems;
import com.example.qr_scanner_and_image_similarity_detection.R;
import com.example.qr_scanner_and_image_similarity_detection.activities.sign_in_up_activities.SigninActivity;
import com.example.qr_scanner_and_image_similarity_detection.fragments.History_of_Movement_Fragment;
import com.example.qr_scanner_and_image_similarity_detection.fragments.HomeFragment;
import com.example.qr_scanner_and_image_similarity_detection.fragments.Reminder_Item_Fragment;
import com.example.qr_scanner_and_image_similarity_detection.fragments.Upload_photo_Fragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final String QR_SCANNING_KEY = "QR_SCANNING_KEY";
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private ChipNavigationBar bottom_navigationBar;
    private FragmentTransaction transaction;
    private NavigationView nav_view;
    private Switch switch_id;
    private FirebaseAuth auth;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Init();
        OpenNavDrawer();


        bottom_navigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                openNav_Bottom(i);
            }
        });

        HomeFragment homeFragment = new HomeFragment();
        openFragment(homeFragment);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_chat, menu);
        return true;
    }

    private void openNav_Bottom(int i) {

        switch (i) {
            case R.id.nav_home:
                HomeFragment homeFragment = new HomeFragment();
                openFragment(homeFragment);
                break;
            case R.id.nav_history:
                History_of_Movement_Fragment history = new History_of_Movement_Fragment();
                openFragment(history);
                break;
            case R.id.nav_reminder:
                Reminder_Item_Fragment reminder_item_fragment = new Reminder_Item_Fragment();
                openFragment(reminder_item_fragment);
                break;
            case R.id.nav_scan_QR:
                Scan();
                //Scan_Qr_Fragment scan_qr_fragment = new Scan_Qr_Fragment();
                //openFragment(scan_qr_fragment);
                break;
            case R.id.nav_upload:
                Upload_photo_Fragment upload_photo_fragment = new Upload_photo_Fragment();
                openFragment(upload_photo_fragment);
                break;
        }
    }

    private void openActivity(Class c) {
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }

    private void Scan() {
        IntentIntegrator integrator = new IntentIntegrator(HomeActivity.this);
        integrator.setOrientationLocked(true);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scanning Code...");
        integrator.setCameraId(0);
        integrator.setCaptureActivity(Capture.class);
        integrator.initiateScan();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                //scan have an error
                Toast.makeText(HomeActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
            } else {
                //scan is successful
                Toast.makeText(HomeActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                //send QR data to emergency call activity..
                String res = result.getContents();
                openActivityWithData(EmergencyActivity.class, QR_SCANNING_KEY, res);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void OpenNavDrawer() {
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void Init() {
        auth = FirebaseAuth.getInstance();

        drawerLayout = findViewById(R.id.nav_drawer);
        nav_view = findViewById(R.id.navigation);
        bottom_navigationBar = findViewById(R.id.chipNavigation);
        transaction = getSupportFragmentManager().beginTransaction();
        bottom_navigationBar.setItemSelected(R.id.nav_home, true);
        nav_view.setNavigationItemSelectedListener(this);
    }

    private void openFragment(final Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commit();

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        if (id == R.id.menu_chat_icon) {
            showChat();
        }

        return super.onOptionsItemSelected(item);
    }

    void showChat() {

        Intent intent = new Intent(this, MychatsActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_drawer_profile:
                openActivity(ProfileActivity.class);
                break;
            case R.id.nav_drawer_logout:
                openActivity(SigninActivity.class);
                auth.signOut();
                finish();
                break;
            case R.id.nav_drawer_my_item:
                openActivity(Myitems.class);
                break;
            case R.id.nav_drawer_tracking:
                switch_id = findViewById(R.id.Switch_id);
                if (switch_id.isChecked()) {
                    switch_id.setChecked(false);
                    showMessage("Disable Tracking");
                } else {
                    switch_id.setChecked(true);
                    showMessage("Enable Tracking");
                }
                break;
        }
        //showMessage("Done");
        drawerLayout.closeDrawer(GravityCompat.START, true);
        return false;
    }

    private void showMessage(String S) {
        Toast.makeText(getApplicationContext(), S, Toast.LENGTH_SHORT).show();
    }


    private void openActivityWithData(Class c, String key, String value) {
        Intent intent = new Intent(this, c);
        intent.putExtra(key, value);
        startActivity(intent);
    }

    private void status(String Status){


    }







}