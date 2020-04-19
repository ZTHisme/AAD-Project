package com.school.eventrra.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.school.eventrra.R;
import com.school.eventrra.callback.OnRvItemClickListener;
import com.school.eventrra.model.Register;
import com.school.eventrra.util.DateUtil;

import java.util.List;

public class RegisterRvAdapter extends RecyclerView.Adapter<RegisterRvAdapter.ViewHolder> {
    private List<Register> dataSet;
    private OnRvItemClickListener<Register> onRvItemClickListener;

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
        final Register register = dataSet.get(holder.getAdapterPosition());

        holder.tvDate.setText(DateUtil.stdDateFormat(register.getDate()));
        holder.tvUsername.setText(register.getUsername());
        holder.tvEventTitle.setText(register.getEventTitle());
    }

    @Override
    public int getItemCount() {
        return dataSet == null ? 0 : dataSet.size();
    }

    public void setDataSet(List<Register> dataSet) {
        this.dataSet = dataSet;
        notifyDataSetChanged();
    }

    public void removeItem(Register register) {
        this.dataSet.remove(register);
        notifyDataSetChanged();
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
                    onRvItemClickListener.onClick(position, dataSet.get(position));
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = getAdapterPosition();
                    onRvItemClickListener.onLongClick(position, dataSet.get(position));
                    return true;
                }
            });
        }
    }
}
