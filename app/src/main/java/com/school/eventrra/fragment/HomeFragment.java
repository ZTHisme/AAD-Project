package com.school.eventrra.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.school.eventrra.R;
import com.school.eventrra.adapter.SectionsPagerAdapter;

public class HomeFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        if (getActivity() == null) {
            return null;
        }

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        SectionsPagerAdapter sectionsPagerAdapter =
                new SectionsPagerAdapter(getContext(), getChildFragmentManager());
        ViewPager viewPager = root.findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = root.findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        return root;
    }
}
