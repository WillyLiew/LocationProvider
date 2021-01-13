package com.example.locationprovider;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    //Need to add permission for ACCESS_COARSE_LOCATION, FINE and INTERNET
    //Add in dependency for gms:play-services-location
    TextView tv;
    Button btn;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;
    LocationCallback locationCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = findViewById(R.id.textView);
        btn=findViewById(R.id.button);
        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this);
        locationRequest=new LocationRequest()
                .setInterval(1000*5)
                .setFastestInterval(1000*2)
                .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        locationCallback=new LocationCallback(){
            // CTRL + O to implement method:
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if(locationResult==null){
                    tv.setText("No location returned");
                }else{
                    for(Location location:locationResult.getLocations()){
                        tv.setText("Latitude: "+String.valueOf(location.getLatitude())+"\n"+
                                "Longitude: "+String.valueOf(location.getLongitude())+"\n");
                    }
                }
            }
        };
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{ Manifest.permission.ACCESS_FINE_LOCATION},100);
            //ActivityCompat.requestPermissions(this,new String[]{ Manifest.permission.ACCESS_COARSE_LOCATION},100)
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback,null);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation();
            }
        });
    }

    private void getLocation() {
        Toast.makeText(this,"Retrieving location ...",Toast.LENGTH_SHORT).show();
        if(fusedLocationProviderClient!=null){
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,new String[]{ Manifest.permission.ACCESS_FINE_LOCATION},100);
                //ActivityCompat.requestPermissions(this,new String[]{ Manifest.permission.ACCESS_COARSE_LOCATION},100)
            }
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if(location!=null){
                        /*tv.setText("Latitude: "+String.valueOf(location.getLatitude())+"\n"+
                                "Longitude: "+String.valueOf(location.getLongitude())+"\n");*/
                        tv.setText(location.toString()+"\n");
                        Toast.makeText(MainActivity.this,"Location updated",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(MainActivity.this,"No result",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

}
