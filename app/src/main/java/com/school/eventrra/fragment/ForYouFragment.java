package com.school.eventrra.fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.school.eventrra.util.DataSet;

import java.io.IOException;
import java.util.List;

public class ForYouFragment extends BaseSubHomeFragment {

    @Override
    void fetchData() {
        filterEvents();
    }

    private void filterEvents() {
        String currentLocation = getCurrentLocation();
        if (currentLocation == null) {
            Toast.makeText(getContext(), "Fail to get current location", Toast.LENGTH_SHORT).show();
            return;
        }

        adapter.setDataSet(DataSet.getEvents(currentLocation));
    }

    private String getCurrentLocation() {
        if (getContext() == null) {
            return null;
        }

        LocationManager lm = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        if (lm == null) {
            return null;
        }

        Geocoder geocoder = new Geocoder(getContext());
        for (String provider : lm.getAllProviders()) {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return null;
            }

            Location location = lm.getLastKnownLocation(provider);
            if (location == null) {
                return null;
            }

            try {
                List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                if (addresses == null || addresses.isEmpty()) {
                    return null;
                }
                return addresses.get(0).getCountryName();
            } catch (IOException e) {
                return null;
            }
        }

        return null;
    }
}
