package com.school.eventrra.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.school.eventrra.R;
import com.school.eventrra.callback.OnRvItemClickListener;
import com.school.eventrra.model.Event;
import com.school.eventrra.util.BitmapUtil;
import com.school.eventrra.util.DataSet;
import com.school.eventrra.util.DateUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class EventRvAdapter extends RecyclerView.Adapter<EventRvAdapter.ViewHolder>
        implements Filterable {
    private Context context;
    private List<Event> dataSet;
    private List<Event> filteredDataSet;
    private OnRvItemClickListener<Event> onRvItemClickListener;
    private boolean ascending;

    public EventRvAdapter(Context context, OnRvItemClickListener<Event> onRvItemClickListener) {
        this.context = context;
        this.onRvItemClickListener = onRvItemClickListener;
    }

    public void setDataSet(List<Event> dataSet) {
        this.dataSet = dataSet;
        this.filteredDataSet = dataSet;
        notifyDataSetChanged();
    }

    public void removeItem(Event event) {
        this.dataSet.remove(event);
        this.filteredDataSet.remove(event);
        notifyDataSetChanged();
    }

    public boolean triggerCountrySort() {
        ascending = !ascending;

        Collections.sort(this.filteredDataSet, new Comparator<Event>() {
            @Override
            public int compare(Event o1, Event o2) {
                return ascending
                        ? o1.getLocation().compareTo(o2.getLocation())
                        : o2.getLocation().compareTo(o1.getLocation());
            }
        });

        notifyDataSetChanged();

        return ascending;
    }

    @NonNull
    @Override
    public EventRvAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull EventRvAdapter.ViewHolder holder, int position) {
        final Event event = filteredDataSet.get(holder.getAdapterPosition());
        holder.img.setImageBitmap(BitmapUtil.base64StringToBitmap(event.getImageBase64()));
        holder.imgFav.setImageDrawable(context.getResources().getDrawable(
                DataSet.isWishlist(event.getId())
                        ? R.drawable.ic_favorite_black_24dp
                        : R.drawable.ic_favorite_border_black_24dp));
        holder.tvDatetime.setText(DateUtil.stdDateFormat(event.getDate()));
        holder.tvTitle.setText(event.getTitle());
        holder.tvLocation.setText(event.getLocation());
    }

    @Override
    public int getItemCount() {
        return filteredDataSet == null ? 0 : filteredDataSet.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String searchTerm = charSequence.toString().trim().toLowerCase();

                final List<Event> filteredList = new ArrayList<>();
                if (searchTerm.isEmpty()) {
                    filteredList.addAll(new ArrayList<>(dataSet));
                } else {
                    for (Event event : dataSet) {
                        if (event.getTitle().toLowerCase().contains(searchTerm) ||
                                event.getLocation().toLowerCase().contains(searchTerm)) {
                            filteredList.add(event);
                        }
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredDataSet = (List<Event>) filterResults.values;

                notifyDataSetChanged();
            }
        };
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView img, imgFav;
        private TextView tvDatetime, tvTitle, tvLocation;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.img);
            imgFav = itemView.findViewById(R.id.img_fav);
            tvDatetime = itemView.findViewById(R.id.tv_datetime);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvLocation = itemView.findViewById(R.id.tv_location);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    onRvItemClickListener.onClick(position, filteredDataSet.get(position));
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = getAdapterPosition();
                    onRvItemClickListener.onLongClick(position, filteredDataSet.get(position));
                    return true;
                }
            });
        }
    }
}
