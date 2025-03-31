package com.example.artgalleryapp;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.artgalleryapp.R;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BrowseFragment extends Fragment {
    private Spinner sourceSpinner;
    private EditText searchInput;
    private RecyclerView recyclerView;
    private ProgressBar loadingIndicator;
    private Button nextPageButton;
    private ArtRepository repository = new ArtRepository();
    private List<Gallery> galleries;
    private int currentPage = 1;
    private String lastSource;
    private String lastQuery;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_browse, container, false);

        sourceSpinner = view.findViewById(R.id.source_spinner);
        searchInput = view.findViewById(R.id.search_input);
        recyclerView = view.findViewById(R.id.artworks_recycler_view);
        loadingIndicator = view.findViewById(R.id.loading_indicator);
        nextPageButton = view.findViewById(R.id.next_page_button);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, new String[]{"Artic", "MET"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sourceSpinner.setAdapter(adapter);

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setAdapter(new ArtworkAdapter(new ArrayList<>(), this::showAddDialog));

        view.findViewById(R.id.search_button).setOnClickListener(v -> {
            currentPage = 1;
            searchArtworks(currentPage);
        });

        nextPageButton.setOnClickListener(v -> {
            currentPage++;
            searchArtworks(currentPage);
        });

        return view;
    }

    private void searchArtworks(int page) {
        String source = sourceSpinner.getSelectedItem().toString();
        String query = searchInput.getText().toString().trim();
        if (query.isEmpty()) return;

        lastSource = source;
        lastQuery = query;

        loadingIndicator.setVisibility(View.VISIBLE);
        nextPageButton.setEnabled(false);
        repository.searchArtworks(source, query, page, new Callback<List<Artwork>>() {
            @Override
            public void onResponse(Call<List<Artwork>> call, Response<List<Artwork>> response) {
                loadingIndicator.setVisibility(View.GONE);
                nextPageButton.setEnabled(true);
                if (response.isSuccessful()) {
                    recyclerView.setAdapter(new ArtworkAdapter(response.body(), BrowseFragment.this::showAddDialog));
                } else {
                    Toast.makeText(getContext(), "Error fetching artworks", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Artwork>> call, Throwable t) {
                loadingIndicator.setVisibility(View.GONE);
                nextPageButton.setEnabled(true);
                Toast.makeText(getContext(), "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showAddDialog(Artwork artwork) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Add to Gallery");

        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(16, 16, 16, 16);

        Spinner gallerySpinner = new Spinner(getContext());
        EditText nameInput = new EditText(getContext());
        nameInput.setHint("New gallery name");
        EditText descInput = new EditText(getContext());
        descInput.setHint("Description (optional)");

        layout.addView(gallerySpinner);
        layout.addView(nameInput);
        layout.addView(descInput);
        builder.setView(layout);

        repository.getGalleries(new Callback<List<Gallery>>() {
            @Override
            public void onResponse(Call<List<Gallery>> call, Response<List<Gallery>> response) {
                if (response.isSuccessful()) {
                    galleries = response.body();
                    Log.d("BrowseFragment", "Fetched galleries: " + galleries.size());
                    List<String> galleryNames = galleries.stream().map(Gallery::getName).collect(Collectors.toList());
                    galleryNames.add(0, "Create New Gallery");
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                            android.R.layout.simple_spinner_item, galleryNames);
                    gallerySpinner.setAdapter(adapter);
                } else {
                    Log.e("BrowseFragment", "Failed to fetch galleries: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Gallery>> call, Throwable t) {
                Log.e("BrowseFragment", "Network error fetching galleries: " + t.getMessage());
            }
        });

        builder.setPositiveButton("Add", (dialog, which) -> {
            String selected = gallerySpinner.getSelectedItem().toString();
            if ("Create New Gallery".equals(selected)) {
                String name = nameInput.getText().toString().trim();
                if (name.isEmpty()) return;
                String description = descInput.getText().toString().trim();
                Gallery newGallery = new Gallery(null, name, description);
                Log.d("BrowseFragment", "Creating gallery: " + newGallery.getName() + ", Desc: " + newGallery.getDescription());
                repository.createGallery(newGallery, new Callback<Gallery>() {
                    @Override
                    public void onResponse(Call<Gallery> call, Response<Gallery> response) {
                        if (response.isSuccessful()) {
                            Gallery createdGallery = response.body();
                            Log.d("BrowseFragment", "Gallery created: " + createdGallery.getName() + ", ID: " + createdGallery.getId());
                            addToGallery(createdGallery.getId(), artwork.getImageUrl());
                        } else {
                            Log.e("BrowseFragment", "Failed to create gallery: " + response.code() + ", Body: " + response.errorBody());
                            Toast.makeText(getContext(), "Error creating gallery", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Gallery> call, Throwable t) {
                        Log.e("BrowseFragment", "Network error creating gallery: " + t.getMessage());
                        Toast.makeText(getContext(), "Error creating gallery: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Gallery selectedGallery = galleries.stream()
                        .filter(g -> g.getName().equals(selected))
                        .findFirst().orElse(null);
                if (selectedGallery != null) {
                    addToGallery(selectedGallery.getId(), artwork.getImageUrl());
                }
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void addToGallery(Long galleryId, String imageUrl) { // Changed to Long
        repository.addArtworkToGallery(galleryId, imageUrl, new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("BrowseFragment", "Artwork added to gallery ID: " + galleryId);
                    Toast.makeText(getContext(), "Artwork added!", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("BrowseFragment", "Failed to add artwork: " + response.code());
                    Toast.makeText(getContext(), "Error adding artwork", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("BrowseFragment", "Network error adding artwork: " + t.getMessage());
                Toast.makeText(getContext(), "Error adding artwork: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}