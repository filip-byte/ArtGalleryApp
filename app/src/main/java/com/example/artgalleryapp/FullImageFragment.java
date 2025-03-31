package com.example.artgalleryapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.fragment.app.Fragment;
import com.example.artgalleryapp.R;
import com.squareup.picasso.Picasso;

public class FullImageFragment extends Fragment {
    private static final String ARG_IMAGE_URL = "image_url";

    public FullImageFragment() {}

    public static FullImageFragment newInstance(String imageUrl) {
        FullImageFragment fragment = new FullImageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_IMAGE_URL, imageUrl);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_full_image, container, false);
        ImageView imageView = view.findViewById(R.id.full_image_view);

        String imageUrl = getArguments() != null ? getArguments().getString(ARG_IMAGE_URL) : null;
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Picasso.get().load(imageUrl).into(imageView);
        } else {
            imageView.setImageResource(android.R.drawable.ic_menu_gallery);
        }

        return view;
    }
}