package com.school.eventrra.fragment;

import com.school.eventrra.model.Event;
import com.school.eventrra.util.DataSet;
import com.school.eventrra.util.DateUtil;

import java.util.List;

public class ThisWeekendFragment extends BaseSubHomeFragment {

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {
            onResume();
        }
    }

    @Override
    void fetchData() {
        fetchEvent();
    }

    @Override
    List<Event> filterData() {
        return DataSet.getEvents(DateUtil.getFirstDayOfCurrentWeek(),
                DateUtil.getFirstDayOfNextWeek());
    }

    @Override
    void afterFavStatusChange(int position, Event event, boolean isFavorite) {
    }
}
