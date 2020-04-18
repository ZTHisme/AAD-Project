package com.school.eventrra.util;

import com.school.eventrra.model.Event;
import com.school.eventrra.model.Register;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DataSet {
    public static Event selectedEvent;
    public static boolean isAdmin;

    public static List<Event> getDummyEvents() {
        List<Event> events = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            Event event = new Event();
            event.setDate(new Date());
            event.setFromDate(new Date());
            event.setToDate(new Date());
            event.setTitle("Title " + i);
            event.setLocation("Location " + i);
            event.setAbout("About About About About About About About About About About About " + i);
            event.setPrice("Price " + i);
            events.add(event);
        }

        return events;
    }

    public static List<Register> getDummyRegisters() {
        List<Register> registers = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            Register register = new Register();
            register.setDate(new Date());
            register.setUsername("Username " + i);
            register.setEventTitle("Event Title " + i);
            registers.add(register);
        }

        return registers;
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
