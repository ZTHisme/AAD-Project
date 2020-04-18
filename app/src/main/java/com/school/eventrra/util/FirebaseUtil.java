package com.school.eventrra.util;

import com.google.firebase.database.DataSnapshot;
import com.school.eventrra.model.Event;

import java.util.ArrayList;
import java.util.List;

public class FirebaseUtil {

    public static List<Event> parseEventList(DataSnapshot dataSnapshot) {
        List<Event> events = new ArrayList<>();

        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
            events.add(snapshot.getValue(Event.class));
        }

        return events;
    }

//    public static List<Event> parseEventList(DataSnapshot dataSnapshot) {
//        return new FirebaseListParser<Event>().parse(dataSnapshot);
//    }

    public static List<String> parseWishlist(DataSnapshot dataSnapshot) {
        return new FirebaseListParser<String>().parse(dataSnapshot);
    }
}
