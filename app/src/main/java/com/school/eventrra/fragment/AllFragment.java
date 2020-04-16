package com.school.eventrra.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.school.eventrra.R;
import com.school.eventrra.adapter.EventRvAdapter;
import com.school.eventrra.callback.OnRvItemClickListener;
import com.school.eventrra.model.Event;
import com.school.eventrra.util.DataSet;

public class AllFragment extends Fragment implements OnRvItemClickListener<Event> {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);
        RecyclerView rv = root.findViewById(R.id.rv);
        EventRvAdapter adapter = new EventRvAdapter(getContext(), this);
        // TODO: 4/16/2020 get data from api and set to adapter
        adapter.setDataSet(DataSet.getDummyEvents());
        rv.setAdapter(adapter);
        return root;
    }

    @Override
    public void onClick(int position, Event event) {
        // TODO: 4/16/2020 go to detail page
        Toast.makeText(getContext(), "onClick: " + event.getTitle(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLongClick(int position, Event event) {
        // TODO: 4/16/2020 show edit or delete option
        Toast.makeText(getContext(), "onLongClick: " + event.getTitle(), Toast.LENGTH_SHORT).show();
    }
}
