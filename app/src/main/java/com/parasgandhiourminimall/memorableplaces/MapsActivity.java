package com.parasgandhiourminimall.memorableplaces;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.gms.identity.intents.Address;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener,
        LocationListener {

    private GoogleMap mMap;
    LocationManager locationManager;
    String provider;
    Intent intent;
    Marker marker;
    String addsholder = "";//new Date().toString();
    LatLng markerLoc;
    String IntentType;
    int index;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent i = getIntent();
        IntentType = i.getStringExtra("Intent Type");
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        provider = locationManager.getBestProvider(new Criteria(), false);
        if (!IntentType.equals("add")){
            index = Integer.parseInt(IntentType);
            index--;
            Log.i("Index of Clicked", String.valueOf(index));
            Log.i("size of placeLoc array", MainActivity.placesLoc.size()+"");
            Log.i("LatLng from Array ", MainActivity.placesLoc.get(index)+"");
            markerLoc = MainActivity.placesLoc.get(index);
            Log.i("Latlng",markerLoc.toString());
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (IntentType.equals("add")){
                    MainActivity.places.add(addsholder);
                    MainActivity.arrayAdapter.notifyDataSetChanged();
                    MainActivity.placesLoc.add(markerLoc);
                }
                // app icon in action bar clicked; goto parent activity.
                this.finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (IntentType.equals("add")){
            Location location = locationManager.getLastKnownLocation(provider);
            LatLng current = new LatLng(location.getLatitude(),location.getLongitude());
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(current,15));
            onLocationChanged(location);
        }else {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(markerLoc,15));
            marker = mMap.addMarker(new MarkerOptions().position(markerLoc).title(MainActivity.places.get(index+1)));
        }
    }


    @Override
    public void onMapLongClick(LatLng latLng) {
        System.out.println("latLng: "+latLng);
        if (IntentType.equals("add")){
            markerLoc = latLng;
            if (marker!=null){
                marker.remove();
            }
            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            addsholder = "";
            try {
                List<android.location.Address> addressList = geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);
                if (addressList!=null&&addressList.size()>0){
                    for (int i = 0; i<addressList.get(0).getMaxAddressLineIndex(); i++){
                        addsholder += addressList.get(0).getAddressLine(i);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            marker = mMap.addMarker(new MarkerOptions().position(latLng).title(addsholder));
            System.out.println("Done With the Marker and Address");
            System.out.println("Adds: "+addsholder);
            System.out.println("Long Click Loc: "+latLng.toString());
//            Log.i("Long Click at :",);

        }
    }

    @Override
    public void onLocationChanged(Location location) {
        if (IntentType.equals("add")){
            LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,12));
            System.out.println("Lat: "+latLng.latitude+"\nLon:"+latLng.longitude);

            mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(LatLng latLng) {
                    if (marker!=null){
                        marker.remove();
                    }
                    Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                    try {
                        List<android.location.Address> addressList = geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);
                        if (addressList!=null&&addressList.size()>0){
                            for (int i = 0; i<addressList.get(0).getMaxAddressLineIndex(); i++){
                                addsholder += addressList.get(0).getAddressLine(i);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    marker = mMap.addMarker(new MarkerOptions().position(latLng).title(addsholder));
                    System.out.println("Done With the Marker and Address");
                    System.out.println("Adds: "+addsholder);
                }
            });
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        locationManager.requestLocationUpdates(provider, 400, 1, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }
}
