package com.school.eventrra.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.school.eventrra.R;
import com.school.eventrra.callback.OnRvItemClickListener;
import com.school.eventrra.model.Register;
import com.school.eventrra.util.DateUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RegisterRvAdapter extends RecyclerView.Adapter<RegisterRvAdapter.ViewHolder>
        implements Filterable {
    private List<Register> dataSet;
    private List<Register> filteredDataSet;
    private OnRvItemClickListener<Register> onRvItemClickListener;
    private boolean ascending;

    public RegisterRvAdapter(OnRvItemClickListener<Register> onRvItemClickListener) {
        this.onRvItemClickListener = onRvItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.registered_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Register register = filteredDataSet.get(holder.getAdapterPosition());

        holder.tvDate.setText(DateUtil.stdDateFormat(register.getDate()));
        holder.tvUsername.setText(register.getUsername());
        holder.tvEventTitle.setText(register.getEventTitle());
    }

    @Override
    public int getItemCount() {
        return filteredDataSet == null ? 0 : filteredDataSet.size();
    }

    public void setDataSet(List<Register> dataSet) {
        this.dataSet = dataSet;
        this.filteredDataSet = dataSet;
        notifyDataSetChanged();
    }

    public void removeItem(Register register) {
        this.dataSet.remove(register);
        this.filteredDataSet.remove(register);
        notifyDataSetChanged();
    }

    public boolean triggerDateSort() {
        ascending = !ascending;

        Collections.sort(this.filteredDataSet, new Comparator<Register>() {
            @Override
            public int compare(Register o1, Register o2) {
                return ascending
                        ? (int) (o1.getDate().getTime() - o2.getDate().getTime())
                        : (int) (o2.getDate().getTime() - o1.getDate().getTime());
            }
        });

        notifyDataSetChanged();

        return ascending;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String searchTerm = charSequence.toString().trim().toLowerCase();

                final List<Register> filteredList = new ArrayList<>();
                if (searchTerm.isEmpty()) {
                    filteredList.addAll(new ArrayList<>(dataSet));
                } else {
                    for (Register register : dataSet) {
                        if (register.getEventTitle().toLowerCase().contains(searchTerm) ||
                                register.getUsername().toLowerCase().contains(searchTerm) ||
                                register.getPhone().toLowerCase().contains(searchTerm) ||
                                register.getEmail().toLowerCase().contains(searchTerm) ||
                                register.getAddress().toLowerCase().contains(searchTerm)) {
                            filteredList.add(register);
                        }
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredDataSet = (List<Register>) filterResults.values;

                notifyDataSetChanged();
            }
        };
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvDate, tvUsername, tvEventTitle;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvDate = itemView.findViewById(R.id.tv_date);
            tvUsername = itemView.findViewById(R.id.tv_username);
            tvEventTitle = itemView.findViewById(R.id.tv_event_title);

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
