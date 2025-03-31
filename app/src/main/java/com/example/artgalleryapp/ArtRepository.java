package com.example.artgalleryapp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import retrofit2.Callback;

public class ArtRepository {
    private ApiService apiService = ApiClient.getApiService();

    public void searchArtworks(String source, String query, int page, Callback<List<Artwork>> callback) {
        apiService.searchArtworks(source, query, page, 6).enqueue(callback);
    }

    public void createGallery(Gallery gallery, Callback<Gallery> callback) {
        apiService.createGallery(gallery).enqueue(callback);
    }

    public void addArtworkToGallery(String galleryId, String imageUrl, Callback<Void> callback) {
        Map<String, String> data = new HashMap<>();
        data.put("imageUrl", imageUrl);
        apiService.addArtworkToGallery(galleryId, data).enqueue(callback);
    }

    public void getGalleries(Callback<List<Gallery>> callback) {
        apiService.getGalleries().enqueue(callback);
    }

    public void getGalleryArtworks(String galleryId, Callback<List<Artwork>> callback) {
        apiService.getGalleryArtworks(galleryId).enqueue(callback);
    }
}