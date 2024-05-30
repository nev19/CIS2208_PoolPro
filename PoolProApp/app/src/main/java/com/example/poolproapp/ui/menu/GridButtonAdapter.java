package com.example.poolproapp.ui.menu;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.poolproapp.R;

import java.util.List;

public class GridButtonAdapter extends RecyclerView.Adapter<GridButtonAdapter.ViewHolder> {

    private List<Integer> imageResources; // List of image resources
    private Context context;
    private int poolId;

    public GridButtonAdapter(Context context, List<Integer> imageResources, int poolId) {
        this.context = context;
        this.imageResources = imageResources;
        this.poolId = poolId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grid_button, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int imageResource = imageResources.get(position);
        holder.imageView.setImageResource(imageResource);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int clickedPosition = holder.getAdapterPosition();
                if (clickedPosition != RecyclerView.NO_POSITION) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("poolId", poolId); // Pass the poolId to the next fragment
                    switch (clickedPosition) {
                        case 0:
                            Navigation.findNavController(v).navigate(R.id.action_menuFragment_to_chlorineFragment, bundle);
                            break;
                        case 1:
                            Navigation.findNavController(v).navigate(R.id.action_menuFragment_to_acidityFragment, bundle);
                            break;
                        case 2:
                            Navigation.findNavController(v).navigate(R.id.action_menuFragment_to_requestFragment, bundle);
                            break;
                        case 3:
                            Navigation.findNavController(v).navigate(R.id.action_menuFragment_to_refillFragment, bundle);
                            break;
                        default:
                            break;
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return imageResources.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_button);
        }
    }
}
