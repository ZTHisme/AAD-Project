package com.school.eventrra.util;

import com.school.eventrra.model.Event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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

    public static List<String> getCountries() {
        Locale[] locales = Locale.getAvailableLocales();
        ArrayList<String> countries = new ArrayList<>();
        for (Locale locale : locales) {
            String country = locale.getDisplayCountry();
            if (country.trim().length() > 0 && !countries.contains(country)) {
                countries.add(country);
            }
        }

        Collections.sort(countries);

        return countries;
    }
}
