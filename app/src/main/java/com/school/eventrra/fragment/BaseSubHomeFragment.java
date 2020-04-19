package com.school.eventrra.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.school.eventrra.R;
import com.school.eventrra.activity.EventActivity;
import com.school.eventrra.adapter.EventRvAdapter;
import com.school.eventrra.callback.OnRvItemClickListener;
import com.school.eventrra.model.Event;
import com.school.eventrra.util.DataSet;

public abstract class BaseSubHomeFragment extends Fragment implements OnRvItemClickListener<Event> {
    ProgressDialog progressDialog;
    EventRvAdapter adapter;

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

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchData();
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

    abstract void fetchData();
}
