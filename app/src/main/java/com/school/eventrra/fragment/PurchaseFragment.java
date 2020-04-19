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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.school.eventrra.R;
import com.school.eventrra.activity.PurchasedDetailActivity;
import com.school.eventrra.adapter.RegisterRvAdapter;
import com.school.eventrra.callback.OnRvItemClickListener;
import com.school.eventrra.model.Register;
import com.school.eventrra.util.Constants;
import com.school.eventrra.util.DataSet;
import com.school.eventrra.util.DialogUtil;
import com.school.eventrra.util.FirebaseUtil;

public class PurchaseFragment extends Fragment implements OnRvItemClickListener<Register> {
    private DatabaseReference myRef;

    private RegisterRvAdapter adapter;
    private ProgressDialog progressDialog;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);
        RecyclerView rv = root.findViewById(R.id.rv);
        adapter = new RegisterRvAdapter(this);
        rv.setAdapter(adapter);
        if (getContext() != null) {
            rv.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        }

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Processing...");
        progressDialog.setCancelable(false);

        myRef = FirebaseDatabase.getInstance().getReference(Constants.TABLE_REGISTER);

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
                boolean ascending = adapter.triggerDateSort();
                imgSort.setRotation(ascending ? 180 : 0);
            }
        });

        fetchData();

        return root;
    }

    @Override
    public void onClick(int position, Register register) {
        DataSet.selectedRegister = register;
        startActivity(new Intent(getContext(), PurchasedDetailActivity.class));
    }

    @Override
    public void onLongClick(int position, final Register register) {
        if (DataSet.isAdmin) {
            DialogUtil.showDeleteConfirmDialog(getContext(),
                    register.getEventTitle(),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteRegister(register);
                        }
                    });
        }
    }

    private void fetchData() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(getContext(), "Sign in first", Toast.LENGTH_SHORT).show();
            return;
        }

        myRef.orderByChild("email")
                .equalTo(currentUser.getEmail())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        adapter.setDataSet(FirebaseUtil.parsePurchaseList(dataSnapshot));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
    }

    private void deleteRegister(final Register register) {
        progressDialog.show();
        myRef.child(register.getId())
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            adapter.removeItem(register);
                        } else {
                            Toast.makeText(getContext(), "fail to delete!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
