package com.school.eventrra.callback;

import com.school.eventrra.model.Event;

public interface EventRvItemClickListener extends OnRvItemClickListener<Event> {
    void onFavClick(int position, Event event);
}
