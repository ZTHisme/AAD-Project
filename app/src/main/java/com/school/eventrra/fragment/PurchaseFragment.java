package com.school.eventrra.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.school.eventrra.R;
import com.school.eventrra.activity.PurchasedDetailActivity;
import com.school.eventrra.adapter.RegisterRvAdapter;
import com.school.eventrra.callback.OnRvItemClickListener;
import com.school.eventrra.model.Register;
import com.school.eventrra.util.DataSet;

public class PurchaseFragment extends Fragment implements OnRvItemClickListener<Register> {
    public static Register selectedRegister;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);
        RecyclerView rv = root.findViewById(R.id.rv);
        RegisterRvAdapter adapter = new RegisterRvAdapter(this);
        // TODO: 4/16/2020 get data from api and set to adapter
        adapter.setDataSet(DataSet.getDummyRegisters());
        rv.setAdapter(adapter);
        if (getContext() != null) {
            rv.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        }
        return root;
    }

    @Override
    public void onClick(int position, Register register) {
        PurchaseFragment.selectedRegister = register;
        startActivity(new Intent(getContext(), PurchasedDetailActivity.class));
    }

    @Override
    public void onLongClick(int position, Register register) {
        // nothing to do
    }
}
