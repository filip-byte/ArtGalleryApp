package com.example.artgalleryapp;

import java.util.List;
import retrofit2.Callback;

public class ArtRepository {
    private ApiService apiService = ApiClient.getApiService();

    public void searchArtworks(String source, String query, int page, Callback<List<Artwork>> callback) {
        apiService.searchArtworks(source, query, page, 6).enqueue(callback);
    }

    public void createGallery(Gallery gallery, Callback<Gallery> callback) {
        apiService.createGallery(gallery.getName(), gallery.getDescription()).enqueue(callback);
    }

    public void addArtworkToGallery(Long galleryId, String imageUrl, Callback<Void> callback) { // Changed to Long
        apiService.addArtworkToGallery(galleryId, imageUrl).enqueue(callback);
    }

    public void getGalleries(Callback<List<Gallery>> callback) {
        apiService.getGalleries().enqueue(callback);
    }

    public void getGalleryArtworks(Long galleryId, Callback<List<Artwork>> callback) { // Changed to Long
        apiService.getGalleryArtworks(galleryId).enqueue(callback);
    }
}