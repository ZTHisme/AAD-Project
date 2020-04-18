package com.school.eventrra.util;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;

class FirebaseListParser<T> {

    List<T> parse(DataSnapshot dataSnapshot) {
        List<T> list = new ArrayList<>();
        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
            list.add((T) snapshot.getValue());
        }
        return list;
    }
}
