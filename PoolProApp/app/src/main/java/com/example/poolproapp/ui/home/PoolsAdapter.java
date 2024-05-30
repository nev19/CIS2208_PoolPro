package com.example.poolproapp.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.poolproapp.Pool;
import com.example.poolproapp.R;
import java.util.List;

public class PoolsAdapter extends RecyclerView.Adapter<PoolsAdapter.PoolViewHolder> {

    private List<Pool> pools;
    private OnItemClickListener listener;

    public PoolsAdapter(List<Pool> pools, OnItemClickListener listener) {
        this.pools = pools;
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(Pool pool, int poolId);
    }

    @NonNull
    @Override
    public PoolViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pool, parent, false);
        return new PoolViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PoolViewHolder holder, int position) {
        final Pool pool = pools.get(position);
        holder.bind(pool, listener);
    }

    @Override
    public int getItemCount() {
        return pools.size();
    }

    public static class PoolViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName;
        TextView textViewCapacity;

        public PoolViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewCapacity = itemView.findViewById(R.id.textViewCapacity);
        }

        public void bind(final Pool pool, final OnItemClickListener listener) {
            textViewName.setText(pool.getName());
            textViewCapacity.setVisibility(View.GONE); // Hide the capacity TextView

            // Set click listener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(pool, (int) pool.getId()); // Pass the pool ID
                }
            });
        }
    }
}
