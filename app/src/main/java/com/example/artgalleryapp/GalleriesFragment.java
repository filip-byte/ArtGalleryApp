package com.example.artgalleryapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GalleriesFragment extends Fragment {
    private RecyclerView recyclerView;
    private ArtRepository repository = new ArtRepository();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_galleries, container, false);
        recyclerView = view.findViewById(R.id.galleries_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        loadGalleries();
        return view;
    }

    private void loadGalleries() {
        repository.getGalleries(new Callback<List<Gallery>>() {
            @Override
            public void onResponse(Call<List<Gallery>> call, Response<List<Gallery>> response) {
                if (response.isSuccessful()) {
                    recyclerView.setAdapter(new GalleryAdapter(response.body(), gallery -> {
                        getParentFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, new GalleryArtworksFragment(gallery.getId()))
                                .addToBackStack(null)
                                .commit();
                    }));
                }
            }

            @Override
            public void onFailure(Call<List<Gallery>> call, Throwable t) {}
        });
    }
}