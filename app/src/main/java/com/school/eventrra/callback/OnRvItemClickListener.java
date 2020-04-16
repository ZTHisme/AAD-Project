package com.school.eventrra.callback;

public interface OnRvItemClickListener<T> {
    void onClick(int position, T t);

    void onLongClick(int position, T t);
}
