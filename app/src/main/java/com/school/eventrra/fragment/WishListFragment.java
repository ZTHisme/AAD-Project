package com.school.eventrra.fragment;

import com.school.eventrra.model.Event;
import com.school.eventrra.util.DataSet;

import java.util.List;

public class WishListFragment extends BaseSubHomeFragment {

    @Override
    void fetchData() {
        fetchEvent();
    }

    @Override
    List<Event> filterData() {
        return DataSet.getWishlistEvents();
    }
}
