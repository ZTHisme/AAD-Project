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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EventRvAdapter extends RecyclerView.Adapter<EventRvAdapter.ViewHolder>
        implements Filterable {
    private Context context;
    private List<Event> dataSet;
    private List<Event> filteredDataSet;
    private OnRvItemClickListener<Event> onRvItemClickListener;

    public EventRvAdapter(Context context, OnRvItemClickListener<Event> onRvItemClickListener) {
        this.context = context;
        this.onRvItemClickListener = onRvItemClickListener;
    }

    public void setDataSet(List<Event> dataSet) {
        this.dataSet = dataSet;
        this.filteredDataSet = dataSet;
        notifyDataSetChanged();
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
        // TODO: 4/16/2020 if include in favorite list, then use fill heart, else border heart icon
        holder.imgFav.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_favorite_black_24dp));
        holder.tvDatetime.setText(new Date().toString());
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
