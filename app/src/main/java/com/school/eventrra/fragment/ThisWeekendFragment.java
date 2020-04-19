package com.school.eventrra.fragment;

import com.school.eventrra.util.DataSet;
import com.school.eventrra.util.DateUtil;

public class ThisWeekendFragment extends BaseSubHomeFragment {

    @Override
    void fetchData() {
        filterEvents();
    }

    private void filterEvents() {
        adapter.setDataSet(DataSet.getEvents(DateUtil.getFirstDayOfCurrentWeek(),
                DateUtil.getFirstDayOfNextWeek()));
    }
}
