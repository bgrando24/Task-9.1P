package com.example.task_91p;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.task_91p.databinding.ActivityMapsBinding;

import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        //        get all items from database
        DataManager dm = new DataManager(this);
        List<LostFoundItem> items = dm.getAllItems();
//        get lat/lon values, find averages of lat and lon to get midpoint
        double avgLat = 0;
        double avgLon = 0;
        LatLng midpoint;

        if (items.isEmpty()) {
//            default is sydney
            midpoint = new LatLng(-34, 151);
        } else {
//            calculate midpoint
            for (LostFoundItem item : items) {
                avgLat += item.getLat();
                avgLon += item.getLon();
            }

            avgLat /= items.size();
            avgLon /= items.size();
            midpoint = new LatLng(avgLat, avgLon);

//            add markers for each item
            for (LostFoundItem item : items) {
                LatLng itemLocation = new LatLng(item.getLat(), item.getLon());
                mMap.addMarker(new MarkerOptions().position(itemLocation).title(item.getName()));
            }
        }


        mMap = googleMap;

        // Begin camera at midpoint
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(midpoint));
    }
}