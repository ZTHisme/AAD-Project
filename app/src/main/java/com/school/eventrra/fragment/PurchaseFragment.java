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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.school.eventrra.R;
import com.school.eventrra.activity.PurchasedDetailActivity;
import com.school.eventrra.adapter.RegisterRvAdapter;
import com.school.eventrra.callback.OnRvItemClickListener;
import com.school.eventrra.model.Register;
import com.school.eventrra.util.Constants;
import com.school.eventrra.util.DataSet;
import com.school.eventrra.util.FirebaseUtil;

public class PurchaseFragment extends Fragment implements OnRvItemClickListener<Register> {
    private RegisterRvAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);
        RecyclerView rv = root.findViewById(R.id.rv);
        adapter = new RegisterRvAdapter(this);
        rv.setAdapter(adapter);
        if (getContext() != null) {
            rv.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        }

        fetchData();

        return root;
    }

    @Override
    public void onClick(int position, Register register) {
        DataSet.selectedRegister = register;
        startActivity(new Intent(getContext(), PurchasedDetailActivity.class));
    }

    @Override
    public void onLongClick(int position, Register register) {
        if (DataSet.isAdmin) {
            // TODO: 4/16/2020 show edit or delete option
            Toast.makeText(getContext(), "onLongClick: " + register.getEventTitle(), Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchData() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(getContext(), "Sign in first", Toast.LENGTH_SHORT).show();
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Processing...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        FirebaseDatabase.getInstance()
                .getReference(Constants.TABLE_REGISTER)
                .orderByChild("email")
                .equalTo(currentUser.getEmail())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        progressDialog.dismiss();
                        adapter.setDataSet(FirebaseUtil.parsePurchaseList(dataSnapshot));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        progressDialog.dismiss();
                    }
                });
    }
}
