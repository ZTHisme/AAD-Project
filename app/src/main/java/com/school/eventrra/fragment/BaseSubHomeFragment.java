package com.school.eventrra.fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.school.eventrra.R;
import com.school.eventrra.activity.EventActivity;
import com.school.eventrra.activity.EventEditorActivity;
import com.school.eventrra.adapter.EventRvAdapter;
import com.school.eventrra.callback.OnRvItemClickListener;
import com.school.eventrra.model.Event;
import com.school.eventrra.util.Constants;
import com.school.eventrra.util.DataSet;
import com.school.eventrra.util.DialogUtil;
import com.school.eventrra.util.FirebaseUtil;

import java.util.List;

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

        EditText edtSearch = root.findViewById(R.id.edt_search);
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                adapter.getFilter().filter(s);
            }
        });

        final ImageView imgSort = root.findViewById(R.id.img_sort);
        imgSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean ascending = adapter.triggerCountrySort();
                imgSort.setRotation(ascending ? 180 : 0);
            }
        });

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
    public void onLongClick(int position, final Event event) {
        if (DataSet.isAdmin) {
            DialogUtil.showEditOrDeleteOptionDialog(getContext(),
                    new DialogUtil.EditOrDeleteCallback() {
                        @Override
                        public void edit() {
                            editEvent(event);
                        }

                        @Override
                        public void delete() {
                            DialogUtil.showDeleteConfirmDialog(getContext(),
                                    event.getTitle(),
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            deleteEvent(event);
                                        }
                                    });
                        }
                    });
        }
    }

    private void editEvent(Event event) {
        DataSet.selectedEvent = event;
        startActivity(new Intent(getContext(), EventEditorActivity.class));
    }

    private void deleteEvent(final Event event) {
        progressDialog.show();
        FirebaseDatabase.getInstance()
                .getReference(Constants.TABLE_EVENT)
                .child(event.getId())
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            adapter.removeItem(event);
                        } else {
                            Toast.makeText(getContext(), "Fail to delete!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    void fetchEvent() {
        FirebaseDatabase.getInstance()
                .getReference(Constants.TABLE_EVENT)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        DataSet.events = FirebaseUtil.parseEventList(dataSnapshot);
                        adapter.setDataSet(filterData());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
    }

    abstract void fetchData();

    abstract List<Event> filterData();
}
