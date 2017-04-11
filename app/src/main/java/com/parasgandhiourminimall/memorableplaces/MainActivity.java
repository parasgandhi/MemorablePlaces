package com.parasgandhiourminimall.memorableplaces;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView placesList;
    String adds="";
    static ArrayAdapter arrayAdapter;
    static ArrayList<String> places = new ArrayList<>();
    static ArrayList<LatLng> placesLoc = new ArrayList<>();
    Intent i = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(places.size()==0){
            places.add(0,"Add a new place");
        }
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,places);
        placesList = (ListView) findViewById(R.id.placesList);
        placesList.setAdapter(arrayAdapter);
        placesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (places.get(i).equals("Add a new place")){
                    Intent intent = new Intent(getApplicationContext(),MapsActivity.class);
                    intent.putExtra("Intent Type","add");
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(getApplicationContext(),MapsActivity.class);
                    intent.putExtra("Intent Type",String.valueOf(i));
                    Log.i("Index Sent",String.valueOf(i));
                    startActivity(intent);
                }
            }

        });
        if (placesLoc.size()>0){
            for(LatLng latLng : placesLoc){
                Log.i("Location at index",latLng.toString());
            }
        }
    }

}
