package com.school.eventrra.fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.school.eventrra.R;
import com.school.eventrra.activity.EventActivity;
import com.school.eventrra.adapter.EventRvAdapter;
import com.school.eventrra.callback.OnRvItemClickListener;
import com.school.eventrra.model.Event;
import com.school.eventrra.util.Constants;
import com.school.eventrra.util.DataSet;
import com.school.eventrra.util.FirebaseUtil;

import java.io.IOException;
import java.util.List;

public class ForYouFragment extends Fragment implements OnRvItemClickListener<Event> {
    private ProgressDialog progressDialog;
    private EventRvAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);
        RecyclerView rv = root.findViewById(R.id.rv);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Processing...");
        progressDialog.setCancelable(false);

        adapter = new EventRvAdapter(getContext(), this);
        rv.setAdapter(adapter);
        if (getContext() != null) {
            rv.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        }

        fetchData();

        return root;
    }

    private void fetchData() {
        String currentLocation = getCurrentLocation();
        if (currentLocation == null) {
            Toast.makeText(getContext(), "Fail to get current location", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(Constants.TABLE_EVENT);
        progressDialog.show();
        myRef.orderByChild("location")
                .equalTo(currentLocation)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        progressDialog.dismiss();
                        adapter.setDataSet(FirebaseUtil.parseEventList(dataSnapshot));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        progressDialog.dismiss();
                    }
                });
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

    @Override
    public void onClick(int position, Event event) {
        DataSet.selectedEvent = event;
        startActivity(new Intent(getContext(), EventActivity.class));
    }

    @Override
    public void onLongClick(int position, Event event) {
        if (DataSet.isAdmin) {
            // TODO: 4/16/2020 show edit or delete option
            Toast.makeText(getContext(), "onLongClick: " + event.getTitle(), Toast.LENGTH_SHORT).show();
        }
    }
}
