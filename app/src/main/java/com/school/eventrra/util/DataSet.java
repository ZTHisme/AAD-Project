package com.school.eventrra.util;

import com.school.eventrra.model.Event;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DataSet {

    public static List<Event> getDummyEvents() {
        List<Event> events = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            Event event = new Event();
            event.setDate(new Date().getTime());
            event.setTitle("Title " + i);
            event.setLocation("Location " + i);
            events.add(event);
        }

        return events;
    }
}
