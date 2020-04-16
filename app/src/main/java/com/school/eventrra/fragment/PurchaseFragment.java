package com.school.eventrra.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.school.eventrra.R;

public class PurchaseFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_purchase, container, false);
        final TextView textView = root.findViewById(R.id.text_purchase);
        textView.setText("This is purchase fragment");
        return root;
    }
}
