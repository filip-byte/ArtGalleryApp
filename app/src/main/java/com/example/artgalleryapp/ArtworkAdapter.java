package com.example.artgalleryapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.List;

public class ArtworkAdapter extends RecyclerView.Adapter<ArtworkAdapter.ViewHolder> {
    private List<Artwork> artworks;
    private OnAddClickListener addClickListener;

    public ArtworkAdapter(List<Artwork> artworks, OnAddClickListener listener) {
        this.artworks = artworks;
        this.addClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_artwork, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Artwork artwork = artworks.get(position);
        holder.titleTextView.setText(artwork.getTitle());
        Picasso.get().load(artwork.getImageUrl()).into(holder.imageView);
        holder.addButton.setOnClickListener(v -> addClickListener.onAddClick(artwork));
    }

    @Override
    public int getItemCount() {
        return artworks.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleTextView;
        ImageButton addButton;

        ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.artwork_image);
            titleTextView = itemView.findViewById(R.id.artwork_title);
            addButton = itemView.findViewById(R.id.add_button);
        }
    }

    interface OnAddClickListener {
        void onAddClick(Artwork artwork);
    }
}