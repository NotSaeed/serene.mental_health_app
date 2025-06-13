package com.example.serene;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class GalleryActivity extends AppCompatActivity {

    RecyclerView galleryRecyclerView;


    int[] galleryImages = {
            R.drawable.mind_1,
            R.drawable.mind_2,
            R.drawable.mind_3,
            R.drawable.mind_4,
            R.drawable.mind_5,
            R.drawable.mind_6,
            R.drawable.mind_7,
            R.drawable.mind_8
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // back to MainActivity

        galleryRecyclerView = findViewById(R.id.galleryRecyclerView);
        galleryRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        GalleryAdapter adapter = new GalleryAdapter(this, galleryImages);
        galleryRecyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
