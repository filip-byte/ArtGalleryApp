package com.example.artgalleryapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.artgalleryapp.R;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GalleryArtworksFragment extends Fragment {
    private Long galleryId;
    private RecyclerView recyclerView;
    private ProgressBar loadingIndicator;
    private ArtRepository repository = new ArtRepository();

    public GalleryArtworksFragment(Long galleryId) {
        this.galleryId = galleryId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery_artworks, container, false);
        recyclerView = view.findViewById(R.id.artworks_recycler_view);
        loadingIndicator = view.findViewById(R.id.loading_indicator);

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setAdapter(new ArtworkAdapter(new ArrayList<>(), artwork -> {}, getParentFragmentManager()));

        loadArtworks();
        return view;
    }

    private void loadArtworks() {
        loadingIndicator.setVisibility(View.VISIBLE);
        repository.getGalleryArtworks(galleryId, new Callback<List<Artwork>>() {
            @Override
            public void onResponse(Call<List<Artwork>> call, Response<List<Artwork>> response) {
                loadingIndicator.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    List<Artwork> artworks = response.body();
                    Log.d("GalleryArtworks", "Gallery ID: " + galleryId + ", Artworks: " + artworks.size());
                    for (Artwork artwork : artworks) {
                        Log.d("GalleryArtworks", "Title: " + artwork.getTitle() + ", ImageUrl: " + artwork.getImageUrl());
                    }
                    recyclerView.setAdapter(new ArtworkAdapter(artworks, artwork -> {}, getParentFragmentManager()));
                } else {
                    Toast.makeText(getContext(), "Error fetching artworks", Toast.LENGTH_SHORT).show();
                    Log.e("GalleryArtworks", "Response failed: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Artwork>> call, Throwable t) {
                loadingIndicator.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("GalleryArtworks", "Network error: " + t.getMessage());
            }
        });
    }
}