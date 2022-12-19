package com.example.pollingsystem.ui.resultsmap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pollingsystem.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.LinkedList;

public class MapsFragment extends Fragment {
    LinkedList<Double> latitudes;
    LinkedList<Double> longitudes;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap googleMap) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                for (int i = 0; i < latitudes.stream().count(); i++) {
                    googleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(latitudes.get(i), longitudes.get(i))));
                }
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        if (args != null) {
            latitudes = (LinkedList<Double>) getArguments().getSerializable("latitudes");
            longitudes = (LinkedList<Double>) getArguments().getSerializable("longitudes");
        } else {
            // The arguments are not set
        }
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }
}