package com.example.group22_hw09;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private final OkHttpClient client = new OkHttpClient();
    LatLongResponse latLongResponse;
    ArrayList<LatLng> latLngArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Request request = new Request.Builder()
                .url("https://www.theappsdr.com/map/route")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Gson gson = new Gson();
                latLongResponse = gson.fromJson(Objects.requireNonNull(response.body()).string(), LatLongResponse.class);
                Log.d("TAG", "onResponse: "+ latLongResponse);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        if (latLngArrayList != null){
            Polyline polyline1 = googleMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .addAll(latLngArrayList));

            googleMap.addMarker(new MarkerOptions()
                    .position(latLngArrayList.get(0))
                    .title("Start location"));

            googleMap.addMarker(new MarkerOptions()
                    .position(latLngArrayList.get(latLngArrayList.size()))
                    .title("End location"));
        }else{
            Log.d("TAG", "onMapReady: ArrayList empty");
        }

        }
    }

    class LatLongResponse {
    String title;
    ArrayList<LatLng> latLngArrayList;

    @Override
    public String toString() {
        return "LatLongResponse{" +
                "title='" + title + '\'' +
                ", latLngArrayList=" + latLngArrayList +
                '}';
    }
}
