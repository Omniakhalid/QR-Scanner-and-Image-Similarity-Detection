package com.example.qr_scanner_and_image_similarity_detection.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.qr_scanner_and_image_similarity_detection.Capture;
import com.example.qr_scanner_and_image_similarity_detection.MychatsActivity;
import com.example.qr_scanner_and_image_similarity_detection.activities.Location.MyLocation;
import com.example.qr_scanner_and_image_similarity_detection.R;
import com.example.qr_scanner_and_image_similarity_detection.activities.sign_in_up_activities.SigninActivity;
import com.example.qr_scanner_and_image_similarity_detection.fragments.History_of_Movement_Fragment;
import com.example.qr_scanner_and_image_similarity_detection.fragments.HomeFragment;
import com.example.qr_scanner_and_image_similarity_detection.fragments.Reminder_Item_Fragment;
import com.example.qr_scanner_and_image_similarity_detection.fragments.Upload_photo_Fragment;
import com.example.qr_scanner_and_image_similarity_detection.models.LocationModel;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, LocationListener, OnMapReadyCallback {

    public static final String QR_SCANNING_KEY = "QR_SCANNING_KEY";
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private ChipNavigationBar bottom_navigationBar;
    private FragmentTransaction transaction;
    private NavigationView nav_view;

    private Switch tracking_switch;

    private FirebaseAuth auth;

    private int perm=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        manager = (LocationManager) getSystemService(LOCATION_SERVICE);
        reference = FirebaseDatabase.getInstance().getReference().child("location");
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        onMapReady(mMap);
        getLocationUpdates();
        readChanges();
        //doWork();
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
            {
                History_of_Movement_Fragment history = new History_of_Movement_Fragment();
                openFragment(history);
                /*if (perm == 1)
                    Toast.makeText(this, "permission required", Toast.LENGTH_SHORT).show();*/
            }
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
        tracking_switch = findViewById(R.id.tracking_switch);
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
            {
                /*History_of_Movement_Fragment history = new History_of_Movement_Fragment();
                openFragment(history);*/
                if (perm == 1)
                {Toast.makeText(this, "permission required", Toast.LENGTH_SHORT).show();
                    stop_tracking();
                    perm=0;
                    break;
                }
                if(perm ==0){
                    getLocationUpdates();
                }
                /*if(locationModel.isStatus()==true)
                { stop_tracking();
                    stat=false;
                    //Map<String, Object>childUpdates=new HashMap<>();
                    //locationModel=new LocationModel(current_date,path_data,user_id,stat);
                    //childUpdates.put("status",stat);
                    //reference.updateChildren(childUpdates);
                    saveStatus();
                    break;}
                if(locationModel.isStatus()==false)
                    getLocationUpdates();*/

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

    /////////////////////////////////////////////////////////////////////////////////////////////////

    private GoogleMap mMap;
    private final int MiN_TIME = 10; //? sec
    private final int MIN_DISTANCE = 0;//0 meter
    private LocationManager manager;
    private DatabaseReference reference;
    private Marker my_marker;

    private LocationModel locationModel;
    private String current_date;
    private String path_data;
    private String user_id;
    //private boolean stat;

    private FirebaseUser current_user;

    private void readChanges() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            //put the marker on the updated location
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    if (snapshot.exists()) {
                        MyLocation location = snapshot.getValue(MyLocation.class);
                        if (location != null) {
                            //kont 3wza adef marker f el map kol ma l user yt7rk
                            /*LatLng updated_location=new LatLng(location.getLatitude(), location.getLongitude());
                            my_marker.setPosition(updated_location);
                            mMap.addMarker(new MarkerOptions().position(updated_location));*/
                            my_marker.setPosition(new LatLng(location.getLatitude(), location.getLongitude()));
                        }
                    }
                } catch (Exception ex) {
                    Toast.makeText(HomeActivity.this, "In Way to Update Your Location...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //android 10
    /*@Override
    public boolean shouldShowRequestPermissionRationale(@NonNull String permission) {
        return true;
    }*/

    private void getLocationUpdates() {//to allow open GPS
        if (manager != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                            PackageManager.PERMISSION_GRANTED) {
                if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MiN_TIME, MIN_DISTANCE, this);
                    Toast.makeText(this, "Tracking Enabled", Toast.LENGTH_SHORT).show();

                    perm=1;
                    //stat=true;


                } else if (manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MiN_TIME, MIN_DISTANCE, this);
                    Toast.makeText(this, "Tracking Enabled", Toast.LENGTH_SHORT).show();
                    perm=1;
                    //stat=true;

                } else
                {Toast.makeText(this, "In Way To Update Your Location...", Toast.LENGTH_SHORT).show();
                }
            } else {
                //when permission denied
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
                Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show();
                perm=0;
                //stat=false;

            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                getLocationUpdates();
            /*else {
                perm = 1;
                Toast.makeText(this, "permission required", Toast.LENGTH_SHORT).show();
            }*/
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Location loc = null;
        try {
            //get last known location crashed so we put it
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            //to get my current location through GPS
            loc = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        } catch (Exception exception) {
            Toast.makeText(HomeActivity.this, "you aren't allowed to access this current location", Toast.LENGTH_LONG).show();

        }
        if (loc != null) {
            //to get my position, i will use latitude &longitude then add marker to my position and zoom 17
            LatLng my_position = new LatLng(loc.getLatitude(), loc.getLongitude());
           /* my_marker = mMap.addMarker(new MarkerOptions().position(my_position).title("User Current location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(my_position, 17));*/
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        if (location != null)
            saveLocation(location);
        else
            Toast.makeText(this, "no location", Toast.LENGTH_SHORT).show();
    }

    @Override//tl3 exception w la2to gayb sertha f 7tetha w mdrbsh
    public void onStatusChanged(String provider, int status, Bundle extras) {
        //when GPS status alters
    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "GPS Enabled", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "GPS Disabled", Toast.LENGTH_SHORT).show();

    }

    //save location(lng/lat) & CompleteAddressString in firebase
    private void saveLocation(Location location) {
        Calendar calendar = Calendar.getInstance();
        current_date = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        current_user = FirebaseAuth.getInstance().getCurrentUser();
        user_id = current_user.getUid();
        locationModel = new LocationModel();
        //locationModel.setStatus(stat);
        locationModel.setDate(current_date);
        path_data = getCompleteAddressString(location.getLatitude(), location.getLongitude());
        locationModel.setPath(path_data);
        locationModel.setUserId(user_id);
        reference.push().setValue(locationModel);
    }

    //use geocoder to convert latitude &longitude to CompleteAddressString
    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.w("Current address", strReturnedAddress.toString());
            } else {
                Log.w("Current address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("Current address", "Cannot get Address!");
        }
        return strAdd;
    }

    void stop_tracking() {
        manager.removeUpdates(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(false);
        }
    }

    /*void saveStatus(){
        current_user = FirebaseAuth.getInstance().getCurrentUser();
        user_id = current_user.getUid();
        locationModel = new LocationModel();
        //locationModel.setStatus(stat);
        locationModel.setUserId(user_id);
        reference.push().setValue(locationModel);
    }*/

    /*public static Thread performOnBackgroundThread(final Runnable runnable) {
        final Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    runnable.run();
                } finally {

                }
            }
        };
        t.start();
        return t;
    }*/

    //to ope gps setting if gps is disabled hst5dmha b3den
    private void showSettingsAlert() {
        Toast.makeText(this, "GPS is disabled in your device. Please Enable it ?", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
    }

    /*public Thread doWork() {
        final Runnable runnable = new Runnable() {
            public void run() {
                System.out.println("Background Task here");
            }
        };

        // run on background thread.
        return performOnBackgroundThread(runnable);
    }*/

}