package com.school.eventrra.util;

import com.google.firebase.database.DataSnapshot;
import com.school.eventrra.model.Event;
import com.school.eventrra.model.Register;

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

    public static List<String> parseWishlist(DataSnapshot dataSnapshot) {
        List<String> wishlist = new ArrayList<>();

        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
            wishlist.add(snapshot.getValue(String.class));
        }

        return wishlist;
    }

    public static List<Register> parsePurchaseList(DataSnapshot dataSnapshot) {
        List<Register> registers = new ArrayList<>();

        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
            registers.add(snapshot.getValue(Register.class));
        }

        return registers;
    }
}
