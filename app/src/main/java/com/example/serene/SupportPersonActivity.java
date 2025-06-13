package com.example.serene;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SupportPersonActivity extends AppCompatActivity {

    private LinearLayout storyContainer;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support_person);
        getSupportActionBar().setTitle("Support a Person");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        username = getIntent().getStringExtra("username");
        storyContainer = findViewById(R.id.storyContainer);

        loadStoriesFromFirebase();
        Button btnSubmitStory = findViewById(R.id.btnSubmitStory);
        btnSubmitStory.setOnClickListener(v -> {
            Intent intent = new Intent(SupportPersonActivity.this, SubmitStoryActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        });
    }

    private void loadStoriesFromFirebase() {
        DatabaseReference storiesRef = FirebaseDatabase.getInstance().getReference("donation_stories");

        storiesRef.get().addOnSuccessListener(snapshot -> {
            for (DataSnapshot storySnap : snapshot.getChildren()) {
                String name = storySnap.child("name").getValue(String.class);
                String location = storySnap.child("location").getValue(String.class);
                String story = storySnap.child("story").getValue(String.class);
                String storyId = storySnap.getKey();

                View storyView = LayoutInflater.from(this).inflate(R.layout.item_donation_story, storyContainer, false);

                ((TextView) storyView.findViewById(R.id.txtStoryName)).setText(name + " - " + location);
                ((TextView) storyView.findViewById(R.id.txtStoryText)).setText(story);

                storyView.findViewById(R.id.btnHelpNow).setOnClickListener(v -> {
                    Intent intent = new Intent(SupportPersonActivity.this, DonateToPersonActivity.class);
                    intent.putExtra("username", username);
                    intent.putExtra("storyId", storyId);
                    intent.putExtra("personName", name);
                    intent.putExtra("storyText", story);
                    startActivity(intent);
                });


                storyContainer.addView(storyView);
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
