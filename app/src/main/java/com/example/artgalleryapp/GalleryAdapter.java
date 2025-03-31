package com.example.artgalleryapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {
    private List<Gallery> galleries;
    private OnGalleryClickListener clickListener;

    public GalleryAdapter(List<Gallery> galleries, OnGalleryClickListener listener) {
        this.galleries = galleries;
        this.clickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_gallery, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Gallery gallery = galleries.get(position);
        holder.nameTextView.setText(gallery.getName());
        holder.itemView.setOnClickListener(v -> clickListener.onGalleryClick(gallery));
    }

    @Override
    public int getItemCount() {
        return galleries.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;

        ViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.gallery_name);
        }
    }

    interface OnGalleryClickListener {
        void onGalleryClick(Gallery gallery);
    }
}