package com.example.qr_scanner_and_image_similarity_detection;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    private final int MiN_TIME = 10; //? sec
    private final int MIN_DISTANCE = 0;
    private LocationManager manager;//? meter
    private DatabaseReference reference;
    private Marker my_marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        manager = (LocationManager) getSystemService(LOCATION_SERVICE);

        reference = FirebaseDatabase.getInstance().getReference().child("location");
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        getLocationUpdates();
        readChanges();
    }

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
                    Toast.makeText(MapsActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getLocationUpdates() {//to allow open GPS
        if (manager != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                            PackageManager.PERMISSION_GRANTED) {
                if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MiN_TIME, MIN_DISTANCE, this);
                } else if (manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MiN_TIME, MIN_DISTANCE, this);
                } else
                    Toast.makeText(this, "no provider enabled", Toast.LENGTH_SHORT).show();
            } else {
                //when permission denied
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                getLocationUpdates();
            else
                Toast.makeText(this, "permission required", Toast.LENGTH_SHORT).show();
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
        }catch (Exception exception){
            Toast.makeText(MapsActivity.this,"you aren't allowed to access this current location",Toast.LENGTH_LONG).show();

        }
        if(loc!=null)
        {
            //to get my position, i will use latitude &longitude then add marker to my position and zoom 17
            LatLng my_position= new LatLng(loc.getLatitude(),loc.getLongitude());
            my_marker=mMap.addMarker(new MarkerOptions().position(my_position).title("User Current location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(my_position,17));
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        if(location!=null)
            saveLocation(location);
        else
            Toast.makeText(this,"no location",Toast.LENGTH_SHORT).show();
    }

    @Override//tl3 exception w la2to gayb sertha f 7tetha w mdrbsh
    public void onStatusChanged(String provider, int status, Bundle extras) {
        //when GPS status alters
    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    //save location(lng/lat) & CompleteAddressString in firebase
    private void saveLocation(Location location) {
        reference.push().setValue(location);
        reference.push().setValue(getCompleteAddressString(location.getLatitude(),location.getLongitude()));
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


}