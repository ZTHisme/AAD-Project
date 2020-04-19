package com.school.eventrra.fragment;

import com.school.eventrra.util.DataSet;

public class WishListFragment extends BaseSubHomeFragment {

    @Override
    void fetchData() {
        filterEvents();
    }

    private void filterEvents() {
        adapter.setDataSet(DataSet.getWishlistEvents());
    }
}
