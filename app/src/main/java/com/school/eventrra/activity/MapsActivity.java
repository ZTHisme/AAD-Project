package com.school.eventrra.activity;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.school.eventrra.R;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        LocationListener {
    public static final String PARAM_LAT = "PARAM_LAT";
    public static final String PARAM_LNG = "PARAM_LNG";
    public static final String PARAM_TITLE = "PARAM_TITLE";
    public static final String PARAM_SET_LONG_CLICK_LISTENER = "PARAM_SET_LONG_CLICK_LISTENER";
    public static final String RESULT_LAT_LNG = "RESULT_LAT_LNG";

    private double lat, lng;
    private String title;
    private boolean setLongClickListener;
    private LatLng location;
    private float zoom;

    private GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Bundle b = getIntent().getExtras();
        if (b == null) {
            Toast.makeText(this, "Nothing to do", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        lat = b.getDouble(PARAM_LAT);
        lng = b.getDouble(PARAM_LNG);
        title = b.getString(PARAM_TITLE);
        setLongClickListener = b.getBoolean(PARAM_SET_LONG_CLICK_LISTENER);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        boolean useCurrentLocation = false;

        if (lat != 0 && lng != 0) {
            // Add a marker in location and move the camera with zoom in animation
            location = new LatLng(lat, lng);
            zoom = 15.0f;
        } else {
            // Add a default marker in location(Shwe Dagon Pagoda)
            // and move the camera with zoom in animation
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (locationManager != null &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                useCurrentLocation = true;
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

                Toast.makeText(this,
                        "Please Wait a sec! To get the current location.",
                        Toast.LENGTH_SHORT).show();
            } else {
                location = new LatLng(16.798625, 96.149513);
            }
            zoom = 12.0f;
        }

        if (!useCurrentLocation) {
            showLocationOnMap();
        }

        if (setLongClickListener) {
            this.googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(LatLng latLng) {
                    Intent i = new Intent();
                    i.putExtra(RESULT_LAT_LNG, latLng);
                    setResult(RESULT_OK, i);
                    finish();
                }
            });
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        if (this.location == null) {
            this.location = new LatLng(location.getLatitude(), location.getLongitude());
            showLocationOnMap();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    private void showLocationOnMap() {
        this.googleMap.addMarker(new MarkerOptions().position(location).title(title));
        this.googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, zoom));
    }
}
