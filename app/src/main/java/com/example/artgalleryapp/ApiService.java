package com.example.artgalleryapp;

import java.util.List;
import java.util.Map;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @GET("/api/artworks")
    Call<List<Artwork>> searchArtworks(
            @Query("source") String source,
            @Query("q") String query,
            @Query("page") int page,
            @Query("limit") int limit
    );

    @POST("/api/artworks/galleries")
    Call<Gallery> createGallery(@Body Gallery gallery);

    @POST("/api/artworks/galleries/{id}/artworks")
    Call<Void> addArtworkToGallery(
            @Path("id") String galleryId,
            @Body Map<String, String> artworkData
    );

    @GET("/api/artworks/galleries")
    Call<List<Gallery>> getGalleries();

    @GET("/api/artworks/galleries/{id}")
    Call<List<Artwork>> getGalleryArtworks(@Path("id") String id);
}