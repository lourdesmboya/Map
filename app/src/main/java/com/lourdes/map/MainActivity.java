package com.lourdes.map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap gMap;
    public Switch switch_location;
    private FusedLocationProviderClient mFusedLocationClient;
    private Location myLoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.m_f);
        mapFragment.getMapAsync(this);
        switch_location = findViewById(R.id.switch1);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        switch_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Requesting Permission to access location
                requestPermission();
            }
        });
    } //End of Oncreate method

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        gMap = googleMap;
        gMap.getUiSettings().setMyLocationButtonEnabled(true);
        gMap.setMyLocationEnabled(true);
    }

    //For getting user permission
    private void requestPermission() {
        if(switch_location.isChecked()) {
            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                //Show Location
                
                showLocation();

            }
        }
        else {
            ActivityCompat.requestPermissions(MainActivity.this, new String [] { Manifest.permission.ACCESS_FINE_LOCATION}, 2);
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 2)
        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                showLocation();
            }
        };

    }


    @SuppressLint("MissingPermission")
    private void showLocation()
    {
        mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>()
        {
            @Override
            public void onComplete(@NonNull Task<Location> task)
            {

                if (task.isSuccessful())
                {
                    myLoc = task.getResult();
                    if (myLoc!= null) {
                        LatLng latlng = new LatLng(myLoc.getLongitude(), myLoc.getLatitude());
                        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 30));
                    };
                };

            }
        });
    }

    //Use LocationReqs, setReq interval, Use Minimum Power, Add a marker to your location, when you tap on the marker say I am here,
    //Find geocordinates at your home and show your marker at your home
    //Request user to turn on the location




}

