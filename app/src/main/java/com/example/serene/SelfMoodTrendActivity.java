package com.example.serene;

import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class SelfMoodTrendActivity extends AppCompatActivity {

    private MoodLineChartView chartView;
    private LinearLayout chartContainer;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_mood_trend);

        getSupportActionBar().setTitle("My Mood Chart");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        chartContainer = findViewById(R.id.chartContainer);

        chartView = new MoodLineChartView(this, null);
        chartView.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                800
        ));
        chartContainer.addView(chartView);

        username = getIntent().getStringExtra("username");
        if (username == null || username.trim().isEmpty()) {
            username = "guest"; // fallback
        }

        loadUserMoodData(username);
    }

    private void loadUserMoodData(String username) {
        FirebaseDatabase.getInstance().getReference("moods")
                .child(username)
                .get()
                .addOnSuccessListener(snapshot -> {
                    List<MoodEntry> moodEntries = new ArrayList<>();

                    for (DataSnapshot dateSnapshot : snapshot.getChildren()) {
                        try {
                            HashMap<String, Object> map = (HashMap<String, Object>) dateSnapshot.getValue();
                            if (map != null && map.containsKey("date") && map.containsKey("score")) {
                                String date = String.valueOf(map.get("date"));
                                long scoreLong = (long) map.get("score");
                                moodEntries.add(new MoodEntry(date, (int) scoreLong));
                            }
                        } catch (Exception e) {
                            Log.e("SelfMoodTrend", "Data parse error: " + e.getMessage());
                        }
                    }

                    if (moodEntries.isEmpty()) {
                        Toast.makeText(this, "No mood data found for user", Toast.LENGTH_SHORT).show();
                        return;
                    }


                    Collections.sort(moodEntries, Comparator.comparing(entry -> entry.date));

                    List<Integer> scores = new ArrayList<>();
                    List<String> labels = new ArrayList<>();

                    for (MoodEntry entry : moodEntries) {
                        scores.add(entry.score);
                        labels.add(entry.date.substring(5)); // MM-DD
                    }

                    chartView.setData(scores, labels);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to load mood data", Toast.LENGTH_SHORT).show();
                    Log.e("SelfMoodTrend", "Firebase error: ", e);
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public static class MoodEntry {
        String date;
        int score;

        public MoodEntry(String date, int score) {
            this.date = date;
            this.score = score;
        }
    }
}
